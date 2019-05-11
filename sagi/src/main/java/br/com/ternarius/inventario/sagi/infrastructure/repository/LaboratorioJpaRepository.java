package br.com.ternarius.inventario.sagi.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.repository.LaboratorioRepository;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Repository
public interface LaboratorioJpaRepository 
		extends LaboratorioRepository, JpaRepository<Laboratorio, String> {
	
	@Modifying
	@Query("update Laboratorio l set l.localizacao = :localizacao, l.edificio = :edificio, l.andar = :andar where l.id = :id")
	void updateLocalizacaoAndEdificioAndAndar(@Param("id") String id, @Param("localizacao") String localizacao, @Param("edificio") String edificio, @Param("andar") Integer andar);
}