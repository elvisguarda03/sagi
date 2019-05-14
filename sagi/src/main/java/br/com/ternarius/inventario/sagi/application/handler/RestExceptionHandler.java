package br.com.ternarius.inventario.sagi.application.handler;

import br.com.ternarius.inventario.sagi.application.error.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;
import java.util.HashMap;

/***
 *
 * @author Elvis de Sousa
 *
 */

@ControllerAdvice(basePackages = "br.com.ternarius.inventario.sagi.application.controller.rest")
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException manvException) {
        var errors = new HashMap<>();
        var fieldErrors = manvException.getBindingResult().getFieldErrors();

        fieldErrors.forEach(f -> {
            var field = f.getField();
            var fieldMessage = f.getDefaultMessage();
            errors.put(field, fieldMessage);
        });
        errors.put("status", "" + HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException hmnre) {
        var errors = new HashMap<String, String>();
        errors.put("error", hmnre.getLocalizedMessage());
        errors.put("status", "" + HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(errors);
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception) {
        var error = ErrorDetails.Builder.newBuilder()
                .title("O recurso não pode ser acessado!")
                .message(exception.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(new Date().getTime())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}