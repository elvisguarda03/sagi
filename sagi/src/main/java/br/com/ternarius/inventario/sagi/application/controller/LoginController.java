package br.com.ternarius.inventario.sagi.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Elvis da Guarda
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    @GetMapping
    public String login(@RequestParam(name = "error", required = false) Boolean error,
                        @RequestParam(name = "resetada", required = false) Boolean resetada, HttpServletResponse response, Model model) {
        if (IsLogged()) {
            return redirectToDashboard();
        }

        model.addAttribute("loginError", error);
        setAttributes(resetada, response, model);

        return "login/index";
    }

    private void setAttributes(Boolean resetada, HttpServletResponse response, Model model) {
        if (is3xxRedirection(response)) {
            model.addAttribute("resetada", resetada);
        }
    }
}