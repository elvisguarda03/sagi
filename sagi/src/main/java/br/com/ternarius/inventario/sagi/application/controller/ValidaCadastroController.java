package br.com.ternarius.inventario.sagi.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.service.CadastroService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("cadastro/validar")
public class ValidaCadastroController extends BaseController {

	private final CadastroService service;
	
	@GetMapping
	public ModelAndView index(@RequestParam("email") String email, @RequestParam("token") String token,
			ModelAndView modelAndView, RedirectAttributes attributes) {
		final var notification = service.ativarUsuario(email, token);

		modelAndView.setViewName("redirect:/login");

		if (notification.fail()) {
			attributes.addFlashAttribute("msg", Message.MSG_10.getMessage());
			return modelAndView;
		}

		attributes.addFlashAttribute("msg", Message.MSG_11.getMessage());

		return modelAndView;
	}
}