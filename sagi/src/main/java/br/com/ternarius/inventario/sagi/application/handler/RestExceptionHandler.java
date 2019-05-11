package br.com.ternarius.inventario.sagi.application.handler;

import br.com.ternarius.inventario.sagi.application.error.ErrorDetails;
import br.com.ternarius.inventario.sagi.application.error.ValidationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(basePackages = "br.com.ternarius.inventario.sagi.application.controller.rest")
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException manvException, Map<String, String> errors) {
        var fieldErrors = manvException.getBindingResult().getFieldErrors();

        fieldErrors.forEach(f -> {
            var field = f.getField();
            var fieldMessage = f.getDefaultMessage();
            errors.put(field, fieldMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handlerAccessDeniedException(AccessDeniedException exception) {
        var error = ErrorDetails.Builder.newBuilder()
                .title("O recurso não pode ser acessado!")
                .message(exception.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(new Date().getTime())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handlerConstraintViolationException(BindingResult result) {
        var errors = new ArrayList<ValidationMessage>();
        result.getFieldErrors().forEach(f -> errors.add(
                ValidationMessage.builder()
                        .field(f.getField())
                        .message(f.getDefaultMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build()
        ));

        return ResponseEntity.badRequest().body(errors);
    }

}