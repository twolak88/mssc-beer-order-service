package twolak.springframework.beer.order.service.services;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import twolak.springframework.beer.order.service.web.model.BeerOrderDto;
import twolak.springframework.beer.order.service.web.model.BeerOrderPagedList;

/**
 *
 * @author twolak
 */
public interface BeerOrderService {
    BeerOrderPagedList listOrders(UUID customerId, Pageable pageable);

    BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto);

    BeerOrderDto getOrderById(UUID customerId, UUID orderId);

    void pickupOrder(UUID customerId, UUID orderId);
}
