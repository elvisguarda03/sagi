package br.com.ternarius.inventario.sagi.application.dto;

import br.com.ternarius.inventario.sagi.domain.entity.Equipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
import br.com.ternarius.inventario.sagi.domain.entity.SolicitacaoEquipamento;
import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
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
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SolicitacaoEquipamentoDto {
    @Nullable
    private String id;

    @CreatedBy
    private Usuario comodatario;

    @LastModifiedBy
    @Nullable
    private Usuario comodante;

    @Nullable
    private Equipamento equipamento;

    @Nullable
    private StatusMovimentacao status;

    @NotNull(message = "Por favor, selecione ao menos um Equipamento.")
    private List<String> equipamentosId;

    @NotNull(message = "Por favor, selecione um Laboratório.")
    private String laboratorioId;

    @Nullable
    private Laboratorio novaLocalizacao;

    @Nullable
    private LocalDateTime dataSolicitacao;

    @Nullable
    private LocalDateTime dataModificacao;

    public SolicitacaoEquipamentoDto(SolicitacaoEquipamento solicitacao) {
        if (!isNull(solicitacao)) {
            this.id = solicitacao.getId();
            this.status = solicitacao.getStatus();
            this.comodante = solicitacao.getComodante();
            this.comodatario = solicitacao.getComodatario();
            this.equipamento = solicitacao.getEquipamento();
            this.novaLocalizacao = solicitacao.getNovaLocalizacao();
            this.dataModificacao = solicitacao.getDataModificacao();
            this.dataSolicitacao = solicitacao.getDataSolicitacao();
        }
    }

    public SolicitacaoEquipamento toEntity() {
        return SolicitacaoEquipamento.builder()
                .id(id)
                .comodante(comodante)
                .comodatario(comodatario)
                .dataModificacao(dataModificacao)
                .dataSolicitacao(dataSolicitacao)
                .equipamento(equipamento)
                .novaLocalizacao(novaLocalizacao)
                .status(status)
                .build();
    }
}
