package com.saga.order.config;


import com.saga.order.entity.PurchaseOrder;
import com.saga.order.repository.OrderRepository;
import com.saga.order.service.OrderStatusPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import saga.commons.dto.OrderRequestDto;
import saga.commons.events.OrderStatus;
import saga.commons.events.PaymentStatus;

import java.util.function.Consumer;

@Configuration
public class OrderStatusUpdateHandler {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderStatusPublisher publisher;


    @Transactional
    public void updateOrder(int id, Consumer<PurchaseOrder> consumer) {
        return repository.findById(id).ifPresent(
                consumer.andThen(this::updateOrder));
    }

    private void updateOrder(PurchaseOrder purchaseOrder) {

       boolean isPaymentComplete = PaymentStatus.PAYMENT_COMPLETED.equals(purchaseOrder.getPaymentStatus());

       OrderStatus orderStatus = isPaymentComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;

       purchaseOrder.setOrderStatus(orderStatus);

       if (!isPaymentComplete) {
           publisher.publishOrderEvent(convertEntityToDto(purchaseOrder), orderStatus);
       }

    }

    public OrderRequestDto convertEntityToDto(PurchaseOrder purchaseOrder) {

        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setOrderId(purchaseOrder.getId())_;
        orderRequestDto.setUserId(purchaseOrder.getUserId());
        orderRequestDto.setAmount(purchaseOrder.getPrice());
        orderRequestDto.setProductId(purchaseOrder.getProductId());

        return orderRequestDto;
    }

    }