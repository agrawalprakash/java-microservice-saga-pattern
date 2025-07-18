package com.saga.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import saga.commons.events.PaymentEvent;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {

    @Autowired
    private OrderStatusUpdateHandler handler;

    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {

        // Listen to payment event topic
        // will check payment status
        // if payment status completed
        // or other status

        return (payment) ->
                handler.updateOrder(
                        payment.getPaymentRequestDto().getOrderId(),
                        po -> {
                            po.setPaymentStatus(
                                    payment.getPaymentStatus()
                            );
                        }
                )
    }

}
