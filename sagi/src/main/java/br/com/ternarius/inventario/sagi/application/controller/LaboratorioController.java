package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.application.dto.LaboratorioDto;
import br.com.ternarius.inventario.sagi.domain.enums.Edificio;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.repository.LaboratorioRepository;
import br.com.ternarius.inventario.sagi.domain.service.LaboratorioService;
import br.com.ternarius.inventario.sagi.util.ConverterEntitiesToDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Elvis da Guarda
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/laboratorio")
public class LaboratorioController extends BaseController {

    @PersistenceContext
    private final EntityManager em;
    private final LaboratorioService service;
    private final LaboratorioRepository laboratorioRepository;

    @GetMapping
    public ModelAndView index(ModelAndView modelAndView) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        modelAndView.addObject("dto", new LaboratorioDto());
        modelAndView.addObject("laboratoriosDto", new ConverterEntitiesToDtos().fromLaboratorios(service.findAll()));

        modelAndView.setViewName("laboratorio/index");

        return modelAndView;
    }

    @GetMapping("/cadastro-laboratorio")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView create(ModelAndView modelAndView) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        modelAndView.addObject("dto", new LaboratorioDto());
        modelAndView.addObject("edificios", Edificio.values());

        modelAndView.setViewName("laboratorio/create");

        return modelAndView;
    }

    @Transactional
    @PostMapping("/cadastro-laboratorio")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView save(@Valid @ModelAttribute("dto") LaboratorioDto dto, BindingResult result,
                             RedirectAttributes attributes, ModelAndView modelAndView) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        if (hasErrors(result, modelAndView)) {
            attributes.addFlashAttribute("success", false);

            modelAndView.addObject("dto", dto);
            modelAndView.addObject("edificios", Edificio.values());

            modelAndView.setViewName("laboratorio/create");

            return modelAndView;
        }

        service.cadastrar(dto.toEntity());

        modelAndView.setViewName("redirect:/laboratorio/cadastro-laboratorio");
        attributes.addFlashAttribute("msg", Message.MSG_05.getMessage());

        return modelAndView;
    }

    @GetMapping("/editar-laboratorio/{idLaboratorio}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView edit(@PathVariable("idLaboratorio") Optional<String> record, ModelAndView modelAndView) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        var actionName = "redirect:/laboratorio";

        if (!hasRecord(record, laboratorioRepository, modelAndView, actionName)) {
            return modelAndView;
        }

        var lab = laboratorioRepository.findById(record.get()).get();
        var dto = new LaboratorioDto(lab);

        modelAndView.addObject("dto", dto);
        modelAndView.addObject("edificios", Edificio.values());

        modelAndView.setViewName("laboratorio/edit");

        return modelAndView;
    }

    @Transactional
    @PostMapping("/editar-laboratorio")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView edit(@Valid @ModelAttribute("dto") LaboratorioDto dto, BindingResult result,
                             ModelAndView modelAndView, RedirectAttributes attributes) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        if (hasErrors(result, attributes)) {
            modelAndView.addObject("dto", dto);
            modelAndView.setViewName("redirect:/laboratorio/editar-laboratorio/" + dto.getId());

            return modelAndView;
        }

        service.update(dto.toEntity());

        modelAndView.setViewName("redirect:/laboratorio");
        attributes.addFlashAttribute("success", true);

        return modelAndView;
    }

    @PostMapping("/filtros")
    public ModelAndView findBy(@RequestParam(name = "localizacao", required = false) String localizacao,
                               @RequestParam(name = "edificio", required = false) Edificio edificio,
                               @RequestParam(name = "andar", required = false) Integer andar,
                               ModelAndView modelAndView) {

        var laboratorios = service.find(localizacao, edificio, andar, em);

        modelAndView.addObject("dto", new LaboratorioDto());
        modelAndView.addObject("laboratoriosDto", new ConverterEntitiesToDtos()
                .fromLaboratorios(laboratorios));

        modelAndView.setViewName("laboratorio/index");

        return modelAndView;
    }
}