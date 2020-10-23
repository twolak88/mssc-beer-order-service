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
public class AllocateBeerOrderResult implements Serializable {

    private static final long serialVersionUID = 3473220905821415284L;
    
    private BeerOrderDto beerOrderDto;
    private Boolean allocationError = false;
    private Boolean pendingInventory = false;
}
