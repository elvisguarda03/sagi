package br.com.ternarius.inventario.sagi.application.controller.rest;

import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.service.LaboratorioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.findAll;

@RequiredArgsConstructor
@RequestMapping("api/laboratorios")
@RestController
public class LaboratorioRestController {

    private final LaboratorioService laboratorioService;

    public ResponseEntity<?> listAll(Pageable pageable) {
        Page<Laboratorio> pages = laboratorioService.findAll(pageable);

        if (pages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(pages);
    }
}