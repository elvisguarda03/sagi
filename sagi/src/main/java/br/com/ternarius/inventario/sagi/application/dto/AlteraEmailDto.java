package br.com.ternarius.inventario.sagi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlteraEmailDto {
    @Nullable
    private String id;

    @NotBlank(message = "Digite o E-mail.")
    @Email(message = "E-mail inválido.")
    private String email;
}
