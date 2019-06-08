package br.com.ternarius.inventario.sagi.infrastructure.service;

import br.com.ternarius.inventario.sagi.domain.service.PushNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 *
 * @author Elvis de Sousa
 *
 */

@Service
@Slf4j
public class AndroidPushNotificationService implements PushNotificationService {
    private static final String KEY_ASSIGN = "key=";
    private static final String FIREBASE_SERVER_KEY = "AAAAhk4kzbk:APA91bEytN76PhVQCtnPGrpp7v7Wm3SQeNRz_xlioO3pIWEZp6zlnj2pY8mytT2WYytAOJ6Uw4DH2mmc1xWaTVIbq2pcrSvCBS8Dk8VYYOQjsU_sJlyi4p9Xq2ULRofTVgmBIhhDpQHy";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    public CompletableFuture<?> send(HttpEntity<?> entity) {
        var interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", KEY_ASSIGN
                + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));

        var restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);

        var firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

        return completedFuture(firebaseResponse);
    }
}