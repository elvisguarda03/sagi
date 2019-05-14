package br.com.ternarius.inventario.sagi.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

	@GetMapping
	public String login() {
		if (IsLogged()) {
			return "redirect:/dashboard";
		}
		return "login/index";
	}

	@GetMapping("/erro")
	public String errorLogin(Model model) {
		model.addAttribute("loginError", true);
		return "login/index";
	}
}