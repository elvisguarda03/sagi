package br.com.ternarius.inventario.sagi.domain.service;

import br.com.ternarius.inventario.sagi.application.dto.SolicitacaoEquipamentoDto;
import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.SolicitacaoEquipamento;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.enums.TipoUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.EquipamentoRepository;
import br.com.ternarius.inventario.sagi.domain.repository.LaboratorioRepository;
import br.com.ternarius.inventario.sagi.domain.repository.SolicitacaoEquipamentoRepository;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.domain.valueobject.Notification;
import br.com.ternarius.inventario.sagi.domain.valueobject.SolicitacaoNotification;
import br.com.ternarius.inventario.sagi.infrastructure.config.AppConfig;
import br.com.ternarius.inventario.sagi.infrastructure.config.SendGridConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.ternarius.inventario.sagi.domain.enums.TipoUsuario.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitacaoEquipamentoService extends BaseService {

    private final SolicitacaoEquipamentoRepository solicitacaoEquipamentoRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailNotificationService notificationService;
    private final SendGridConfig sendGridConfig;
    private final AppConfig config;

    public Optional<SolicitacaoEquipamento> findById(String id) {
        return solicitacaoEquipamentoRepository.findById(id);
    }

    public Notification validaAprovacaoMovimentacao(TipoUsuario tipoUsuario, Equipamento equipamento) {
        final var notification = new Notification();

        if (tipoUsuario.equals(TipoUsuario.MOD) &&
                equipamento.isValor()) {
            notification.addError(Message.MSG_21.getMessage());
        }

        return notification;
    }

    public SolicitacaoEquipamento cadastrar(SolicitacaoEquipamento solicitacaoEquipamento) {
        return solicitacaoEquipamentoRepository.save(solicitacaoEquipamento);
    }

    public List<SolicitacaoEquipamento> fimdAll() {
        return solicitacaoEquipamentoRepository.findAll();
    }

    public void deleteById(String id) {
        solicitacaoEquipamentoRepository.deleteById(id);
    }

    public void solicitar(SolicitacaoEquipamento solicitacaoEquipamento, List<String> equipamentosId, String laboratorioId) {
        equipamentosId.forEach(e -> {
            var equipamento = equipamentoRepository.findById(e)
                    .get();

            var novaLocalizacao = laboratorioRepository.findById(laboratorioId)
                    .get();

            solicitacaoEquipamento.setEquipamento(equipamento);
            solicitacaoEquipamento.setNovaLocalizacao(novaLocalizacao);

            cadastrar(solicitacaoEquipamento);
        });

        var message = "O usuário " + solicitacaoEquipamento.getComodatario().getNome() + " acaba de solicitar a movimentação de um equipamento. Efetue login  para averiguar!!\n";
        var solicitacao = SolicitacaoNotification.builder()
                .message(message)
                .nome(solicitacaoEquipamento.getComodatario().getNome())
                .templateId(sendGridConfig.getSendGridTemplateCadastro())
                .title("Solicitação Cadastro")
                .url(config.getUrl() + "equipamento/solicita-emprestimo")
                .build();

        sendAllNotificationForEmail(solicitacao, notificationService, usuarioRepository
                .findByTipoUsuario(USER));
    }
}
