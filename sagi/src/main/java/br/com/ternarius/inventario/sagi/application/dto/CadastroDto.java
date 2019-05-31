package br.com.ternarius.inventario.sagi.application.dto;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.enums.TipoUsuario;
import br.com.ternarius.inventario.sagi.infrastructure.security.PasswordEqual;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Elvis da Guarda
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordEqual
public class CadastroDto {

    @Size(min = 5, max = 50, message = "Digite o Nome e o Sobrenome.")
    @NotBlank(message = "Digite o Nome e o Sobrenome")
    private String nome;

    @NotEmpty(message = "Digite o E-mail")
    @Size(max = 100, message = "Limite de 100 caracteres.")
    @Email(message = "E-mail inválido.")
    private String email;

    @Size(min = 4, max = 6, message = "Deve conter entre 4 a 6 caracteres.")
    private String senha;

    private String confirmacaoSenha;

    public CadastroDto(Usuario usuario) {
        if (usuario != null) {
            nome = usuario.getNome();
            email = usuario.getEmail();
        }
    }

    public Usuario toEntity() {
        return Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .status(StatusUsuario.CADASTRO_EM_ABERTO)
                .tipoUsuario(TipoUsuario.USER)
                .build();
    }
}