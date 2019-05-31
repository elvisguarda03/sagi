package br.com.ternarius.inventario.sagi.domain.entity;

import br.com.ternarius.inventario.sagi.infrastructure.service.HistoricoListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Elvis da Guarda
 *
 */

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "nome_equipamento", nullable = false)
    @NotBlank(message = "O Nome do Equipamento é obrigatório")
    @Pattern(regexp = "[^0-9]*", message = "Não é possível inserir números no campo Nome Equipamento.")
    private String nomeEquipamento;

    @Column(unique = true)
    @Range(min = 1, message = "O código de patrimônio não pode conter 0 ou valores negativos")
    @Nullable
    private Long codigoPatrimonio;

    @Column(nullable = false)
    @NotNull(message = "O campo Status é obrigatório.")
    private Boolean status;

    @Builder.Default
    @Nullable
    private Boolean isMaintenance = false;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_aquisicao")
    @Nullable
    private LocalDate dataAquisicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Selecione um Laboratório.")
    @JsonBackReference
    private Laboratorio laboratorio;

    @Range(min = 1, message = "O campo Valor não pode conter 0 ou valores negativos.")
    @Column(nullable = false)
    @Digits(integer = 18, fraction = 2, message = "Campo inválido!\nEx: 10,2.")
    @NotNull(message = "O campo Valor é obrigatório.")
    @EqualsAndHashCode.Include
    private BigDecimal valor;

    @Column(name = "data_exclusao")
    @Nullable
    private LocalDate dataExclusao;

    @Column(name = "is_delete", nullable = false)
    @Builder.Default
    private Boolean isDelete = false;

    @OneToMany(mappedBy = "equipamento")
    @Builder.Default
    @JsonIgnore
    @Nullable
    private List<Historico> historicos = new ArrayList<>();

    @Column(nullable = false)
    @NotBlank(message = "O campo Descrição é obrigatório.")
    private String descricao;

    public void addHistorico(Historico historico) {
        if (historico != null) {
            historicos.add(historico);
        }
    }
}