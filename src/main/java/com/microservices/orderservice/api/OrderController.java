package com.microservices.orderservice.api;

import com.microservices.orderservice.domain.command.OrderCommand;
import com.microservices.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    // what this will do is resilience4j will apply circuit breaker logic to this method
    @CircuitBreaker(name="inventory",fallbackMethod="fallbackMethod")
    @TimeLimiter(name="inventory")
    @Retry(name="inventory")
    // use of CompletableFuture is to make this method asynchronous so that
    public CompletableFuture<String> placeOrder(@RequestBody OrderCommand orderCommand){
        log.info("OrderController is called,{}",orderCommand);
        return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderCommand));
    }

    // use of CompletableFuture is to make this method asynchronous so that
    // it will execute in a separate thread and will not block the main thread
    public CompletableFuture<String> fallbackMethod(OrderCommand orderCommand,RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Oops Something went wrong! Please order after some time ");
    }

}
