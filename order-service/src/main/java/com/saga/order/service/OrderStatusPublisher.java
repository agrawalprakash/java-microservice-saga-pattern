package com.saga.order.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;
import saga.commons.dto.OrderRequestDto;
import saga.commons.events.OrderEvent;
import saga.commons.events.OrderStatus;

@Service
public class OrderStatusPublisher {

    @Autowired
    private Sinks.Many<OrderEvent> orderSinks;

    public void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {

        OrderEvent orderEvent = new OrderEvent(orderRequestDto, orderStatus);

        orderSinks.tryEmitNext(orderEvent);

    }
}
