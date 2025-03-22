package sibling.stream.order.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTracking {

    private String orderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "dd-MM-yyyy hh:mm:ss")
    public Date time;
    @Builder.Default
    public OrderTrackingState state = OrderTrackingState.REQUESTED;

    public static enum OrderTrackingState {
        REQUESTED, VALIDATED, LOW_FRAUD, APPROVED, CONFIRMED, SHIPMENT_PREPARED, SHIPMENT_DISPATCHED, SHIPMENT_DELIVERED, COMPLETED
    }
}