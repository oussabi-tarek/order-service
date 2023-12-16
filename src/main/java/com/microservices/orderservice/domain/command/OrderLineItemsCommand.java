package com.microservices.orderservice.domain.command;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class OrderLineItemsCommand {
     private String skuCode;
     private BigDecimal price;
     private Integer quantity;
}
