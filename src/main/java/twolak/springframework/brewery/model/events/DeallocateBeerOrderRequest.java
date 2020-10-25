package twolak.springframework.brewery.model.events;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import twolak.springframework.brewery.model.BeerOrderDto;

/**
 *
 * @author twolak
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeallocateBeerOrderRequest implements Serializable {

    private static final long serialVersionUID = -189084177759845447L;
    
    private BeerOrderDto beerOrderDto;
}
