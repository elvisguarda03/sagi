package br.com.ternarius.inventario.sagi.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
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
		extends EquipamentoRepository, PagingAndSortingRepository<Equipamento, String> {
	
	@Modifying
	@Query("UPDATE Equipamento e SET e.isMaintenance = :pIsMaintenance WHERE e.id = :pId")
	Equipamento updateIsMaintenance(@Param("pId") String id, @Param("pIsMaintenance") Boolean isMaintenance);

	@Modifying
	@Query("FROM Equipamento e INNER JOIN FETCH e.laboratorio l WHERE l = :pLab GROUP BY l.localizacao")
	List<Equipamento> findAll(@Param("pLab") Laboratorio laboratorio);

	@Modifying
	@Query("FROM Equipamento e WHERE e.isDelete = :pIsDelete")
	List<Equipamento> findAll(@Param("pIsDelete") Boolean isDelete);
}