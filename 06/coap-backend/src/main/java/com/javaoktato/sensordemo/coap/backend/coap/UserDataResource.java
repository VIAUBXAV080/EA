/*
 * A diasor és a kapcsolódó forráskódok és példák a szerző által tartott
 * tanfolyamon résztvevők számára készült, és csak a résztvevők jogosultak
 * saját tanulás céljára felhasználni. Az anyagok módosítása vagy a
 * fentitől eltérő felhasználása csak a szerző előzetes engedélyével történhet.
 *
 * Copyright (c) Kövesdán Gábor 2017
 * gabor.kovesdan@gmail.com
 */

package com.javaoktato.sensordemo.coap.backend.coap;

import com.google.gson.Gson;
import com.javaoktato.sensordemo.coap.backend.data.UserData;
import com.javaoktato.sensordemo.coap.backend.data.UserDataRepository;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserDataResource extends CoapResource {

    @Autowired
    private Gson gson;

    @Autowired
    private UserDataRepository repository;

    public UserDataResource() {
        super("UserDataResource");
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        List<UserData> data = repository.findAll();
        exchange.respond(CoAP.ResponseCode.VALID, gson.toJson(data), MediaTypeRegistry.APPLICATION_JSON);
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        exchange.accept();

        int format = exchange.getRequestOptions().getContentFormat();
        if (format == MediaTypeRegistry.APPLICATION_JSON) {
            String userDataJson = exchange.advanced().getCurrentRequest().getPayloadString();
            System.out.println("Received data: " + userDataJson);
            UserData userData = gson.fromJson(userDataJson, UserData.class);
            userData = repository.save(userData);
            exchange.respond(CoAP.ResponseCode.CREATED, gson.toJson(userData), MediaTypeRegistry.APPLICATION_JSON);
        }
    }
}
