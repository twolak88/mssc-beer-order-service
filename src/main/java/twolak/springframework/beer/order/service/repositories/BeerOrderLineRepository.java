package twolak.springframework.beer.order.service.repositories;

import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import twolak.springframework.beer.order.service.domain.BeerOrderLine;

/**
 *
 * @author twolak
 */
public interface BeerOrderLineRepository extends PagingAndSortingRepository<BeerOrderLine, UUID> {
    
}
