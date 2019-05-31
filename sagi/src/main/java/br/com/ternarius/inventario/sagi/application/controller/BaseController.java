package br.com.ternarius.inventario.sagi.application.controller;

import java.util.*;

import org.springframework.http.HttpStatus;
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
import br.com.ternarius.inventario.sagi.domain.repository.Repository;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Elvis da Guarda
 */
public abstract class BaseController {

    public Boolean IsLogged() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal() instanceof UserDetails &&
                SecurityContextHolder.getContext()
                        .getAuthentication().isAuthenticated();
    }

    public void refresh(UserDetails userDetails) {
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public Boolean hasErrors(BindingResult result, ModelAndView modelAndView) {
        if (result.hasErrors()) {
            var messages = new HashMap<String, String>();
            result.getFieldErrors().forEach(f -> messages.put(f.getField(), f.getDefaultMessage()));

            modelAndView.addObject("errors", messages);
            modelAndView.addObject("errorEmail", true);

            return true;
        }

        return false;
    }

    public Boolean hasErrors(BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            var messages = new HashMap<String, String>();
            result.getFieldErrors().forEach(e -> messages.put(e.getField(), e.getDefaultMessage()));

            attributes.addFlashAttribute("errors", messages);

            return true;
        }

        return false;
    }


    public void login(Optional<Usuario> record) {
        if (!record.isPresent()) {
            return;
        }

        final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(record.get().getTipoUsuario().toString()));

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

    public void userLogged(HttpSession httpSession, UserDetails loggedInUser, UsuarioRepository repository) {
        Optional<Usuario> record = repository.findByEmail(loggedInUser.getUsername());

        record.ifPresent(u -> {
            httpSession.setAttribute("nome", u.getNome());
            httpSession.setAttribute("email", u.getEmail());

            if (u.getAlreadyLoggedIn()) {
                httpSession.setAttribute("firstLogin", true);

                u.setAlreadyLoggedIn(true);

                return;
            }
        });

        httpSession.setAttribute("firstLogin", false);
    }

    protected Boolean IsAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .toString().equals("ROLE_ADMIN");
    }

    protected String redirectToDashboard() {
        return "redirect:/dashboard";
    }

    protected void redirectToLogin(ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/login");
    }

    protected Boolean is3xxRedirection(HttpServletResponse response) {
        return response.getStatus() == HttpStatus.FOUND.value()
                || response.getStatus() == HttpStatus.MOVED_PERMANENTLY.value();
    }
}