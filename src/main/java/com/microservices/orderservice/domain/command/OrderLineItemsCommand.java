package com.microservices.orderservice.domain.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class OrderLineItemsCommand {
     private String skuCode;
     private BigDecimal price;
     private Integer quantity;
}
