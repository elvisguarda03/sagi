package br.com.ternarius.inventario.sagi.domain.entity;

import br.com.ternarius.inventario.sagi.domain.enums.StatusMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 *
 * @author Elvis de Sousa
 */

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SolicitacaoEquipamento {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comodatario_id", nullable = false)
    private Usuario comodatario;
//Pessoa(Professor) que solicita o empréstimo

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @Nullable
    private Usuario comodante;
//Pessoa(ADMIN) que permite ou nega a solicitação

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    @NotNull(message = "É necessário selecionar um Equipamento.")
    private Equipamento equipamento;

    @Enumerated(value = EnumType.ORDINAL)
    private StatusMovimentacao status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratorio_id", nullable = false)
    @NotNull(message = "É necessário selecionar o Laboratório de destino.")
    private Laboratorio novaLocalizacao;

    @Column(name = "data_solicitacao", nullable = false)
    @CreatedDate
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_modificacao")
    @LastModifiedDate
    private LocalDateTime dataModificacao;
}