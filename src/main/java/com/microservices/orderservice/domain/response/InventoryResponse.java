package com.microservices.orderservice.domain.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InventoryResponse {
    private String skuCode;
    private boolean isInStock;
}
