package br.com.ternarius.inventario.sagi.application.controller.rest;

import br.com.ternarius.inventario.sagi.application.controller.BaseController;
import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.service.LaboratorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') OR hasRole('MOD')")
@RequestMapping("/api/laboratorios")
@RestController
public class LaboratorioRestController extends BaseController {

    private final LaboratorioService laboratorioService;

    @GetMapping
    public ResponseEntity<?> listAll() {
        List<Laboratorio> laboratorios = laboratorioService.findAll();

        if (laboratorios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(laboratorios);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Laboratorio laboratorio) {
        return new ResponseEntity<>(laboratorioService.cadastrar(laboratorio), HttpStatus.CREATED);
    }

    @GetMapping("/{localizacao}")
    public ResponseEntity<?> findByLocalizacao(@NotNull(message = "Forneça a Localização.") @PathVariable("localizacao") String localizacao) {
        if (!laboratorioService.existsByLocalizacaoContainingIgnoreCase(localizacao)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(laboratorioService.findByLocalizacao(localizacao));
    }

    @Transactional
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Laboratorio laboratorio) {
        return ResponseEntity.ok().body(laboratorioService.update(laboratorio));
    }

    @Transactional
    @DeleteMapping("/{idLaboratorio}")
    public ResponseEntity<?> delete(@PathVariable("idLaboratorio") String id) {
        if (!laboratorioService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        laboratorioService.unavailable(id);

        var success = new HashMap<String, String>();
        success.put("msg", Message.MSG_15.getMessage());
        success.put("successDelete", String.valueOf(true));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(success);
    }
}