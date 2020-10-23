package twolak.springframework.brewery.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author twolak
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderLineDto {
    
    @JsonProperty(value = "id")
    private UUID id = null;

    @JsonProperty(value = "version")
    private Integer version = null;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    @JsonProperty(value = "createdDate")
    private OffsetDateTime createdDate = null;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    @JsonProperty(value = "lastModifiedDate")
    private OffsetDateTime lastModifiedDate = null;
    
    private String upc;
    private String beerName;
    private BeerStyleEnum beerStyle;
    private UUID beerId;
    private BigDecimal price;
    private Integer orderQuantity = 0;
    private Integer quantityAllocated = 0;
}
