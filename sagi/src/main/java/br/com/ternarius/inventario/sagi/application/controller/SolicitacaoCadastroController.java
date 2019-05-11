package br.com.ternarius.inventario.sagi.application.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.domain.service.CadastroService;
import br.com.ternarius.inventario.sagi.util.ConverterEntitiesToDtos;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Elvis da Guarda
 *
 */

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("admin/solicita-cadastro")
public class SolicitacaoCadastroController extends BaseController {

	private final UsuarioRepository usuarioRepository;
	private final CadastroService service;
	
	@GetMapping
	public ModelAndView index(ModelAndView modelAndView, @AuthenticationPrincipal UserDetails userDetails) {
		if (!IsLogged()) {
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		
		userLogged(modelAndView, userDetails, usuarioRepository);
		
		modelAndView.addObject("usuariosDto", new ConverterEntitiesToDtos().fromUsuarios(service.findByStatus()));
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
	public ModelAndView reject(@PathVariable("idUsuario") Optional<UUID> idUsuario, ModelAndView modelAndView) {
//		Excluir o usuário das solicitações pendentes
		return modelAndView;
	}
}