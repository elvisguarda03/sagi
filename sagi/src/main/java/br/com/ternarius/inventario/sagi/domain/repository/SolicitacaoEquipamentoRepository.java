package br.com.ternarius.inventario.sagi.domain.repository;

import br.com.ternarius.inventario.sagi.domain.entity.SolicitacaoEquipamento;

import java.util.List;
import java.util.Optional;

public interface SolicitacaoEquipamentoRepository extends Repository {
    SolicitacaoEquipamento save(SolicitacaoEquipamento solicitacaoEquipamento);
    Optional<SolicitacaoEquipamento> findById(String id);
    List<SolicitacaoEquipamento> findAll();
    void deleteById(String id);
}