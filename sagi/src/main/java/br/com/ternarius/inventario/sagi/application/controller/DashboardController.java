package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import com.currencyfair.onesignal.OneSignal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 *
 * @author Elvis da Guarda
 *
 */
@Controller
@RequiredArgsConstructor
@RequestMapping({"/dashboard" ,"/"})
public class DashboardController extends BaseController {

	private final UsuarioRepository repository;

	@GetMapping
	public ModelAndView index(ModelAndView modelAndView, @AuthenticationPrincipal UserDetails userDetails, HttpSession httpSession) {
		if (!IsLogged()) {
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}

		userLogged(httpSession, userDetails, repository);

		httpSession.setAttribute("authorization", IsAdmin(userDetails));
		modelAndView.setViewName("dashboard/index");

		return modelAndView;
	}
}