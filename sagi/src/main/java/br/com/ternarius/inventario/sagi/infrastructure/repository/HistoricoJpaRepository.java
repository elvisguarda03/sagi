package br.com.ternarius.inventario.sagi.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ternarius.inventario.sagi.domain.entity.Historico;
import br.com.ternarius.inventario.sagi.domain.repository.HistoricoRepository;

/**
 * 
 * @author Elvis de Sousa
 *
 */

@Repository
public interface HistoricoJpaRepository 
	extends HistoricoRepository, JpaRepository<Historico, String>{
}
