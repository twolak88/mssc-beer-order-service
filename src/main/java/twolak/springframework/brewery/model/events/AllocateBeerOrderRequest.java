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
public class AllocateBeerOrderRequest implements Serializable{

    private static final long serialVersionUID = 3654341402373484029L;
    
    private BeerOrderDto beerOrderDto;
}
