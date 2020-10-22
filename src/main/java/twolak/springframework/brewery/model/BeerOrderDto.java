package twolak.springframework.brewery.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.Set;
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
public class BeerOrderDto {

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
    
    private UUID customerId;
    private String customerRef;
    private Set<BeerOrderLineDto> beerOrderLines;
    private BeerOrderStatusEnum orderStatus;//can be change to string
    private String orderStatusCallbackUrl;
}
