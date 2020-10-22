package twolak.springframework.beer.order.service.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twolak.springframework.beer.order.service.bootstrap.BeerOrderBootStrap;
import twolak.springframework.beer.order.service.domain.Customer;
import twolak.springframework.beer.order.service.repositories.BeerOrderRepository;
import twolak.springframework.beer.order.service.repositories.CustomerRepository;
import twolak.springframework.brewery.model.BeerOrderDto;
import twolak.springframework.brewery.model.BeerOrderLineDto;

/**
 *
 * @author twolak
 */
@Slf4j
@Service
public class TastingRoomService {
    
    private final CustomerRepository customerRepository;
    private final BeerOrderService beerOrderService;
    private final BeerOrderRepository beerOrderRepository;
    private final List<String> beerUpcs = new ArrayList<>();

    public TastingRoomService(CustomerRepository customerRepository, BeerOrderService beerOrderService, BeerOrderRepository beerOrderRepository) {
        this.customerRepository = customerRepository;
        this.beerOrderService = beerOrderService;
        this.beerOrderRepository = beerOrderRepository;
        
        this.beerUpcs.add(BeerOrderBootStrap.BEER_1_UPC);
        this.beerUpcs.add(BeerOrderBootStrap.BEER_2_UPC);
        this.beerUpcs.add(BeerOrderBootStrap.BEER_3_UPC);
    }
    
    @Transactional
    @Scheduled(fixedRate = 2000)
    public void placeTastingRoomOrder() {
        List<Customer> customers = this.customerRepository.findAllByCustomerNameLike(BeerOrderBootStrap.TASTING_ROOM);
        
        if (customers.size() == 1) {
            doPlaceOrder(customers.get(0));
        } else {
            log.error("Too many or too few tasting room customers found");
        }
    }
    
    public void doPlaceOrder(Customer customer) {
        String beerToOrder = getRandomBeerUpc();
        
        BeerOrderLineDto beerOrderLineDto = BeerOrderLineDto.builder()
                .upc(beerToOrder)
                .orderQuantity(new Random().nextInt(6))
                .build();
        Set<BeerOrderLineDto> beerOrderLineDtos = new HashSet<>();
        beerOrderLineDtos.add(beerOrderLineDto);
        
        BeerOrderDto beerOrderDto = BeerOrderDto.builder()
                .customerId(customer.getId())
                .customerRef(UUID.randomUUID().toString())
                .beerOrderLines(beerOrderLineDtos)
                .build();
        
        BeerOrderDto savedBeerOrderDto = this.beerOrderService.placeOrder(customer.getId(), beerOrderDto);
    }
    
    private String getRandomBeerUpc() {
        return this.beerUpcs.get(new Random().nextInt(this.beerUpcs.size() - 0));
    }
}
