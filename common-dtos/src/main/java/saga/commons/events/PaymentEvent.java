package saga.commons.events;

import saga.commons.dto.PaymentRequestDto;

import java.util.Date;
import java.util.UUID;

public class PaymentEvent implements Event {

    private UUID eventID = UUID.randomUUID();
    private Date eventDate = new Date();

    private PaymentRequestDto paymentRequestDto;
    private PaymentStatus paymentStatus;

    @Override
    public UUID getEventId() {
        return eventID;
    }

    @Override
    public Date getDate() {
        return eventDate;
    }
}
