package br.com.ternarius.inventario.sagi.domain.repository;

import java.util.List;
import java.util.Optional;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.enums.TipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 * @author Elvis da Guarda
 *
 */
public interface UsuarioRepository extends Repository {
	Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);
    Usuario save(Usuario usuario);
    Page<Usuario> findAll(Pageable pageable);
    List<Usuario> findAll();
    List<Usuario> findByStatus(StatusUsuario status);
    List<Usuario> findByTipoUsuarioOrTipoUsuario(TipoUsuario admin, TipoUsuario mod);
	Optional<Usuario> findById(String id);
    void updatePassword(String id, String password);
	void updateStatus(String id, StatusUsuario status);
    void deleteById(String id);
}