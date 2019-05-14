package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.domain.entity.Movimentacao;
import br.com.ternarius.inventario.sagi.domain.entity.SolicitacaoEquipamento;
import br.com.ternarius.inventario.sagi.domain.repository.SolicitacaoEquipamentoRepository;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.domain.service.MovimentacaoService;
import br.com.ternarius.inventario.sagi.domain.service.SolicitacaoEquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

/**
 *
 * @author Elvis de Sousa
 */

@RequiredArgsConstructor
@RequestMapping("/equipamento/solicita-emprestimo")
@Controller
public class SolicitaMovimentacaoController extends BaseController {

    private final MovimentacaoService movimentacaoService;
    private final UsuarioRepository repository;
    private final SolicitacaoEquipamentoService solicitacaoEquipamentoService;
    private final SolicitacaoEquipamentoRepository solicitacaoEquipamentoRepository;

    @GetMapping
    public ModelAndView index(ModelAndView modelAndView, @AuthenticationPrincipal UserDetails userDetails) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        userLogged(modelAndView, userDetails, repository);

        modelAndView.setViewName("movimentacao/index");
        modelAndView.addObject("movimentacaoDtos", solicitacaoEquipamentoService.fimdAll());

        return modelAndView;
    }

    @GetMapping("/cadastra-solicitacao/{idEquipamento}")
    public ModelAndView create(@PathVariable("idEquipamento") Optional<String> record, ModelAndView modelAndView, @AuthenticationPrincipal UserDetails userDetails) {
        return modelAndView;
    }

    @Transactional(rollbackFor = Throwable.class)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ModelAndView create(@Valid @ModelAttribute("movimentacao") SolicitacaoEquipamento solicitacaoEquipamento,
                               BindingResult result, ModelAndView modelAndView, RedirectAttributes attributes) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        if (hasErrors(result, modelAndView)) {
            modelAndView.setViewName("movimentacao/cadastro-movimentacao");
            return modelAndView;
        }

        solicitacaoEquipamentoService.cadastrar(solicitacaoEquipamento);

        modelAndView.setViewName("redirect:/equipamento/solicita-emprestimo/cadastra-solicitacao");
        attributes.addFlashAttribute("", null);

        return modelAndView;
    }

    @Transactional(rollbackFor = Throwable.class)
    @GetMapping("/permite/{idSolicitacao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MOD')")
    public ModelAndView accept(@PathVariable Optional<String> record, ModelAndView modelAndView, RedirectAttributes attributes) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        var actionName = "movimentacao/index";

        if (!hasRecord(record, solicitacaoEquipamentoRepository, modelAndView, actionName)) {
            return modelAndView;
        }

        var solicitacao = solicitacaoEquipamentoService.findById(record.get()).get();

        var movimentacao = Movimentacao.builder()
                .equipamento(solicitacao.getEquipamento())
                .solicitante(solicitacao.getComodatario())
                .localizacaoAnterior(solicitacao.getEquipamento().getLaboratorio())
                .localizacaoAtual(solicitacao.getNovaLocalizacao())
                .build();
        movimentacaoService.cadastrar(movimentacao);

        modelAndView.setViewName("redirect:/equipamento/solicita-emprestimo");
        attributes.addFlashAttribute("", null);

        return modelAndView;
    }

    @GetMapping("/negado/{idSolicitacao}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView reject(@PathVariable Optional<String> record, ModelAndView modelAndView, RedirectAttributes attributes) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        var actionName = "movimentacao/index";

        if (hasRecord(record, solicitacaoEquipamentoRepository, modelAndView, actionName)) {
            return modelAndView;
        }

        solicitacaoEquipamentoService.deleteById(record.get());

        modelAndView.setViewName("redirect:/equipamento//solicita-emprestimo");
        attributes.addFlashAttribute("", null);

        return modelAndView;
    }
}
