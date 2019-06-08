package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.domain.service.CadastroService;
import br.com.ternarius.inventario.sagi.domain.service.PushNotificationService;
import br.com.ternarius.inventario.sagi.util.ConverterEntitiesToDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 
 * @author Elvis da Guarda
 *
 */

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') OR hasRole('MOD')")
@RequestMapping("admin/solicita-cadastro")
public class SolicitaCadastroController extends BaseController {

	private final UsuarioRepository usuarioRepository;
	private final PushNotificationService pushNotificationService;
	private final CadastroService service;
	
	@GetMapping
	public ModelAndView index(ModelAndView modelAndView) {
		if (!IsLogged()) {
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		
		modelAndView.addObject("usuariosDto", new ConverterEntitiesToDtos().fromUsuarios(service.findByStatus(StatusUsuario.CADASTRO_EM_ABERTO)));
		modelAndView.setViewName("admin/index");
		
		return modelAndView;
	}
	
	@GetMapping(value = "/permite/usuario/{idUsuario}")
	public ModelAndView permit(@PathVariable("idUsuario") Optional<String> record, ModelAndView modelAndView, RedirectAttributes attributes) {
		var actionName = "redirect:/admin/solicita-cadastro";
		
		if (!hasRecord(record, usuarioRepository, modelAndView, actionName)) {
			return modelAndView;
		}
		
		service.ativar(record.get());
		
		attributes.addFlashAttribute("msg", Message.MSG_12.getMessage());
		modelAndView.setViewName(actionName);
		
		return modelAndView;
	}

	@GetMapping(value = "/rejeita/usuario/{idUsuario}")
	public ModelAndView reject(@PathVariable("idUsuario") Optional<String> record, ModelAndView modelAndView,
							   RedirectAttributes attributes) {
		var actionName = "admin/index";

		if (!hasRecord(record, usuarioRepository, modelAndView, actionName)) {
			return modelAndView;
		}

		service.desativar(record.get());

		modelAndView.setViewName("redirect:/admin-solicita-cadastro");
		attributes.addFlashAttribute("msg", Message.MSG_13.getMessage());

		return modelAndView;
	}
}