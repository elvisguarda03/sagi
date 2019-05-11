package br.com.ternarius.inventario.sagi.application.controller.rest;

import br.com.ternarius.inventario.sagi.application.controller.BaseController;
import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.service.EquipamentoService;
import br.com.ternarius.inventario.sagi.domain.service.LaboratorioService;
import br.com.ternarius.inventario.sagi.util.ConverterEntitiesToDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RequestMapping("api/equipamentos")
@RestController
public class EquipamentoRestController extends BaseController {

    private final EquipamentoService equipamentoService;

    @GetMapping
    @PreAuthorize(value = "hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> listAll(Pageable pageable) {
        Page<Equipamento> pages = equipamentoService.findAll(pageable);

        if (pages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(pages);
    }

    @Transactional
    @PostMapping("/cadastro-equipamento")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody Equipamento equipamento) {
        return new ResponseEntity<>(equipamentoService.cadastrar(equipamento), HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@Valid @RequestBody Equipamento equipamento, BindingResult result) {

        return null;
    }

    @GetMapping("/{nomeEquipamento}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> findByNome(@NotNull(message = "O nome do equipamento é obrigatório") @PathVariable("nomeEquipamento") String nomeEquipamento, EntityManager em) {
        if (!equipamentoService.existsByNomeEquipamento(nomeEquipamento)) {
            return ResponseEntity.notFound().build();
        }

        var equipamentos = equipamentoService.find(nomeEquipamento, null, null, null, em);

        return ResponseEntity.ok().body(new ConverterEntitiesToDtos().fromEquipamentos(equipamentos));
    }

    @Transactional
    @DeleteMapping("/{idEquipamento}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@NotNull @PathVariable("idEquipamento") String idEquipamento) {
        if (!equipamentoService.existsById(idEquipamento)) {
            return ResponseEntity.notFound().build();
        }

        equipamentoService.deleteById(idEquipamento);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}