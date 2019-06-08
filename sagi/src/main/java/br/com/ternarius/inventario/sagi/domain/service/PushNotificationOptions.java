package br.com.ternarius.inventario.sagi.domain.service;

import com.currencyfair.onesignal.model.app.AppResponse;

public class PushNotificationOptions {
    private static final String restApiKey = System.getenv("REST_API_KEY");
    private static final String appId = System.getenv("APP_ID");

    public static AppResponse getUserId() {
        return null;
    }
}
