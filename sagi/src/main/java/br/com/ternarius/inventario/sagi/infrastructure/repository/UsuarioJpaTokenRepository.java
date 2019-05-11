package br.com.ternarius.inventario.sagi.infrastructure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.ternarius.inventario.sagi.domain.entity.UsuarioToken;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioTokenRepository;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Repository
public interface UsuarioJpaTokenRepository 
		extends UsuarioTokenRepository, CrudRepository<UsuarioToken, Long>{
}
