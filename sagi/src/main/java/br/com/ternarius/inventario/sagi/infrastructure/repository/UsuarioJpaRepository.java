package br.com.ternarius.inventario.sagi.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Repository
public interface UsuarioJpaRepository 
		extends UsuarioRepository, JpaRepository<Usuario, String>{
	
	@Modifying
	@Query("UPDATE Usuario u SET u.status = :status WHERE u.id = :id")
	void updateStatus(@Param("id") String id, @Param("status") StatusUsuario status);
	
	@Modifying
	@Query("UPDATE Usuario u SET u.senha = :password WHERE u.id = :id")
	void updatePassword(@Param("id") String id, @Param("password") String password);
}