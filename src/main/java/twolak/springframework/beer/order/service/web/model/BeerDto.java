package twolak.springframework.beer.order.service.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
public class BeerDto extends BaseItem {

    @Builder
    public BeerDto(UUID id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate, 
            String beerName, BeerStyleEnum beerStyle, String upc, BigDecimal price, Integer quantityOnHand) {
        super(id, version, createdDate, lastModifiedDate);
        this.beerName = beerName;
        this.beerStyle = beerStyle;
        this.upc = upc;
        this.price = price;
        this.quantityOnHand = quantityOnHand;
    }
    
    @NotBlank
    private String beerName;

    @NotNull
    private BeerStyleEnum beerStyle;

    @NotNull
    private String upc;

    @Positive
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    private Integer quantityOnHand;
}
