package twolak.springframework.beer.order.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.ManagedWireMockServer;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import twolak.springframework.beer.order.service.domain.BeerOrder;
import twolak.springframework.beer.order.service.domain.BeerOrderLine;
import twolak.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import twolak.springframework.beer.order.service.domain.Customer;
import twolak.springframework.beer.order.service.repositories.BeerOrderRepository;
import twolak.springframework.beer.order.service.repositories.CustomerRepository;
import twolak.springframework.beer.order.service.services.beer.impl.BeerServiceImpl;
import twolak.springframework.brewery.model.BeerDto;

/**
 *
 * @author twolak
 */
@ExtendWith(WireMockExtension.class)
@SpringBootTest
public class BeerOrderManagerIT {
    
    private static final String BEER_UPC = "1234";
    
    @Autowired
    private BeerOrderManager beerOrderManager;
    
    @Autowired
    private BeerOrderRepository beerOrderRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private WireMockServer wireMockServer;
    
    private Customer testCustomer;
    
    private UUID beerId = UUID.randomUUID();
    
    @TestConfiguration
    static class RestTemplateBuilderProvider {

        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer() {
            WireMockServer wireMockServer = ManagedWireMockServer
                    .with(WireMockConfiguration.wireMockConfig().port(8083));
            wireMockServer.start();
            return wireMockServer;
        }
    }
    
    @BeforeEach
    void setUp() {
        this.testCustomer = this.customerRepository.save(Customer.builder()
                .customerName("Test Customer").build());
    }
    
    @Test
    public void testNewToAllocated() throws JsonProcessingException {
        BeerDto beerDto = BeerDto.builder().id(beerId).upc(BEER_UPC).build();
        
        this.wireMockServer.stubFor(WireMock.get(BeerServiceImpl.BEER_UPC_PATH_V1 + BEER_UPC)
                .willReturn(WireMock.okJson(this.objectMapper.writeValueAsString(beerDto))));
        
        BeerOrder beerOrder = createBeerOrder();
        
        BeerOrder savedBeerOrder = this.beerOrderManager.newBeerOrder(beerOrder);
        
        Assertions.assertNotNull(savedBeerOrder);
        Assertions.assertEquals(BeerOrderStatusEnum.ALLOCATED, savedBeerOrder.getBeerOrderStatus());
    }
    
//    @Test
//    public void testProcessValidationResult() {
//    }
//
//    @Test
//    public void testBeerOrderAllocationPassed() {
//    }
//
//    @Test
//    public void testBeerOrderAllocationPendingInventory() {
//    }
//
//    @Test
//    public void testBeerOrderAllocationFailed() {
//    }
    
    private BeerOrder createBeerOrder() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customer(testCustomer).build();
        
        Set<BeerOrderLine> beerOrderLines = new HashSet<>();
        beerOrderLines.add(BeerOrderLine.builder()
                .beerId(beerId)
                .upc(BEER_UPC)
                .orderQuantity(1)
                .beerOrder(beerOrder)
                .build());
        
        beerOrder.setBeerOrderLines(beerOrderLines);
        return beerOrder;
    }
}
