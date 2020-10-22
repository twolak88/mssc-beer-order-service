package twolak.springframework.beer.order.service.services;

import twolak.springframework.beer.order.service.domain.BeerOrder;

/**
 *
 * @author twolak
 */
public interface BeerOrderManager {
    
    BeerOrder newBeerOrder(BeerOrder beerOrder);
    
}
