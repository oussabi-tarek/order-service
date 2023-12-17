package com.microservices.orderservice.domain.command;


import com.microservices.orderservice.domain.model.OrderLineItems;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCommand {
   private List<OrderLineItemsCommand> orderLineItemsCommands;
}
