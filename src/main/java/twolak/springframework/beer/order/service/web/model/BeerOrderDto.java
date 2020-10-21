package twolak.springframework.beer.order.service.web.model;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import twolak.springframework.beer.order.service.domain.BeerOrderStatusEnum;

/**
 *
 * @author twolak
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BeerOrderDto extends BaseItem {

    @Builder
    public BeerOrderDto(UUID id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate, 
            UUID customerId, String customerRef, Set<BeerOrderLineDto> beerOrderLines, BeerOrderStatusEnum orderStatus, String orderStatusCallbackUrl) {
        super(id, version, createdDate, lastModifiedDate);
        this.customerId = customerId;
        this.customerRef = customerRef;
        this.beerOrderLines = beerOrderLines;
        this.orderStatus = orderStatus;
        this.orderStatusCallbackUrl = orderStatusCallbackUrl;
    }
    
    private UUID customerId;
    private String customerRef;
    private Set<BeerOrderLineDto> beerOrderLines;
    private BeerOrderStatusEnum orderStatus;
    private String orderStatusCallbackUrl;
}
