package br.com.ternarius.inventario.sagi.application.dto;

import java.util.List;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratorioDto {
	
	@Nullable
	private String id;
	
	@NotBlank(message = "A localização é obrigatória.")
	@Pattern(regexp = "/[a-zA-Z\\\\u00C0-\\\\u00FF ]+/i")
	private String localizacao;
	
	@NotBlank(message = "O campo edifício é obrigatório.")
	private String edificio;
	
	@NotNull(message = "O campo andar é obrigatório.")
	@Range(min = 0, max = 3, message = "Os edificíos só possuem 3 andares.")
	private Integer andar;
	
	@OneToMany
	@Nullable
	private List<Equipamento> equipamentos;

	public LaboratorioDto(Laboratorio lab) {
		if (lab != null) {
			this.id = lab.getId();
			this.localizacao = lab.getLocalizacao();
			this.edificio = lab.getEdificio();
			this.andar = lab.getAndar();
			this.equipamentos = lab.getEquipamentos();
		}
	}
	
	public Laboratorio toEntity() {
		return Laboratorio.builder()
					.id(id)
					.localizacao(localizacao)
					.edificio(edificio)
					.andar(andar)
					.equipamentos(equipamentos)
					.build();
	}
}