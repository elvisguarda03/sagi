package br.com.ternarius.inventario.sagi.application.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationMessage {
	private String field;
	private String message;
	private int status;
}
