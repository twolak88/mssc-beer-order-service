package twolak.springframework.beer.order.service.web.model;

import java.math.BigDecimal;
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
public class BeerOrderLineDto extends BaseItem {
    
    @Builder
    public BeerOrderLineDto(UUID id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate,
            String upc, String beerName, BeerStyleEnum beerStyle, UUID beerId, BigDecimal price, Integer orderQuantity) {
        super(id, version, createdDate, lastModifiedDate);
        this.upc = upc;
        this.beerName = beerName;
        this.beerId = beerId;
        this.orderQuantity = orderQuantity;
        this.beerStyle = beerStyle;
        this.price = price;
    }
    
    private String upc;
    private String beerName;
    private BeerStyleEnum beerStyle;
    private UUID beerId;
    private BigDecimal price;
    private Integer orderQuantity = 0;
}
