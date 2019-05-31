package br.com.ternarius.inventario.sagi.application.dto;

import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoDto {
	
	@Nullable
	private String id;
	
	@NotBlank(message = "O nome do equipamento é obrigatório.")
	@Pattern(regexp = "[^0-9]*", message = "Não é possível inserir números no campo Nome Equipamento.")
	private String nomeEquipamento;
	
	@Range(min = 1, message = "O código de patrimônio não pode conter 0 ou valores negativos.")
	@Nullable
	private Long codigoPatrimonio;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Nullable
	private LocalDate dataAquisicao;
	
	@NotNull(message = "Selecione um laboratório.")
	private Laboratorio laboratorio;
	
	@Range(min = 1, message = "O campo Valor não pode conter 0 ou valores negativos.")
	@Digits(integer = 18, fraction = 2, message = "Campo inválido!\nEx: 10,2.")
	@NotNull(message = "O campo Valor é obrigatório.")
	private BigDecimal valor;

	@NotNull(message = "O campo Status é obrigatório.")
	private Boolean statusEquipamento;

	@Nullable
	private LocalDate dataExclusao;
	
	@NotBlank(message = "O campo de Descrição é obrigatório.")
	private String descricao;

	public EquipamentoDto(Equipamento eqp) {
		if (eqp != null) {
			this.id = eqp.getId();
			this.nomeEquipamento = eqp.getNomeEquipamento();
			this.codigoPatrimonio = eqp.getCodigoPatrimonio();
			this.dataAquisicao = eqp.getDataAquisicao();
			this.laboratorio = eqp.getLaboratorio();
			this.valor = eqp.getValor();
			this.dataExclusao = eqp.getDataExclusao();
			this.descricao = eqp.getDescricao();
			this.statusEquipamento = eqp.getStatus();
		}
	}
	
	public Equipamento toEntity() {
		return Equipamento.builder()
					.nomeEquipamento(nomeEquipamento)
					.laboratorio(laboratorio)
					.codigoPatrimonio(codigoPatrimonio)
					.dataAquisicao(dataAquisicao)
					.descricao(descricao)
					.valor(valor)
					.status(statusEquipamento)
					.dataExclusao(dataExclusao)
					.build();
	}
}