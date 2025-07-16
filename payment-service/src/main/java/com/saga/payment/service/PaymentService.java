package com.saga.payment.service;

import com.saga.payment.entity.UserBalance;
import com.saga.payment.repository.UserBalanceRepository;
import com.saga.payment.repository.UserTransactionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saga.commons.dto.OrderRequestDto;
import saga.commons.dto.PaymentRequestDto;
import saga.commons.events.OrderEvent;
import saga.commons.events.PaymentEvent;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @PostConstruct
    public void initUserBalanceInDB() {

        userBalanceRepository.saveAll(
                Stream.of(
                        new UserBalance(101, 5000),
                        new UserBalance(102, 3000),
                        new UserBalance(104, 25000),
                        new UserBalance(105, 99999)
                )
        ).collect(Collectors.toList());
    }

    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {

        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();

        PaymentRequestDto paymentRequestDto =
                new PaymentRequestDto(orderRequestDto.getOrderId(),
                        orderRequestDto.getUserId(),
                        orderRequestDto.getAmount());


        userBalanceRepository.findById(userRequestDto.getUserId())
                .filter(ub -> ub.getPrice() > orderRequestDto.getAmount())
                .map(

                );

        return new PaymentEvent();

    }

    public void cancelOrderEvent(OrderEvent orderEvent) {



    }

}
