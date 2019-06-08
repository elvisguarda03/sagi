package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.domain.repository.MovimentacaoRepository;
import br.com.ternarius.inventario.sagi.domain.service.MovimentacaoService;
import br.com.ternarius.inventario.sagi.util.ConverterEntitiesToDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

/**
 *
 * @author Elvis de Sousa
 *
 */

@RequiredArgsConstructor
@RequestMapping("/equipamento/movimentacao")
@Controller
public class MovimentacaoController extends BaseController {
    @PersistenceContext
    private EntityManager entityManager;
    private final MovimentacaoService movimentacaoService;
    private final MovimentacaoRepository movimentacaoRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView index(ModelAndView modelAndView) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        modelAndView.setViewName("movimentacao/index");

        return modelAndView;
    }

    @GetMapping("/devolve-equipamento/{idMovimentacao}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView giveBack(@PathVariable("idMovimentacao") Optional<String> record,
                             ModelAndView modelAndView, RedirectAttributes attributes) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        var actionName = "movimentacao/index";

        if (!hasRecord(record, movimentacaoRepository, modelAndView, actionName)) {
            return modelAndView;
        }

        movimentacaoService.giveBack(record.get());

        modelAndView.setViewName("redirect:/equipamento/movimentacao");
        attributes.addFlashAttribute("", null);

        return modelAndView;
    }

    @GetMapping("/filtros")
    public ModelAndView filters(@RequestParam(value = "nomeEquipamento", required = false) String nomeEquipamento,
                                @RequestParam(value = "nomeAdmin", required = false) String nomeAdmin,
                                @RequestParam(value = "solicitante", required = false) String solicitante,
                                @RequestParam(value = "localizacaoAtual", required = false) String localizacaoAtual,
                                ModelAndView modelAndView) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        var movimentacoes = movimentacaoService.find(nomeEquipamento, nomeAdmin, solicitante, localizacaoAtual, entityManager);

        modelAndView.addObject("movimentacaoDtos", movimentacoes);
        modelAndView.setViewName("movimentacao/index");

        return modelAndView;
    }
}