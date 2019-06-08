package br.com.ternarius.inventario.sagi.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author Elvis de Sousa
 *
 */

@Data
@Component
@ConfigurationProperties(prefix = "fcm")
public class FcmSettings {
    private String serviceAccountFile;
}