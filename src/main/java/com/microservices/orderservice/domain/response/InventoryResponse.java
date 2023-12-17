package com.microservices.orderservice.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class InventoryResponse {
    private String skuCode;
    private boolean inStock;
}
