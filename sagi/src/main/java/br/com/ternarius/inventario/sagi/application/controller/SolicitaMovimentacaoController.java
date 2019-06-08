package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.application.dto.EquipamentoDto;
import br.com.ternarius.inventario.sagi.application.dto.SolicitacaoEquipamentoDto;
import br.com.ternarius.inventario.sagi.domain.entity.Movimentacao;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.repository.SolicitacaoEquipamentoRepository;
import br.com.ternarius.inventario.sagi.domain.service.EquipamentoService;
import br.com.ternarius.inventario.sagi.domain.service.LaboratorioService;
import br.com.ternarius.inventario.sagi.domain.service.MovimentacaoService;
import br.com.ternarius.inventario.sagi.domain.service.SolicitacaoEquipamentoService;
import br.com.ternarius.inventario.sagi.util.ConverterEntitiesToDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Elvis de Sousa
 */

@RequiredArgsConstructor
@RequestMapping("/equipamento/solicita-emprestimo")
@Controller
public class SolicitaMovimentacaoController extends BaseController {

    private final MovimentacaoService movimentacaoService;
    private final EquipamentoService equipamentoService;
    private final LaboratorioService laboratorioService;
    private final SolicitacaoEquipamentoService solicitacaoEquipamentoService;
    private final SolicitacaoEquipamentoRepository solicitacaoEquipamentoRepository;

    @GetMapping
    public ModelAndView index(@PageableDefault Pageable pageable, ModelAndView modelAndView) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        modelAndView.setViewName("solicitacao/index");

        modelAndView.addObject("dtoMovimentacoes", new ConverterEntitiesToDtos()
                .fromSolicitacoes(solicitacaoEquipamentoService.fimdAll()));

        modelAndView.addObject("dtoLaboratorios", new ConverterEntitiesToDtos()
                .fromLaboratorios(laboratorioService.findByStatus(false)));

        modelAndView.addObject("dtoEquipamentos", new ConverterEntitiesToDtos()
                .fromEquipamentos(equipamentoService.findByIsDelete(false, pageable).getContent()));

        return modelAndView;
    }

    @GetMapping("/Ajax")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MOD') OR hasRole('USER')")
    public @ResponseBody List<EquipamentoDto> filters(@RequestParam(value = "nomeEquipamento") Optional<String> record,
                                                      @PageableDefault Pageable pageable) {
        if (!record.isPresent()) {
            return new ConverterEntitiesToDtos()
                    .fromEquipamentos(equipamentoService
                            .findByIsDelete(false, pageable).getContent());
        }

        return new ConverterEntitiesToDtos().fromEquipamentos(equipamentoService
                .findByNomeEquipamento(record.get()));
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('MOD')")
    @Transactional
    @PostMapping
    public ModelAndView create(@Valid @ModelAttribute("movimentacao") SolicitacaoEquipamentoDto dto,
                               BindingResult result, ModelAndView modelAndView, RedirectAttributes attributes) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        var actionName = "redirect:/equipamento/solicita-emprestimo";

        if (hasErrors(result, modelAndView)) {
            modelAndView.setViewName(actionName);
            return modelAndView;
        }

        if (!hasRecords(dto.getLaboratorioId(), dto.getEquipamentosId(), attributes, actionName)) {
            return modelAndView;
        }

        solicitacaoEquipamentoService.solicitar(dto.toEntity(), dto.getEquipamentosId(), dto.getLaboratorioId());

        modelAndView.setViewName(actionName);

        attributes.addFlashAttribute("msg", Message.MSG_22.getMessage());
        attributes.addFlashAttribute("success", true);

        return modelAndView;
    }

    @Transactional
    @GetMapping("/permite/{idSolicitacao}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MOD')")
    public ModelAndView accept(@PathVariable Optional<String> record, ModelAndView modelAndView, RedirectAttributes attributes) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        var actionName = "redirect:/equipamento/solicita-emprestimo";

        if (!hasRecord(record, solicitacaoEquipamentoRepository, modelAndView, actionName)) {
            attributes.addFlashAttribute("msgError", Message.MSG_25.getMessage());
            attributes.addFlashAttribute("error", true);

            return modelAndView;
        }

        var solicitacao = solicitacaoEquipamentoService.findById(record.get()).get();

        final var notification = solicitacaoEquipamentoService.validaAprovacaoMovimentacao(solicitacao.getComodante().getTipoUsuario(),
                solicitacao.getEquipamento());

        if (notification.fail()) {
            attributes.addFlashAttribute("msg", notification.getFirstError());
            attributes.addFlashAttribute("errorAuthority", true);

            return modelAndView;
        }

        var movimentacao = Movimentacao.builder()
                .equipamento(solicitacao.getEquipamento())
                .solicitante(solicitacao.getComodatario())
                .localizacaoAnterior(solicitacao.getEquipamento().getLaboratorio())
                .localizacaoAtual(solicitacao.getNovaLocalizacao())
                .build();
        movimentacaoService.cadastrar(movimentacao);

        modelAndView.setViewName(actionName);

        attributes.addFlashAttribute("successSoli", true);
        attributes.addFlashAttribute("", "");

        return modelAndView;
    }

    @GetMapping("/negado/{idSolicitacao}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MOD')")
    public ModelAndView reject(@PathVariable Optional<String> record, ModelAndView modelAndView, RedirectAttributes attributes) {
        if (!IsLogged()) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        var actionName = "redirect:/equipamento/solicita-emprestimo";

        if (!hasRecord(record, solicitacaoEquipamentoRepository, modelAndView, actionName)) {
            attributes.addFlashAttribute("msgError", Message.MSG_25.getMessage());
            attributes.addFlashAttribute("errorReject", true);

            return modelAndView;
        }

        solicitacaoEquipamentoService.deleteById(record.get());

        modelAndView.setViewName(actionName);
        attributes.addFlashAttribute("msgSuccess", null);

        return modelAndView;
    }

    private Boolean hasRecords(String idLaboratorio, List<String> idEquipamentos, RedirectAttributes attributes, String actionName) {
        if (!laboratorioService.existsById(idLaboratorio)) {
            attributes.addFlashAttribute("msgError", Message.MSG_23.getMessage());
            attributes.addFlashAttribute("erroLab", true);

            return false;
        }

        for (String id : idEquipamentos) {

            if (!equipamentoService.existsById(id)) {
                attributes.addFlashAttribute("msgError", Message.MSG_26.getMessage());
                attributes.addFlashAttribute("erroEq", true);

                return false;
            }
        }

        return true;
    }
}