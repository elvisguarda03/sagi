package br.com.ternarius.inventario.sagi.domain.service;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public void unavailable(String id) {
        var usuario = repository.findById(id).get();
        usuario.setIsDelete(true);

        repository.save(usuario);
    }

    public Page<Usuario> findByIsDelete(Pageable pageable) {
        return repository.findByIsDeleteAndStatus(false, StatusUsuario.EMAIL_VALIDADO, pageable);
    }
}
