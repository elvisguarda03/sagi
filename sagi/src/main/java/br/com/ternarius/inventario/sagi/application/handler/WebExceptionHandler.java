package br.com.ternarius.inventario.sagi.application.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "br.com.ternarius.inventario.sagi.application.controller")
public class WebExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handlerAccessDeniedException(AccessDeniedException exception) {
        var modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/acesso-negado");
        modelAndView.addObject("erro", exception.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handlerMethodArgumentNotValidException() {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
}
