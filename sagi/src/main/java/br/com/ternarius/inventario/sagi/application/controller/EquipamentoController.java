package br.com.ternarius.inventario.sagi.application.controller;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ternarius.inventario.sagi.application.dto.EquipamentoDto;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.repository.EquipamentoRepository;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.domain.service.EquipamentoService;
import br.com.ternarius.inventario.sagi.domain.service.LaboratorioService;
import br.com.ternarius.inventario.sagi.util.ConverterEntitiesToDtos;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/equipamento")
public class EquipamentoController extends BaseController {

	private final EquipamentoService equipamentoService;
	private final EquipamentoRepository equipamentoRepository;
	private final LaboratorioService laboratorioService;
	private final UsuarioRepository usuarioRepository;
	
	
	@GetMapping
	public ModelAndView index(ModelAndView modelAndView, @AuthenticationPrincipal UserDetails userDetails) {
		if (!IsLogged()) {
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		
		userLogged(modelAndView, userDetails, usuarioRepository);
		
		modelAndView.addObject("equipamentosDto", new ConverterEntitiesToDtos().fromEquipamentos(equipamentoService.findAll()));
		modelAndView.setViewName("equipamento/index");
		
		return modelAndView;
	}

	@GetMapping("/cadastro-equipamento")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView create(ModelAndView modelAndView, @AuthenticationPrincipal UserDetails userDetails) {
		if (!IsLogged()) {
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		
		userLogged(modelAndView, userDetails, usuarioRepository);
		
		modelAndView.addObject("laboratoriosDto", new ConverterEntitiesToDtos().fromLaboratorios(laboratorioService.findAll()));
		modelAndView.addObject("equipamentoDto", new EquipamentoDto());
		modelAndView.setViewName("equipamento/create");
		
		return modelAndView;
	}

	@Transactional
	@PostMapping("/cadastro-equipamento")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView save(@Valid @ModelAttribute("equipamentoDto") EquipamentoDto dto, BindingResult result,
			ModelAndView modelAndView, RedirectAttributes attributes) {
		if (hasErrors(result, modelAndView)) {
			modelAndView.addObject("equipamentoDto", dto);
			modelAndView.addObject("laboratoriosDto", new ConverterEntitiesToDtos().fromLaboratorios(laboratorioService.findAll()));
			modelAndView.setViewName("equipamento/create");
		
			return modelAndView;
		}
		
		equipamentoService.cadastrar(dto.toEntity());
		
		attributes.addFlashAttribute("msg", Message.MSG_06.getMessage());
		modelAndView.setViewName("redirect:/equipamento");
		
		return modelAndView;
	}

	@GetMapping(value = "/editar-equipamento/{idEquipamento}")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView edit(@PathVariable("idEquipamento") Optional<String> record, @AuthenticationPrincipal UserDetails userDetails, ModelAndView modelAndView) {
		if (!IsLogged()) {
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		
		String actionName = "redirect:/equipamento";
		
		if (!hasRecord(record, equipamentoRepository, modelAndView, actionName)) {
			return modelAndView;
		}
		
		userLogged(modelAndView, userDetails, usuarioRepository);
		
		var equipamento = equipamentoRepository.findById(record.get()).get();
		var dto = new EquipamentoDto(equipamento);
		
		modelAndView.setViewName("equipamento/edit");
		modelAndView.addObject("laboratoriosDto", new ConverterEntitiesToDtos().fromLaboratorios(laboratorioService.findAll()));
		modelAndView.addObject("laboratorioDto", dto.getLaboratorio());
		modelAndView.addObject("equipamentoDto", dto);
		
		return modelAndView;
	}
	
	@Transactional
	@PostMapping("/editar-equipamento")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView edit(@Valid @ModelAttribute("equipamentoDto") EquipamentoDto dto, BindingResult result,
			ModelAndView modelAndView, RedirectAttributes attributes) {
		if (hasErrors(result, attributes)) {
			attributes.addFlashAttribute("success", false);
			modelAndView.setViewName("redirect:/equipamento/editar-equipamento/" + dto.getId());
			
			return modelAndView;
		}
		
		equipamentoService.cadastrar(dto.toEntity());

		attributes.addFlashAttribute("success", true);
		modelAndView.setViewName("redirect:/equipamento");
		
		return modelAndView;
	}
	
	@Transactional
	@GetMapping("/deletar-equipamento/{idEquipamento}")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView delete(@PathVariable("idEquipamento") Long id, ModelAndView modelAndView) {
		return modelAndView;
	}
}