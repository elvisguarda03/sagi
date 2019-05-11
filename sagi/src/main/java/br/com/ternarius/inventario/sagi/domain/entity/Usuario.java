package br.com.ternarius.inventario.sagi.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Usuario {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private TipoUsuario tipo;
	
	@Column(nullable = false)
	
	@Enumerated(EnumType.ORDINAL)
	private StatusUsuario status;

	@NotBlank(message = "Digite o email")
	@Column(nullable = false, unique = true, length = 100)
	@Email(message = "E-mail inválido")
	private String email;
	
	@Column(nullable = false, length = 100)
    @NotBlank(message = "O campo nome é obrigatório")
	private String nome;
	
	@Column(nullable = false, length = 100)
	@NotBlank(message = "O campo senha é obrigatório")
	@JsonIgnore
	private String senha;

	@CreatedDate
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
	
	@Column(name = "already_logged_in", nullable = false)
	private Boolean alreadyLoggedIn;
	
	@LastModifiedDate
	@Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao;
	
	public void criptografarSenha() {
		this.senha = BCrypt.hashpw(senha, BCrypt.gensalt());
	}
}