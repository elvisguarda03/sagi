package br.com.ternarius.inventario.sagi.domain.repository;

import br.com.ternarius.inventario.sagi.domain.entity.Movimentacao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimentacaoRepository extends Repository {
    Movimentacao save(Movimentacao movimentacao);
    Optional<Movimentacao> findById(String id);
    List<Movimentacao> findAll();
    List<Movimentacao> findByDataPermissao(LocalDate localDate);
    void deleteById(String id);
}
