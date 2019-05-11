package br.com.ternarius.inventario.sagi.application.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {
	private String id;
	private StatusUsuario status;
	private String email;
	private String nome;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime dataCriacao;
	
	public UsuarioDto(Usuario usuario) {
		if (usuario != null) {
			this.id = usuario.getId();
			this.status = usuario.getStatus();
			this.email = usuario.getEmail();
			this.nome = usuario.getNome();
			this.dataCriacao = usuario.getDataCriacao();
		}
	}
	
	public Usuario toEntity() {
		return Usuario.builder()
				.id(id)
				.status(status)
				.email(email)
				.nome(nome)
				.build();
	}
}