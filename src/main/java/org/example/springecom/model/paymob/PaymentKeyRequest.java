package org.example.springecom.model.paymob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentKeyRequest {
    private String auth_token;
    private long amount_cents;
    private long expiration;
    private long order_id;
    private BillingData billing_data;
    private String currency;
    private long integration_id;

    @Data
    @Builder
    public static class BillingData {
        private String email;
        private String first_name;
        private String last_name;
        private String phone_number;
        private String street;
        private String building;
        private String floor;
        private String apartment;
        private String city;
        private String country;
    }
}