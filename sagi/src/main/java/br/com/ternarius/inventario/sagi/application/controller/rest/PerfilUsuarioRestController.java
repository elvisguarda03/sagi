package br.com.ternarius.inventario.sagi.application.controller.rest;

import br.com.ternarius.inventario.sagi.application.controller.BaseController;
import br.com.ternarius.inventario.sagi.application.dto.AlteraSenhaDto;
import br.com.ternarius.inventario.sagi.domain.enums.Message;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import br.com.ternarius.inventario.sagi.domain.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;

@RequiredArgsConstructor
@RequestMapping("/api/perfil-usuario")
@RestController
public class PerfilUsuarioRestController extends BaseController {
    private final LoginService loginService;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<?> updatePassword(@Valid @RequestBody AlteraSenhaDto dto) {
        final var notification = loginService.changePassword(dto.getId(),
                dto.getSenhaAtual(), dto.getNovaSenha());

        if (notification.fail()) {
            var errors = new HashMap<String, String>();
            errors.put("senhaAtual", notification.getFirstError());
            errors.put("error", String.valueOf(true));

            return ResponseEntity.badRequest().body(errors);
        }

        var success = new HashMap<String, String>();
        success.put("msg", Message.MSG_18.getMessage());
        success.put("success", String.valueOf(true));

        refresh(usuarioRepository.findById(dto.getId())
                .get());

        return ResponseEntity.ok(success);
    }
}