package br.com.ternarius.inventario.sagi.application.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ternarius.inventario.sagi.application.dto.RecuperaSenhaDto;
import br.com.ternarius.inventario.sagi.domain.service.LoginService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/esqueci-minha-senha")
public class RecuperaSenhaController extends BaseController {

	private final LoginService service;
	
	@GetMapping
	public ModelAndView index(ModelAndView modelAndView) {
		if (IsLogged()) {
			modelAndView.setViewName("redirect:/dashboard");
			return modelAndView;
		}
		
		modelAndView.setViewName("login/esqueci-senha");
		modelAndView.addObject("dto", new RecuperaSenhaDto());
		
		return modelAndView;
	}
	
	@PostMapping
	public ModelAndView create(@Valid @ModelAttribute("dto") RecuperaSenhaDto dto, BindingResult result,
			ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
		if (hasErrors(result, modelAndView)) {
			modelAndView.setViewName("login/esqueci-senha");
			modelAndView.addObject("dto", dto);

			return modelAndView;
		}
		
		final var notification = service.recuperarSenha(dto.getEmail());
		
		if (notification.fail()) {
			modelAndView.addObject("error,", true);
			modelAndView.addObject("msg", notification.getFirstError());

			modelAndView.setViewName("login/esqueci-senha");

			return modelAndView;
		}
		
		modelAndView.setViewName("redirect:/esqueci-minha-senha");

		var msg = "Acabamos de enviar um e-mail com um link para você reestabelecer a sua senha.\n" + dto.getEmail()
		+ "\nIMPORTANTE! Lembre-se de verificar a pasta \"lixo\" e \"correio não desejado\".";

		redirectAttributes.addFlashAttribute("success", true);
		redirectAttributes.addFlashAttribute("msg", msg);
		
		return modelAndView;
	}
}