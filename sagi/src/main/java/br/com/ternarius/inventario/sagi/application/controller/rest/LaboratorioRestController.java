package br.com.ternarius.inventario.sagi.application.controller.rest;

import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.service.LaboratorioService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RequestMapping("api/laboratorios")
@RestController
public class LaboratorioRestController {

    private final LaboratorioService laboratorioService;

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> listAll(@PageableDefault Pageable pageable) {
        Page<Laboratorio> pages = laboratorioService.findAll(pageable);

        if (pages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(pages);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody Laboratorio laboratorio) {
        return new ResponseEntity<>(laboratorioService.cadastrar(laboratorio), HttpStatus.CREATED);
    }

    @GetMapping("/{localizacao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> findByLocalizacao(@NotNull(message = "Forneça a Localização.") @PathVariable("localizacao") String localizacao) {
        if (!laboratorioService.existsByLocalizacaoContainingIgnoreCase(localizacao)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(laboratorioService.findByLocalizacao(localizacao));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@Valid @RequestBody Laboratorio laboratorio) {
        return ResponseEntity.ok().body(laboratorioService.update(laboratorio));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@NotNull @PathVariable("idLaboratorio") String id) {
        if (!laboratorioService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        laboratorioService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}