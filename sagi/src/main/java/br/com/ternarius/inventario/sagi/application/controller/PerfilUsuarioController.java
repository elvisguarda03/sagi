package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.application.dto.AlteraEmailDto;
import br.com.ternarius.inventario.sagi.application.dto.AlteraSenhaDto;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.domain.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@RequestMapping("/perfil-usuario")
@Controller
public class PerfilUsuarioController extends BaseController {

    private final LoginService loginService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ModelAndView index(ModelAndView modelAndView, @AuthenticationPrincipal UserDetails userDetails) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        var user = usuarioRepository.findByEmail(userDetails.getUsername()).get();

        modelAndView.addObject("dto", AlteraSenhaDto.builder().id(user.getId()).build());
        modelAndView.setViewName("perfil/index");

        return modelAndView;
    }

    @Transactional
    @PostMapping("/alterar-email")
    public ModelAndView updateEmail(@ModelAttribute("dto") AlteraEmailDto dto, BindingResult result, RedirectAttributes attributes, ModelAndView modelAndView) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        if (hasErrors(result, modelAndView)) {
            modelAndView.addObject("dto", dto);
            modelAndView.setViewName("perfil/index");

            return modelAndView;
        }

        final var notification = loginService.changeEmail(dto.getId(), dto.getEmail());

        if (notification.fail()) {
            modelAndView.addObject("msg", notification.getFirstError());
            modelAndView.addObject("error", true);
            modelAndView.addObject("dto", dto);

            modelAndView.setViewName("perfil/index");

            return modelAndView;
        }

        refresh(usuarioRepository.findById(dto.getId())
                .get());

        attributes.addFlashAttribute("msg", Message.MSG_19.getMessage());
        attributes.addFlashAttribute("success", true);

        modelAndView.setViewName("redirect:/perfil-usuario");

        return modelAndView;
    }
}