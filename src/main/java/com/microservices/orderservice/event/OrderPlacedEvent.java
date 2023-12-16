package com.microservices.orderservice.event;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderPlacedEvent {
  private String orderNumber;
}
