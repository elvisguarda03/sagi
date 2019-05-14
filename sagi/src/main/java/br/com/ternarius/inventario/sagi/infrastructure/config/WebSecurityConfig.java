package br.com.ternarius.inventario.sagi.infrastructure.config;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
				authorizeRequests()
				.antMatchers("/cadastro-usuario").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/esqueci-minha-senha").permitAll()
				.antMatchers("/nova-senha").permitAll()
				.antMatchers("/validar").permitAll()
				.antMatchers("/api/**").permitAll()
				.anyRequest().authenticated()
				.and().formLogin()
						.loginPage("/login")
							.permitAll()
								.failureUrl("/login/erro")
									.defaultSuccessUrl("/dashboard", true)
				.and()
					.logout()
                    	.invalidateHttpSession(true)
                    	.clearAuthentication(true)
                    	.deleteCookies("JSESSIONID")
                    	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    	.logoutSuccessUrl("/login?logout")
                    .permitAll()
                    .and()
                    .rememberMe()
                    .rememberMeParameter("remember-me")
                    .key("remember-me")
                    .tokenRepository(persistentTokenRepository())
                    .userDetailsService(userDetailsService);

		http.csrf().disable();
	}
	
	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring()
        	.antMatchers(
	            "/static/**",
	            "/bootstrap/**",
	            "/js/**",
	            "/css/**",
	            "/videos/**",
	            "/images/**",
	            "/resources/**"
        	 );
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		tokenRepositoryImpl.setDataSource(dataSource);
		
		return tokenRepositoryImpl;
	}
}