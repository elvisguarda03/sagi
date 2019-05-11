package br.com.ternarius.inventario.sagi.infrastructure.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

import br.com.ternarius.inventario.sagi.domain.service.EmailNotificationService;
import br.com.ternarius.inventario.sagi.domain.valueobject.EmailNotification;
import br.com.ternarius.inventario.sagi.infrastructure.config.SendGridConfig;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Elvis da Guarda
 *
 */
@Service
@Qualifier
@RequiredArgsConstructor
public class SendGridEmailNotificationServiceImpl implements EmailNotificationService {
	
	private final SendGridConfig sendConfig;
	
	@Override
	public void send(EmailNotification notification) {
		try {
			Email from = new Email(notification.getFrom());
			Email to = new Email(notification.getTo());
			Content content = new Content("text/plain", notification.getMessage() + "\n"
					+ notification.getUrl());
			Mail mail = new Mail(from, notification.getTitle(), to, content);
			Request rq = new Request();
			rq.setMethod(Method.POST);
			rq.setEndpoint("mail/send");
			rq.setBody(mail.build());
			SendGrid sendGridClient = new SendGrid(sendConfig.getSendGridAPIKey());
			sendGridClient.api(rq);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}