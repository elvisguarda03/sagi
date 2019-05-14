package br.com.ternarius.inventario.sagi.infrastructure.repository;

import br.com.ternarius.inventario.sagi.domain.entity.SolicitacaoEquipamento;
import br.com.ternarius.inventario.sagi.domain.repository.SolicitacaoEquipamentoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoEquipamentoJpaRepository
        extends SolicitacaoEquipamentoRepository, JpaRepository<SolicitacaoEquipamento, String> {
}
