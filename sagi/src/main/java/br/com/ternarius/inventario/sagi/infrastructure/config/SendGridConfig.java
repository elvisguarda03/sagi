package br.com.ternarius.inventario.sagi.infrastructure.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Elvis da Guarda
 *
 */

@Data
@Component
public class SendGridConfig {
	private final String sendGridAPIKey = System.getenv("SENDGRID_API_KEY");

	@Value("${sendgrid.template-cadastro-id}")
	private String sendGridTemplateCadastro;

	@Value("${sendgrid.template-recupera-senha-id}")
	private String sendGridTemplateRecuperaSenha;
}