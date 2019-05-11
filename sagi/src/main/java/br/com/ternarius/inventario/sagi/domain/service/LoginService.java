package br.com.ternarius.inventario.sagi.domain.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.entity.UsuarioToken;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
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
public class LoginService {
	
	private final UsuarioRepository usuarioRepository;
	private final UsuarioTokenRepository usuarioTokenRepository;
	private final EmailNotificationService notificationService;
	private final AppConfig config;
	
	public Notification recuperarSenha(String email) {
		Notification notification = new Notification();
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		
		if (usuario.isPresent()) {
			UsuarioToken usuarioToken = UsuarioToken.builder()
					.usuario(usuario.get())
					.token(SecureRandom.hex())
					.dataExpiracao(LocalDateTime.now().plusHours(24))
					.build();
			
			usuarioToken.criptografarToken();
			usuarioTokenRepository.save(usuarioToken);
		
			final String link = config.getUrl() + "/nova-senha?token=" + usuarioToken.getToken() + "&email=" + email;
			final EmailNotification pushNotification = EmailNotification.builder()
					.to(email)
					.from("nao-responder@capivaras.com.br")
					.title("Recuperação de Senha")
					.template("recupera-senha")
					.url(link)
					.build();
			
			notificationService.send(pushNotification);
			log.info("Email para redefinição de senha para o usuário " + usuario.get().getNome() + " enviado");
			
			return notification;
		}
	
		notification.addError(Message.MSG_09.getMessage());
		
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
		
		if(!usuario.isPresent()) {
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
	
	@Transactional
	public Notification editarSenha(String token, String senha) {
		final Notification notification = new Notification();
		
		Optional<UsuarioToken> registro = validarToken(notification, token);
		
		if (notification.success()) {
			final Usuario usuario = registro.get().getUsuario();
			usuario.setSenha(senha);
			usuario.criptografarSenha();
			
			usuarioRepository.updatePassword(usuario.getId(), usuario.getSenha());
			usuarioTokenRepository.delete(registro.get());
		}
		
		return notification;
	}
	
	private boolean validarTokenAtivo(Optional<UsuarioToken> registro) {
		return registro.isPresent() && registro.get().ativo();
	}
}