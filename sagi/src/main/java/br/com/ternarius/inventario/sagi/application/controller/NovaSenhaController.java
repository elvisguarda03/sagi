package br.com.ternarius.inventario.sagi.application.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ternarius.inventario.sagi.application.dto.NovaSenhaDto;
import br.com.ternarius.inventario.sagi.domain.service.LoginService;
import br.com.ternarius.inventario.sagi.domain.valueobject.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/nova-senha")
public class NovaSenhaController extends BaseController {

	private final LoginService service;

	@GetMapping
	public ModelAndView index(@RequestParam(name = "token") String token,
			@RequestParam(name = "email") String email, ModelAndView modelAndView) {
		final var notification = service.validar(email, token);

		if (notification.fail()) {
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		
		modelAndView.addObject("dto", NovaSenhaDto.builder().token(token).build());
		modelAndView.setViewName("login/nova-senha");
		
		return modelAndView;
	}

	@PostMapping
	public ModelAndView update(@Valid @ModelAttribute("dto") NovaSenhaDto dto, BindingResult result,
			ModelAndView modelAndView, RedirectAttributes attributes) {
		if (hasErrors(result, attributes)) {
			modelAndView.setViewName("login/nova-senha");
			
			return modelAndView;
		}
		
		final var notification = service.editarSenha(dto.getToken(), dto.getSenha());

		if (notification.fail()) {
			modelAndView.addObject("erro", notification.getFirstError());
			modelAndView.setViewName("login/nova-senha");
			return modelAndView;
		}
		
		modelAndView.setViewName("redirect:/login?resetada");
		
		log.info("Senha redefinida!");
		
		return modelAndView;
	}
}