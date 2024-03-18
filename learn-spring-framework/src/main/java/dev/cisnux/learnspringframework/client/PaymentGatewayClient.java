package dev.cisnux.learnspringframework.client;

import lombok.Data;


// simulate third party libraries
@Data
public class PaymentGatewayClient {

    private String endpoint;

    private String privateKey;

    private String publicKey;
}

