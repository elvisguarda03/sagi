package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.domain.enums.TipoUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.domain.service.UsuarioService;
import br.com.ternarius.inventario.sagi.util.ConverterEntitiesToDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/usuario")
@Controller
public class UsuariosController extends BaseController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @GetMapping
    public ModelAndView index(@PageableDefault(size = 8) Pageable pageable, ModelAndView modelAndView) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        var pages = usuarioService.findByIsDelete(pageable);

        modelAndView.addObject("usuarios", new ConverterEntitiesToDtos().fromUsuarios(pages.getContent()));
        modelAndView.addObject("paged", null);
        modelAndView.addObject("tipos", TipoUsuario.values());

        modelAndView.setViewName("usuario/index");

        return modelAndView;
    }

    @Transactional
    @GetMapping("/editar-tipo/{idUsuario}/{tipoUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editType(@PathVariable("idUsuario") Optional<String> record,
                                 @PathVariable("tipoUsuario") Optional<TipoUsuario> tipoUsuario,
                                 ModelAndView modelAndView, RedirectAttributes attributes) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        var actionName = "usuario/index";

        if (!hasRecord(record, usuarioRepository, modelAndView, actionName)) {
            return modelAndView;
        }

        var user = usuarioRepository.findById(record.get()).get();

        tipoUsuario.ifPresent(t -> {
            user.setTipoUsuario(t);
            usuarioRepository.save(user);

            attributes.addFlashAttribute("", null);
            attributes.addFlashAttribute("", null);

            modelAndView.setViewName("redirect:/usuario");
        });

        return modelAndView;
    }

    @GetMapping("/excluir-usuario/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView delete(@PathVariable("idUsuario") Optional<String> record, ModelAndView modelAndView, RedirectAttributes attributes) {
        if (!IsLogged()) {
            redirectToLogin(modelAndView);
            return modelAndView;
        }

        var actionName = "redirect:/usuario";

        if (!hasRecord(record, usuarioRepository, modelAndView, actionName)) {
            return modelAndView;
        }

        usuarioService.unavailable(record.get());

        modelAndView.setViewName("redirect:/usuario");

        attributes.addFlashAttribute("", "");
        attributes.addFlashAttribute("", "");

        return modelAndView;
    }
}