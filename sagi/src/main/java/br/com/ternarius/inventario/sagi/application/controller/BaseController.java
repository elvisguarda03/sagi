package br.com.ternarius.inventario.sagi.application.controller;

import java.util.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.enums.TipoUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.Repository;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.application.error.ValidationMessage;

/**
 * 
 * @author Elvis da Guarda
 *
 */
public abstract class BaseController {
	
	public Boolean IsLogged() {
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal() instanceof UserDetails && 
			   SecurityContextHolder.getContext()
				.getAuthentication().isAuthenticated();
	}
	
	private Boolean IsAdmin(UsuarioRepository repository) {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
	
		Optional<Usuario> record = repository.findByEmail(user.getUsername());
		
		if (!record.isPresent()) {
			return false;
		}
		
		if (!record.get().getTipo().equals(TipoUsuario.ADMIN)) {
			return false;
		}
		
		return true;
	}
	
	private void hasCredential(UsuarioRepository repository, ModelAndView modelAndView) {
		if (IsAdmin(repository)) {
			modelAndView.addObject("isAdmin", true);
		}
	}
	
	public void refresh(UserDetails userDetails) {
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public Boolean hasErrors(BindingResult result, List<ValidationMessage> messages) {
		if (result.hasErrors()) {
			result.getFieldErrors().forEach(f -> messages.add(ValidationMessage.builder()
					.field(f.getField())
					.message(f.getDefaultMessage())
					.build()));

			return true;
		}

		return false;
	}

	public Boolean hasErrors(BindingResult result, ModelAndView modelAndView) {
		if (result.hasErrors()) {
			Map<String, String> messages = new HashMap<>();
			result.getFieldErrors().forEach(f -> messages.put(f.getField(), f.getDefaultMessage()));
			modelAndView.addObject("errors", messages);

			return true;
		}
		return false;
	}
	
	public Boolean hasErrors(BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			Map<String, String> messages = new HashMap<>();
			result.getFieldErrors().forEach(e -> messages.put(e.getField(), e.getDefaultMessage()) );
			
			attributes.addFlashAttribute("errors", messages);
			
			return true;
		}
		return false;
	}
	
	
	public void login(Optional<Usuario> record) {
		if (!record.isPresent()) {
			return;
		}
		
		final Set<GrantedAuthority> grantedAuthorities =  new HashSet<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(record.get().getTipo().toString()));
		
		User user = new User(record.get().getEmail(), record.get().getSenha(), grantedAuthorities);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	public Boolean hasRecord(Optional<String> record, Repository repository, ModelAndView modelAndView, String actionName) {
		if (!record.isPresent() || !repository.existsById(record.get())) {
			modelAndView.setViewName(actionName);
			return false;
		}
		
		return true;
	}
	
	public void userLogged(ModelAndView modelAndView, UserDetails loggedInUser, UsuarioRepository repository) {
		Optional<Usuario> user = repository.findByEmail(loggedInUser.getUsername());
		
		if (user.isPresent()) {
			modelAndView.addObject("nome", user.get().getNome());
			modelAndView.addObject("email", user.get().getEmail());
			
			if (!user.get().getAlreadyLoggedIn()) {
				modelAndView.addObject("firstLogin", true);
				user.get().setAlreadyLoggedIn(true);
				return;
			}
			
			modelAndView.addObject("firstLogin", false);
			hasCredential(repository, modelAndView);
		}
	}
}