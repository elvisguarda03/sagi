package br.com.ternarius.inventario.sagi.domain.repository;

import java.util.Optional;

import br.com.ternarius.inventario.sagi.domain.entity.UsuarioToken;

/**
 * 
 * @author Elvis da Guarda
 *
 */
public interface UsuarioTokenRepository {
    Optional<UsuarioToken> findByToken(String token);
    UsuarioToken save(UsuarioToken entity);
    void delete(UsuarioToken entity);
}
