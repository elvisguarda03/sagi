package br.com.ternarius.inventario.sagi.domain.repository;

import java.util.List;
import java.util.Optional;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;

/**
 * 
 * @author Elvis da Guarda
 *
 */
public interface UsuarioRepository extends Repository {
	Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);
    Usuario save(Usuario usuario);
    List<Usuario> findByStatus(StatusUsuario status);
	Optional<Usuario> findById(String id);
    void updatePassword(String id, String password);
	void updateStatus(String id, StatusUsuario status);
    void deleteById(String id);
}
