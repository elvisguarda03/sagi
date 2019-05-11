package br.com.ternarius.inventario.sagi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EntityScan(basePackages = {"br.com.ternarius.inventario.sagi.domain.entity", "br.com.ternarius.inventario.sagi.infrastructure.config"})
@ComponentScan(basePackages = {"br.com.ternarius.*"})
@EnableJpaRepositories(basePackages = {"br.com.ternarius.inventario.sagi.infrastructure.repository"})
@EnableTransactionManagement
@EnableJpaAuditing
@SpringBootApplication
public class SagiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SagiApplication.class, args);
	}
} 