package br.com.ternarius.inventario.sagi.infrastructure.repository;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.enums.TipoUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Repository
public interface UsuarioJpaRepository 
		extends UsuarioRepository, JpaRepository<Usuario, String>{

	@Modifying
	@Query("FROM Usuario u WHERE NOT u.tipoUsuario = :pTipo")
	List<Usuario> findByTipoUsuario(@Param("pTipo") TipoUsuario tipo);

	@Modifying
	@Query("UPDATE Usuario u SET u.status = :status WHERE u.id = :id")
	void updateStatus(@Param("id") String id, @Param("status") StatusUsuario status);

	@Modifying
	@Query("UPDATE Usuario u SET u.senha = :pPassword WHERE u.id = :pId")
	void updatePassword(@Param("pId") String id, @Param("pPassword") String password);
}