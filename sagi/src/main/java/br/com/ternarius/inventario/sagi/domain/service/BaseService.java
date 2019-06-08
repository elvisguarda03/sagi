package br.com.ternarius.inventario.sagi.domain.service;

import br.com.ternarius.inventario.sagi.domain.entity.Usuario;
import br.com.ternarius.inventario.sagi.domain.valueobject.EmailNotification;
import br.com.ternarius.inventario.sagi.domain.valueobject.SolicitacaoNotification;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseService {

    protected final void sendAllNotificationForEmail(SolicitacaoNotification solicitacaoNotification, EmailNotificationService notificationService, List<Usuario> admins) {
        admins.forEach(a -> {
            EmailNotification pushNotification = EmailNotification.builder()
                    .from("sagi@gmail.com")
                    .to(a.getEmail())
                    .template(solicitacaoNotification.getTemplateId())
                    .nome(solicitacaoNotification.getNome())
                    .title(solicitacaoNotification.getTitle())
                    .message(solicitacaoNotification.getMessage())
                    .url(solicitacaoNotification.getUrl())
                    .build();
            notificationService.send(pushNotification);
        });
    }
}
