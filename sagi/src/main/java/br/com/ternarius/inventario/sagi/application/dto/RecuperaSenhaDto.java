package br.com.ternarius.inventario.sagi.application.dto;

import javax.validation.constraints.NotBlank;

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
public class RecuperaSenhaDto {
	
	@NotBlank(message = "O campo email é obrigatório!")
	private String email;
}
