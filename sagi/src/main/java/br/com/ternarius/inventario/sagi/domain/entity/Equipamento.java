package br.com.ternarius.inventario.sagi.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import br.com.ternarius.inventario.sagi.infrastructure.service.HistoricoListener;
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
@EntityListeners(HistoricoListener.class)
public class Equipamento {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	@Column(name = "nome_equipamento", nullable=false)
	@NotBlank(message = "O nome do equipamento é obrigatório")
	@Pattern(regexp = "/[a-zA-Z\\\\u00C0-\\\\u00FF ]+/i")
	private String nomeEquipamento;
	
	@Column(nullable = true, unique = true)
	@Range(min = 1, message = "O código de patrimônio não pode conter 0 ou valores negativos")
	@Nullable
	private Long codigoPatrimonio;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "data_aquisicao", nullable = true)
	@Nullable
	private LocalDate dataAquisicao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "Selecione um laboratório")
	private Laboratorio laboratorio;
	
	@Range(min = 1, message = "O campo de valor não pode conter 0 ou valores negativos")
	@Column(nullable=false)
	@Digits(integer = 18, fraction = 2, message = "Campo inválido!\nEx: 10,2")
	@NotNull(message = "O campo de valor é obrigatório")
	private BigDecimal valor;
	
	@Column(name = "data_exclusao")
	@Nullable
	private LocalDate dataExclusao;
	
	@OneToMany(mappedBy = "equipamento")
	@Builder.Default
	@Nullable
	private List<Historico> historicos = new ArrayList<>();
	
	@Column(nullable=false)
	@NotBlank(message = "O campo de descrição é obrigatório")
	private String descricao;

	public void addHistorico(Historico historico) {
		if (historico != null) {
			historicos.add(historico);
		}
	}
}