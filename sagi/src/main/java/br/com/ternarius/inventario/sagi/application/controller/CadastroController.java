package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.application.dto.CadastroDto;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.service.CadastroService;
import br.com.ternarius.inventario.sagi.domain.valueobject.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author Elvis da Guarda
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/cadastro-usuario")
public class CadastroController extends BaseController {

    private final CadastroService service;

    @GetMapping
    public ModelAndView index(ModelAndView modelAndView) {
        if (IsLogged()) {
            modelAndView.setViewName("redirect:/dashboard");
            return modelAndView;
        }


        modelAndView.setViewName("login/cadastro-usuario");
        modelAndView.addObject("dto", new CadastroDto());

        return modelAndView;
    }

    @PostMapping
    public ModelAndView create(@Valid @ModelAttribute("dto") CadastroDto dto, BindingResult result,
                               RedirectAttributes attributes, ModelAndView modelAndView) {
        if (hasErrors(result, modelAndView)) {
            modelAndView.setViewName("login/cadastro-usuario");
            modelAndView.addObject("dto", dto);

            return modelAndView;
        }

        final Notification notification = service.cadastrar(dto.toEntity());

        if (notification.fail()) {
            modelAndView.setViewName("login/cadastro-usuario");

            modelAndView.addObject("error", true);
            modelAndView.addObject("msg", notification.getFirstError());
            modelAndView.addObject("dto", dto);

            return modelAndView;
        }

        attributes.addFlashAttribute("success", true);
        attributes.addFlashAttribute("msg", Message.MSG_14.getMessage());

        modelAndView.setViewName("redirect:/cadastro-usuario");

        return modelAndView;
    }
}