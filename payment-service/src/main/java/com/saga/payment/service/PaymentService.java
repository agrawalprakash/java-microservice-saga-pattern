package com.saga.payment.service;

import com.saga.payment.entity.UserBalance;
import com.saga.payment.entity.UserTransaction;
import com.saga.payment.repository.UserBalanceRepository;
import com.saga.payment.repository.UserTransactionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saga.commons.dto.OrderRequestDto;
import saga.commons.dto.PaymentRequestDto;
import saga.commons.events.OrderEvent;
import saga.commons.events.PaymentEvent;
import saga.commons.events.PaymentStatus;

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

    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {

        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();

        PaymentRequestDto paymentRequestDto =
                new PaymentRequestDto(orderRequestDto.getOrderId(),
                        orderRequestDto.getUserId(),
                        orderRequestDto.getAmount());


        userBalanceRepository.findById(userRequestDto.getUserId())
                .filter(ub -> ub.getPrice() > orderRequestDto.getAmount())
                .map(ub -> {
                    ub.setPrice(ub.getPrice() - orderRequestDto.getAmount());
                            userTransactionRepository.save(new UserTransaction(orderRequestDto.getOrderId(),
                                    orderRequestDto.getUserId(),
                                    orderRequestDto.getAmount()));
                            return new PaymentEvent(paymentRequestDto,
                                    PaymentStatus.PAYMENT_COMPLETED);
                        }
                ).orElse(new PaymentEvent(paymentRequestDto , PaymentStatus.PAYMENT_FAILED));

        return new PaymentEvent();

    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {

        userTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId()).
                ifPresent(ut -> {
                    userTransactionRepository.delete(ut);
                    userBalanceRepository.findById(ut.getUserId())
                            .ifPresent(ub ->
                                    ub.setAmount(ub.getAmount + ut.getAmount()));
                });


    }

}
