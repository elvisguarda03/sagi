package br.com.ternarius.inventario.sagi.domain.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Laboratorio {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	
	@Column(name = "nome_lab", nullable = false)
	@NotBlank(message = "A localização é obrigatória.")
	private String localizacao;
	
	@Column(nullable = false)
	@NotBlank(message = "O campo edifício é obrigatório.")
	private String edificio;
	
	@Column(nullable = false)
	@NotNull(message = "O campo andar é obrigatório.")
	private Integer andar;
	
	@OneToMany(mappedBy = "laboratorio")
	@Nullable
	private List<Equipamento> equipamentos;
}