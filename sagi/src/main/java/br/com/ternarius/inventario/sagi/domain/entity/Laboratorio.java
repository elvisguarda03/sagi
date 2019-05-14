package br.com.ternarius.inventario.sagi.domain.entity;

import br.com.ternarius.inventario.sagi.domain.enums.Edificio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Laboratorio {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator")
	private String id;
	
	@Column(name = "nome_lab", nullable = false, unique = true)
	@NotBlank(message = "O campo Localização é obrigatório.")
	@Pattern(regexp = "[^0-9]*", message = "Não é possível inserir números no campo Localização.")
	@EqualsAndHashCode.Include
	 private String localizacao;
	
	@Column(nullable = false)
	@Enumerated
	@NotNull(message = "O campo Edifício é obrigatório.")
	private Edificio edificio;
	
	@Column(nullable = false)
	@Range(min = 0, max = 3, message = "O número do Andar é inválido.")
	@NotNull(message = "O campo Andar é obrigatório.")
	private Integer andar;
	
	@OneToMany(mappedBy = "laboratorio")
	@Nullable
	@JsonManagedReference
	private List<Equipamento> equipamentos;
}