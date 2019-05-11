package br.com.ternarius.inventario.sagi.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Data
@Component
public class SendGridConfig {
	
	@Value("${app.SENDGRID_API_KEY}")
	private String sendGridAPIKey;
}
