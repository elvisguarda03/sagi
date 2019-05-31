package br.com.ternarius.inventario.sagi.domain.valueobject;

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
public class EmailNotification {
    private String nome;
    private String from;
    private String to;
    private String title;
    private String message;
    private String template;
    private String url;
}
