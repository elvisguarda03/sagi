package br.com.ternarius.inventario.sagi.infrastructure.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

	private final UsuarioRepository repositorio;
	private final String PREFIX_AUTHORITY = "ROLE_";
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final Optional<Usuario> record = this.repositorio.findByEmail(username);

		if (!record.isPresent() || 
				record.get().getStatus().equals(StatusUsuario.CONFIRMACAO_EMAIL_PENDENTE)) {
			return null;
		}
		
		final Usuario usuario = record.get();
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		
		AuthorityUtils.createAuthorityList(PREFIX_AUTHORITY + usuario.getTipo().toString())
				.forEach(a -> grantedAuthorities.add(a));
		
		return new User(usuario.getEmail(), usuario.getSenha(), grantedAuthorities);
	}
}