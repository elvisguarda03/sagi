package br.com.ternarius.inventario.sagi.application.controller.rest;

import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') OR hasRole('MOD')")
@RequestMapping("/api/equipamentos")
@RestController
public class EquipamentoRestController {

    private final EquipamentoService equipamentoService;

    @PersistenceContext
    private EntityManager em;

    @GetMapping
    public ResponseEntity<?> listAll(Pageable pageable) {
        Page<Equipamento> pages = equipamentoService.findAll(pageable);

        if (pages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pages);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Equipamento equipamento) {
        return new ResponseEntity<>(equipamentoService.cadastrar(equipamento), HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Equipamento equipamento) {
        return ResponseEntity.ok().body(equipamentoService.update(equipamento));
    }

    @GetMapping("/filtros")
    public ResponseEntity<?> find(@RequestParam(value = "nome", required = false) String nome,
                                  @RequestParam(value = "localizacao", required = false) String localizacao,
                                  @RequestParam(value = "codigoPatrimonio", required = false) Long codigoPatrimonio,
                                  @RequestParam(value = "valor", required = false) BigDecimal valor) {
        var equipamentos = equipamentoService.find(nome, localizacao, codigoPatrimonio, valor, em);

        if (equipamentos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(equipamentos);
    }

    @Transactional
    @DeleteMapping("/{idEquipamento}")
    public ResponseEntity<?> delete(@NotNull(message = "É necessário informar o Id para que esta requisição seja efetuada com sucesso.") @PathVariable("idEquipamento")
                                                String idEquipamento) {
        if (!equipamentoService.existsById(idEquipamento)) {
            return ResponseEntity.notFound().build();
        }

        equipamentoService.unavailable(idEquipamento);

        return ResponseEntity.noContent().build();
    }
}