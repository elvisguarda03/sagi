package br.com.ternarius.inventario.sagi.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.repository.EquipamentoRepository;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Repository
public interface EquipamentoJpaRepository 
		extends EquipamentoRepository, JpaRepository<Equipamento, String> {
	
	@Modifying
	@Query("select e from Equipamento e where e.nomeEquipamento = :nome")
	Optional<Equipamento> findByNomeEquipamento(@Param("nome") String nomeEquipamento);

	@Modifying
	@Query("select e from Equipamento e inner join e.laboratorio l where l = :lab group by l.localizacao")
	List<Equipamento> findAll(@Param("lab") Laboratorio laboratorio);
}