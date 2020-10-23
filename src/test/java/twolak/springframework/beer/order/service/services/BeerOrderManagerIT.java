package twolak.springframework.beer.order.service.services;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import twolak.springframework.beer.order.service.domain.BeerOrder;
import twolak.springframework.beer.order.service.domain.BeerOrderLine;
import twolak.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import twolak.springframework.beer.order.service.domain.Customer;
import twolak.springframework.beer.order.service.repositories.BeerOrderRepository;
import twolak.springframework.beer.order.service.repositories.CustomerRepository;

/**
 *
 * @author twolak
 */
@SpringBootTest
public class BeerOrderManagerIT {
    
    @Autowired
    BeerOrderManager beerOrderManager;
    
    @Autowired
    BeerOrderRepository beerOrderRepository;
    
    @Autowired
    CustomerRepository customerRepository;
    
    Customer testCustomer;
    
    UUID beerId = UUID.randomUUID();
    
    @BeforeEach
    void setUp() {
        this.testCustomer = this.customerRepository.save(Customer.builder()
                .customerName("Test Customer").build());
    }
    
    @Test
    public void testNewToAllocated() {
        BeerOrder beerOrder = createBeerOrder();
        
        BeerOrder savedBeerOrder = this.beerOrderManager.newBeerOrder(beerOrder);
        
        Assertions.assertNotNull(savedBeerOrder);
        Assertions.assertEquals(BeerOrderStatusEnum.ALLOCATED, savedBeerOrder.getBeerOrderStatus());
    }
    
    private BeerOrder createBeerOrder() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customer(testCustomer).build();
        
        Set<BeerOrderLine> beerOrderLines = new HashSet<>();
        beerOrderLines.add(BeerOrderLine.builder()
                .beerId(beerId)
                .orderQuantity(1)
                .beerOrder(beerOrder)
                .build());
        
        beerOrder.setBeerOrderLines(beerOrderLines);
        return beerOrder;
    }
}
