package org.example.springecom.model.paymob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRegistrationRequest {
    private String auth_token;
    private String delivery_needed;
    private long amount_cents;
    private String currency;
    private String merchant_order_id;
    private List<Object> items;
}