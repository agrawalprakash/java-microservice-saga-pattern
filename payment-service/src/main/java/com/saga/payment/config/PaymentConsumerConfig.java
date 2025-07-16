package com.saga.payment.config;

import com.saga.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import saga.commons.events.OrderEvent;
import saga.commons.events.OrderStatus;
import saga.commons.events.PaymentEvent;

@Configuration
public class PaymentConsumerConfig {

    @Autowired
    private PaymentService paymentService;

    public Function<Flux<OrderEvent>,
                Flux<PaymentEvent>>
    paymentProcessor() {

        return orderEventFlux ->
                orderEventFlux.flatMap(this::processPayment);

    }

    private Mono<PaymentEvent> processPayment(OrderEvent orderEvent) {

        if (OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())) {
            return Mono.fromSupplier(()->
                    this.paymentService.newOrderEvent(orderEvent));
        } else {
            return Mono.fromRunnable(() ->
                    this.paymentService.cancelOrderEvent(orderEvent));
        }

    }
}
