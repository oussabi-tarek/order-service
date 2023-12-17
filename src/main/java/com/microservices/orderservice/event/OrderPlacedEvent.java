package com.microservices.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
  private String orderNumber;
}
