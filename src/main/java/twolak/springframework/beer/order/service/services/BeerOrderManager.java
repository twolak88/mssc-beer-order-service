package twolak.springframework.beer.order.service.services;

import java.util.UUID;
import twolak.springframework.beer.order.service.domain.BeerOrder;

/**
 *
 * @author twolak
 */
public interface BeerOrderManager {
    
    BeerOrder newBeerOrder(BeerOrder beerOrder);
    void processValidationResult(UUID beerOrderId, Boolean isValid);
}
