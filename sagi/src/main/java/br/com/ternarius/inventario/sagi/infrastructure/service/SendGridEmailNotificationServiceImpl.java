package br.com.ternarius.inventario.sagi.infrastructure.service;

import java.io.IOException;

import br.com.ternarius.inventario.sagi.infrastructure.config.DynamicTemplatePersonalization;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.ternarius.inventario.sagi.domain.service.EmailNotificationService;
import br.com.ternarius.inventario.sagi.domain.valueobject.EmailNotification;
import br.com.ternarius.inventario.sagi.infrastructure.config.SendGridConfig;
import lombok.RequiredArgsConstructor;

/**
 * @author Elvis da Guarda
 */
@Service
@Slf4j
@Qualifier
@RequiredArgsConstructor
public class SendGridEmailNotificationServiceImpl implements EmailNotificationService {

    private final SendGridConfig sendConfig;

    @Override
    public void send(EmailNotification notification) {
        var from = new Email(notification.getFrom());
        var to = new Email(notification.getTo());

        var content = new Content("text/plain", notification.getMessage() + notification.getUrl());

        var mail = new Mail(from, notification.getTitle(), to, content);

        var sendGridClient = new SendGrid(sendConfig.getSendGridAPIKey());

        try {
            var rq = new Request();
            rq.setMethod(Method.POST);
            rq.setEndpoint("mail/send");
            rq.setBody(mail.build());

            var response = sendGridClient.api(rq);

            log.info("" + response.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}