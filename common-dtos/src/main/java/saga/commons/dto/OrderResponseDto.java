package saga.commons.dto;

import saga.commons.events.OrderStatus;

public class OrderResponseDto {

    private Integer userId;
    private Integer productId;
    private Integer amount;
    private Integer orderId;
    private OrderStatus orderStatus;

}
