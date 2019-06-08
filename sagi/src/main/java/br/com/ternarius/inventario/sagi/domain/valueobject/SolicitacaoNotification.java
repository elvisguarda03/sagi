package br.com.ternarius.inventario.sagi.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SolicitacaoNotification {
    private String nome;
    private String title;
    private String message;
    private String url;
    private String templateId;
}
