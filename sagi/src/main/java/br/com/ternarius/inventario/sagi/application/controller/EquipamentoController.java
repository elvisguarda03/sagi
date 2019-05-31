package br.com.ternarius.inventario.sagi.application.controller;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping
    public ModelAndView index(ModelAndView modelAndView) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        modelAndView.addObject("equipamentosDto", new ConverterEntitiesToDtos().fromEquipamentos(equipamentoService.findAll()));
        modelAndView.setViewName("equipamento/index");

        return modelAndView;
    }

    @GetMapping("/cadastro-equipamento")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView create(ModelAndView modelAndView) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

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
    public ModelAndView edit(@PathVariable("idEquipamento") Optional<String> record, ModelAndView modelAndView) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        var actionName = "redirect:/equipamento";

        if (!hasRecord(record, equipamentoRepository, modelAndView, actionName)) {
            return modelAndView;
        }

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
    public ModelAndView delete(@PathVariable("idEquipamento") Optional<String> record, ModelAndView modelAndView,
                               RedirectAttributes attributes) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        var actionName = "equipamento/index";

        if (!hasRecord(record, equipamentoRepository, modelAndView, actionName)) {
            return modelAndView;
        }

        equipamentoService.unavailable(record.get());

        modelAndView.setViewName("redirect:/equipamento");
        attributes.addFlashAttribute("msg", Message.MSG_16.getMessage());
        attributes.addFlashAttribute("success", true);

        return modelAndView;
    }

    @Transactional
    @GetMapping("/solicita-manutencao/{idEquipamento}")
    public ModelAndView needMaintenance(@PathVariable("idEquipamento") Optional<String> record, ModelAndView modelAndView,
                                        RedirectAttributes attributes) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        var actionName = "equipamento/index";

        if (!hasRecord(record, equipamentoRepository, modelAndView, actionName)) {
            return modelAndView;
        }

        var equipamento = equipamentoService.findById(record.get()).get();
        equipamentoService.updateStatusMaitenance(equipamento.getId(), equipamento.getIsMaintenance());

        modelAndView.setViewName("redirect:/equipamento");
        attributes.addFlashAttribute("", null);

        return modelAndView;
    }
}