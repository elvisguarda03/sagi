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
	@Query("update Usuario u set u.status = :status where u.id = :id")
	void updateStatus(@Param("id") String id, @Param("status") StatusUsuario status);
	
	@Modifying
	@Query("update Usuario u set u.senha = :password where u.id = :id")
	void updatePassword(@Param("id") String id, @Param("password") String password);
}