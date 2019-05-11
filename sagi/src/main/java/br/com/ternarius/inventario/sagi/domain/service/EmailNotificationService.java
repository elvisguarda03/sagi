package br.com.ternarius.inventario.sagi.domain.service;

import org.springframework.stereotype.Service;

import br.com.ternarius.inventario.sagi.domain.valueobject.EmailNotification;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Service
public interface EmailNotificationService {
	
	void send(EmailNotification notification);
}