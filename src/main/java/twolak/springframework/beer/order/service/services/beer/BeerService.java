package twolak.springframework.beer.order.service.services.beer;

import java.util.Optional;
import java.util.UUID;
import twolak.springframework.beer.order.service.web.model.BeerDto;

/**
 *
 * @author twolak
 */
public interface BeerService {
    Optional<BeerDto> getBeerById(UUID beerId);
    
    Optional<BeerDto> getBeerByUpc(String upc);
}
