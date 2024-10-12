package com.javaoktato.sensordemo.coap.backend;

import com.javaoktato.sensordemo.coap.backend.coap.UserDataResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.elements.config.UdpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoApDemoApplication implements CommandLineRunner {
    @Autowired
    public UserDataResource userDataResource;

    public static void main(String[] args) {
        SpringApplication.run(CoApDemoApplication.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {
        CoapConfig.register();
        UdpConfig.register();
        CoapServer coapServer = new CoapServer();
        coapServer.add(userDataResource);
//        EndpointManager.getEndpointManager().getNetworkInterfaces()
//                .stream()
//                .map(e -> new InetSocketAddress(e, COAP_PORT))
//                .map(CoapEndpoint::new)
//                .forEach(coapServer::addEndpoint);

        coapServer.start();
    }
}
