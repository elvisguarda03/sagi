package br.com.ternarius.inventario.sagi.domain.service;

import br.com.ternarius.inventario.sagi.domain.entity.Movimentacao;
import br.com.ternarius.inventario.sagi.domain.repository.MovimentacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Elvis de Sousa
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;

    public Movimentacao cadastrar(Movimentacao movimentacao) {
        return movimentacaoRepository.save(movimentacao);
    }

    public List<Movimentacao> findAll() {
        return movimentacaoRepository.findAll();
    }

    public List<Movimentacao> findByDataPermissao(LocalDate localDate) {
        return movimentacaoRepository.findByDataPermissao(localDate);
    }

    public void giveBack(String id) {
        var movimentacao = movimentacaoRepository.findById(id).get();
        movimentacao.getEquipamento().setLaboratorio(movimentacao.getLocalizacaoAnterior());
        movimentacao.setStatus(true);

        cadastrar(movimentacao);
    }

    public Optional<Movimentacao> findById(String id) {
        return movimentacaoRepository.findById(id);
    }

    public List<Movimentacao> find(String nomeEquipamento, String nomeAdmin, String solicitante, String localizacaoAtual, EntityManager em) {
        return movimentacaoRepository.find(nomeEquipamento, localizacaoAtual, nomeAdmin, solicitante, em);
    }
}
