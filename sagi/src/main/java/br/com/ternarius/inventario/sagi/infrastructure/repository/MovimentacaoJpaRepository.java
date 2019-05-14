package br.com.ternarius.inventario.sagi.infrastructure.repository;

import br.com.ternarius.inventario.sagi.domain.entity.Movimentacao;
import br.com.ternarius.inventario.sagi.domain.repository.MovimentacaoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoJpaRepository
        extends MovimentacaoRepository, JpaRepository<Movimentacao, String> {
}