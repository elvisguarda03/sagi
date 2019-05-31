package br.com.ternarius.inventario.sagi.domain.service;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.entity.UsuarioToken;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.enums.TipoUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioTokenRepository;
import br.com.ternarius.inventario.sagi.domain.valueobject.EmailNotification;
import br.com.ternarius.inventario.sagi.domain.valueobject.Notification;
import br.com.ternarius.inventario.sagi.infrastructure.config.AppConfig;
import br.com.ternarius.inventario.sagi.infrastructure.config.SendGridConfig;
import br.com.ternarius.inventario.sagi.infrastructure.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Elvis da Guarda
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CadastroService {

    private final UsuarioTokenRepository usuarioTokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailNotificationService notificationService;
    private final SendGridConfig sendGridConfig;
    private final AppConfig config;

    public Notification cadastrar(Usuario usuario) {
        final Notification notification = validarUsuario(usuario);

        if (notification.success()) {
            usuario.criptografarSenha();

            usuarioRepository.save(usuario);

            sendAllNotificationForEmail(usuario.getNome());

            log.info("Usuário cadastrado!");
        }

        return notification;
    }

    private void sendAllNotificationForEmail(String nome) {
        var url = config.getUrl() + "admin/solicita-cadastro";

        var admins = usuarioRepository.findByTipoUsuarioOrTipoUsuario(TipoUsuario.ADMIN, TipoUsuario.MOD);

        admins.forEach(a -> {
            EmailNotification pushNotification = EmailNotification.builder()
                    .from("sagi@gmail.com")
                    .to(a.getEmail())
                    .template(sendGridConfig.getSendGridTemplateCadastro())
                    .nome(nome)
                    .title("Solicitação de Cadastro")
                    .message("O usuário " + nome + " acaba de se cadastrar no sistema e aguarda pela sua definição. Efetue login  para validar!!")
                    .url(url)
                    .build();
            notificationService.send(pushNotification);
        });
    }

    public void ativar(String id) {
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setStatus(StatusUsuario.CONFIRMACAO_EMAIL_PENDENTE);

        UsuarioToken usuarioToken = buildToken(usuario);
        usuarioToken.criptografarToken();
        usuarioTokenRepository.save(usuarioToken);

        sendConfirmationEmail(usuario.getEmail(), usuarioToken.getToken());
        log.info("Email enviado!");
    }

    public void desativar(String id) {
        var usuario = usuarioRepository.findById(id).get();
        usuarioRepository.deleteById(id);

        log.info("Solicitação para cadastro do usuário " + usuario.getNome() + " deletada.");
    }

    public List<Usuario> findByStatus(StatusUsuario statusUsuario) {
        return usuarioRepository.findByStatus(statusUsuario);
    }

    private Notification validarUsuario(Usuario usuario) {
        final var notification = new Notification();

        if (Objects.isNull(usuario)) {
            notification.addError(Message.MSG_07.getMessage());
            return notification;
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            notification.addError(Message.MSG_08.getMessage());
        }

        return notification;
    }

    public void sendConfirmationEmail(final String email, final String token) {
        Usuario u = usuarioRepository.findByEmail(email).get();

        String url = config.getUrl() + "/validar?email=" + email + "&token=" + token;

        EmailNotification pushNotification = EmailNotification.builder()
                .from("sagi@gmail.com")
                .to(u.getEmail())
                .template(sendGridConfig.getSendGridTemplateCadastro())
                .nome(u.getNome())
                .title("Conta foi validada pelo Admin do sistema")
                .message("A sua conta já está efetivada, para efetuar o primeiro login no sistema clique no botão abaixo.")
                .url(url)
                .build();

        notificationService.send(pushNotification);
    }

    @Transactional
    public Notification ativarUsuario(final String email, final String token) {
        final Notification notification = validar(email, token);

        if (notification.fail()) {
            return notification;
        }

        Usuario user = usuarioRepository.findByEmail(email).get();
        UsuarioToken usuarioToken = usuarioTokenRepository.findByToken(token).get();

        usuarioRepository.updateStatus(user.getId(), StatusUsuario.EMAIL_VALIDADO);
        usuarioTokenRepository.delete(usuarioToken);

        log.info("Usuário ativado!");

        return notification;
    }

    public Notification validar(String email, String token) {
        final Notification notification = validarEmail(email);

        if (notification.fail()) {
            return notification;
        }

        validarToken(notification, token);

        return notification;
    }

    public Notification validarEmail(String email) {
        final Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        final Notification notification = new Notification();

        if (!usuario.isPresent()) {
            notification.addError(Message.MSG_09.getMessage());
        }

        return notification;
    }

    public Optional<UsuarioToken> validarToken(final Notification notification, String token) {
        final Optional<UsuarioToken> registro = usuarioTokenRepository.findByToken(token);

        if (!validarTokenAtivo(registro)) {
            notification.addError(Message.MSG_10.getMessage());
            return null;
        }

        return registro;
    }

    private UsuarioToken buildToken(Usuario user) {
        final UsuarioToken usuarioToken = UsuarioToken.builder()
                .usuario(user)
                .dataExpiracao(LocalDateTime.now().plusHours(24))
                .token(SecureRandom.hex())
                .build();

        usuarioToken.criptografarToken();
        usuarioTokenRepository.save(usuarioToken);

        return usuarioToken;
    }

    private boolean validarTokenAtivo(Optional<UsuarioToken> registro) {
        return registro.isPresent() && registro.get().ativo();
    }
}