package com.microservices.orderservice.domain.command;


import com.microservices.orderservice.domain.model.OrderLineItems;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OrderCommand {
   private List<OrderLineItemsCommand> orderLineItemsCommands;
}
