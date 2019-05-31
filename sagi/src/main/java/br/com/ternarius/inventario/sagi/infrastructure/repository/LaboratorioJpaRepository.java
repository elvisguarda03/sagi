package br.com.ternarius.inventario.sagi.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.repository.LaboratorioRepository;

import java.util.List;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Repository
public interface LaboratorioJpaRepository 
		extends LaboratorioRepository, PagingAndSortingRepository<Laboratorio, String> {
	
	@Modifying
	@Query("UPDATE Laboratorio l SET l.localizacao = :localizacao, l.edificio = :edificio, l.andar = :andar WHERE l.id = :id")
	void updateLocalizacaoAndEdificioAndAndar(@Param("id") String id, @Param("localizacao") String localizacao, @Param("edificio") String edificio, @Param("andar") Integer andar);

	@Modifying
	@Query("FROM Laboratorio l WHERE l.isDelete = :pIsDelete")
	List<Laboratorio> findAll(@Param("pIsDelete") Boolean isDelete);
}