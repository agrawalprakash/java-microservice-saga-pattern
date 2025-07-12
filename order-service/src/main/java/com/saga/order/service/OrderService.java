package com.saga.order.service;

import com.saga.order.entity.PurchaseOrder;
import com.saga.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import saga.commons.dto.OrderRequestDto;
import saga.commons.events.OrderStatus;

public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusPublisher orderStatusPublisher;

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDto) {

        PurchaseOrder order = orderRepository.save(convertDtoToEntity(orderRequestDto));
        order.setOrderId(order.getId());
        // produce kafka event with status ORDER_CREATED
        orderStatusPublisher.publishOrderEvent(orderRequestDto, OrderStatus.ORDER_CREATED);
        return order;

    }

    private PurchaseOrder convertDtoToEntity(OrderRequestDto dto) {

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(dto.getAmount());

        return purchaseOrder;
    }

}
