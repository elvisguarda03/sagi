package br.com.ternarius.inventario.sagi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 *
 * @author Elvis de Sousa
 *
 */

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movimentacao {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    @NotNull
    private Equipamento equipamento;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private Usuario admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @NotNull
    private Usuario solicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localizacao_atual_id", nullable = false)
    @NotNull
    private Laboratorio localizacaoAtual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localizacao_anterior_id", nullable = false)
    @Nullable
    private Laboratorio localizacaoAnterior;

    @Column(name = "data_permissao")
    @CreatedDate
    private LocalDate dataPermissao;

    @LastModifiedDate
    @Column(name = "data_devolucao")
    @Nullable
    private LocalDate dataDevolucao;
}