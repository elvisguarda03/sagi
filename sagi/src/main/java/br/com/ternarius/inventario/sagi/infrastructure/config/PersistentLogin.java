package br.com.ternarius.inventario.sagi.infrastructure.config;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Entity
@Table(name = "persistent_logins")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PersistentLogin {
	
	@Id
	@Column(length = 64)
	private String series;
	
	@Column(length = 100, nullable = false)
	private String username;
	
	@Column(length = 64, nullable = false)
	private String token;

	@CreatedDate
	@Column(name = "last_used", nullable = false)
	private LocalDateTime lastUsed;
}