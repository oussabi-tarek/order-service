package com.microservices.orderservice.service;

import com.microservices.orderservice.domain.command.OrderCommand;
import com.microservices.orderservice.domain.command.OrderLineItemsCommand;
import com.microservices.orderservice.domain.model.Order;
import com.microservices.orderservice.domain.model.OrderLineItems;
import com.microservices.orderservice.domain.response.InventoryResponse;
import com.microservices.orderservice.event.OrderPlacedEvent;
import com.microservices.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    // transactional annotation so that if any exception occurs then it will rollback the transaction
    @Transactional
    public String placeOrder(OrderCommand orderCommand){
         Order order=new Order();
         order.setOrderNumber(UUID.randomUUID().toString());
         // get orderLineItemsList from orderCommand
         order.setOrderLineItemsList(orderCommand.getOrderLineItemsCommands()
                 .stream().map(this::mapToOrderLineItems)
                 .toList());
         // get skuCodes from orderLineItemsList
         List<String> skuCodes=order.getOrderLineItemsList()
                 .stream()
                 .map(OrderLineItems::getSkuCode)
                 .toList();
         // call inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponsesArray  =webClientBuilder.build().get()
                        .uri("http://inventory-service/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                                .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                .block();
        boolean allProductsInStock;

        if(inventoryResponsesArray.length==0) {
            log.info("No products found");
            allProductsInStock = false;
        }
        else {
            log.info("Products found");
            for (InventoryResponse inventoryResponse : inventoryResponsesArray) {
                log.info("InventoryResponse : {}", inventoryResponse.isInStock());
            }
            allProductsInStock = Arrays.stream(inventoryResponsesArray)
                    .allMatch(InventoryResponse::isInStock);
        }
        log.info("allProductsInStock : {}",allProductsInStock);
        if(allProductsInStock) {
            log.info("All products are in stock");
            orderRepository.save(order);
            // we can also define the notificationTopic as the default topic for our order service so instead ot tap the topic
            // every time we want to send to kafka we can set it as default
            kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getId().toString()));
            return order.getId().toString();
        }
        else {
            throw new IllegalArgumentException("Product is not in stock, please try again lat");
        }
    }
    public OrderLineItems mapToOrderLineItems(OrderLineItemsCommand orderLineItemsCommand){
        return OrderLineItems.builder()
                .skuCode(orderLineItemsCommand.getSkuCode())
                .price(orderLineItemsCommand.getPrice())
                .quantity(orderLineItemsCommand.getQuantity())
                .build();
    }
}
