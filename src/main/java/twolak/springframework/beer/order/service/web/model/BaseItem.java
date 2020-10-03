package twolak.springframework.beer.order.service.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author twolak
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseItem {
    
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
}
