package twolak.springframework.beer.order.service.services;

import java.util.UUID;
import twolak.springframework.beer.order.service.domain.BeerOrder;
import twolak.springframework.brewery.model.BeerOrderDto;

/**
 *
 * @author twolak
 */
public interface BeerOrderManager {
    
    BeerOrder newBeerOrder(BeerOrder beerOrder);
    void processValidationResult(UUID beerOrderId, Boolean isValid);
    void beerOrderAllocationPassed(BeerOrderDto beerOrderDto);
    void beerOrderAllocationPendingInventory(BeerOrderDto beerOrderDto);
    void beerOrderAllocationFailed(BeerOrderDto beerOrderDto);
}
