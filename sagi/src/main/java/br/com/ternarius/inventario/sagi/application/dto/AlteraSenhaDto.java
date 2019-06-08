package br.com.ternarius.inventario.sagi.application.dto;

import br.com.ternarius.inventario.sagi.infrastructure.security.PasswordEqual;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordEqual(first = "novaSenha")
public class AlteraSenhaDto {
    @Nullable
    private String id;

    @NotBlank(message = "Por favor, verifique o campo Senha Atual.")
    private String senhaAtual;

    @Size(min = 4, max = 6, message = "Deve conter entre 4 a 6 caracteres.")
    @NotBlank(message = "Por favor, verifique o campo Nova Senha.")
    private String novaSenha;

    private String confirmacaoSenha;
}
