package twolak.springframework.beer.order.service.web.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author twolak
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderStatusUpdate extends BaseItem {

    @Builder
    public OrderStatusUpdate(UUID id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate,
            UUID orderId, String customerRef, String orderStatus) {
        super(id, version, createdDate, lastModifiedDate);
        this.orderId = orderId;
        this.customerRef = customerRef;
        this.orderStatus = orderStatus;
    }
    
    private UUID orderId;
    private String customerRef;
    private String orderStatus;
}
