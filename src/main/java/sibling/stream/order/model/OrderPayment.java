package sibling.stream.order.model;

import java.math.BigDecimal;
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
public class OrderPayment {

    private String orderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "dd-MM-yyyy hh:mm:ss")
    public Date time;
    @Builder.Default
    private BigDecimal amount = BigDecimal.ZERO;
    
    @Builder.Default
    public OrderPaymentType type = OrderPaymentType.CREDIT_CARD;

    public static enum OrderPaymentType {
        CREDIT_CARD, PIX, CRYPTO
    }
}