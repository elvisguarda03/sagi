package br.com.ternarius.inventario.sagi.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ternarius.inventario.sagi.application.dto.CadastroDto;
import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.entity.UsuarioToken;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioTokenRepository;
import br.com.ternarius.inventario.sagi.domain.valueobject.EmailNotification;
import br.com.ternarius.inventario.sagi.domain.valueobject.Notification;
import br.com.ternarius.inventario.sagi.infrastructure.config.AppConfig;
import br.com.ternarius.inventario.sagi.infrastructure.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CadastroService {

	private final UsuarioTokenRepository usuarioTokenRepository;
	private final UsuarioRepository usuarioRepository;
	private final EmailNotificationService notificationService;
	private final AppConfig config;

	public Notification cadastrar(CadastroDto dto) {
		final Notification notification = validarSenha(dto);

		validarUsuario(notification, dto);

		if (notification.success()) {
			Usuario usuario = dto.toEntity();
			usuario.setAlreadyLoggedIn(false);
			usuario.criptografarSenha();
	
			usuarioRepository.save(usuario);
			log.info("Usuário cadastrado!");
		}

		return notification;
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

	public List<Usuario> findByStatus() {
		return usuarioRepository.findByStatus(StatusUsuario.CADASTRO_EM_ABERTO);
	}

	public Notification validarSenha(CadastroDto cadastroDto) {
		final Notification notification = new Notification();

		if (!cadastroDto.getSenha().equals(cadastroDto.getConfirmacaoSenha())) {
			notification.addError(Message.MSG_03.getMessage());
		}

		return notification;
	}

	private void validarUsuario(Notification notification, CadastroDto dto) {
		if (Objects.isNull(dto)) {
			notification.addError(Message.MSG_07.getMessage());
			return;
		}

		if (usuarioRepository.existsByEmail(dto.getEmail())) {
			notification.addError(Message.MSG_08.getMessage());
		}
	}

	public void sendConfirmationEmail(final String email, final String token) {
		Usuario u = usuarioRepository.findByEmail(email).get();

		String url = config.getUrl() + "/validar?email=" + email + "&token=" + token;

		EmailNotification pushNotification = EmailNotification.builder()
				.from("sagi@gmail.com")
				.to(u.getEmail())
				.title("Conta foi validada pelo Admin do sistema")
				.message("A sua conta já está efetivada, para efetuar o primeiro login no sistema acesse o link.")
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

	public List<Usuario> findByStatusCadastroEmAberto() {
		return usuarioRepository.findByStatus(StatusUsuario.CADASTRO_EM_ABERTO);
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