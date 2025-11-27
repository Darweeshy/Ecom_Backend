package org.example.springecom.service;

import org.example.springecom.model.Order;
import org.example.springecom.model.paymob.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class PaymobService {

    @Value("${paymob.api-key}")
    private String apiKey;

    @Value("${paymob.integration-id}")
    private long integrationId;

    @Autowired
    private RestTemplate restTemplate;

    private static final String PAYMOB_API_BASE_URL = "https://accept.paymob.com/api";

    // Step 1: Get Auth Token
    private String getAuthToken() {
        AuthRequest request = new AuthRequest(apiKey);
        AuthResponse response = restTemplate.postForObject(PAYMOB_API_BASE_URL + "/auth/tokens", request, AuthResponse.class);
        return response != null ? response.getToken() : null;
    }

    // Step 2: Register Order
    private OrderRegistrationResponse registerOrder(String authToken, Order order) {
        OrderRegistrationRequest request = OrderRegistrationRequest.builder()
                .auth_token(authToken)
                .delivery_needed("false")
                .amount_cents(order.getTotalPrice().longValue() * 100)
                .currency("EGP")
                .merchant_order_id(order.getId().toString())
                .items(Collections.emptyList())
                .build();

        return restTemplate.postForObject(PAYMOB_API_BASE_URL + "/ecommerce/orders", request, OrderRegistrationResponse.class);
    }

    // Step 3: Get Payment Key
    private String getPaymentKey(String authToken, Order order, long paymobOrderId) {
        PaymentKeyRequest.BillingData billingData = PaymentKeyRequest.BillingData.builder()
                .email(order.getUser().getEmail())
                .first_name(order.getUser().getUsername())
                .last_name("N/A")
                .phone_number(order.getPhoneNumber())
                .street(order.getLocation())
                .building("N/A").floor("N/A").apartment("N/A").city("N/A").country("EG")
                .build();

        PaymentKeyRequest request = PaymentKeyRequest.builder()
                .auth_token(authToken)
                .amount_cents(order.getTotalPrice().longValue() * 100)
                .expiration(3600)
                .order_id(paymobOrderId)
                .billing_data(billingData)
                .currency("EGP")
                .integration_id(integrationId)
                .build();

        PaymentKeyResponse response = restTemplate.postForObject(PAYMOB_API_BASE_URL + "/acceptance/payment_keys", request, PaymentKeyResponse.class);
        return response != null ? response.getToken() : null;
    }

    // Main public method
    public String initiatePayment(Order order) {
        String authToken = getAuthToken();
        if (authToken == null) throw new RuntimeException("Could not authenticate with Paymob.");

        OrderRegistrationResponse orderResponse = registerOrder(authToken, order);
        if (orderResponse == null) throw new RuntimeException("Could not register order with Paymob.");

        order.setPaymobOrderId(orderResponse.getId());

        return getPaymentKey(authToken, order, orderResponse.getId());
    }
}