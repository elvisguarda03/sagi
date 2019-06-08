package br.com.ternarius.inventario.sagi.application.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/solicita-emprestimo")
@RestController
public class SolicitaMovimentaoRestController {

    @GetMapping
    public ResponseEntity<?> findByNomeEquipamento(@RequestBody String nomeEquipamento) {
        return ResponseEntity.ok(null);
    }
}
