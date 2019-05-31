package br.com.ternarius.inventario.sagi.application.dto;

import javax.validation.constraints.Size;

import br.com.ternarius.inventario.sagi.infrastructure.security.PasswordEqual;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Elvis da Guarda
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordEqual
public class NovaSenhaDto {

	private String token;

	@Size(min = 4, max = 6, message = "Deve conter entre 4 a 6 caracteres")
	private String senha;

	private String confirmacaoSenha;
}
