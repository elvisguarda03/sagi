package br.com.ternarius.inventario.sagi.domain.service;

import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface PushNotificationService {

    @Async
    CompletableFuture<?> send(HttpEntity<?> httpEntity);
}
