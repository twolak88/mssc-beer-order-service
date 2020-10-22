package twolak.springframework.beer.order.service.services.impl;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twolak.springframework.beer.order.service.domain.BeerOrder;
import twolak.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import twolak.springframework.beer.order.service.domain.Customer;
import twolak.springframework.beer.order.service.repositories.BeerOrderRepository;
import twolak.springframework.beer.order.service.repositories.CustomerRepository;
import twolak.springframework.beer.order.service.services.BeerOrderService;
import twolak.springframework.beer.order.service.services.beer.BeerService;
import twolak.springframework.beer.order.service.web.mappers.BeerOrderMapper;
import twolak.springframework.brewery.model.BeerOrderDto;
import twolak.springframework.brewery.model.BeerOrderPagedList;

/**
 *
 * @author twolak
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BeerOrderServiceImpl implements BeerOrderService {
    
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final ApplicationEventPublisher publisher;
    private final BeerService beerService;

    @Override
    public BeerOrderPagedList listOrders(UUID customerId, Pageable pageable) {
        Optional<Customer> customerOptional = this.customerRepository.findById(customerId);
        
        if (customerOptional.isPresent()) {
            Page<BeerOrder> beerOrderPage = this.beerOrderRepository.findAllByCustomer(customerOptional.get(), pageable);
            return new BeerOrderPagedList(beerOrderPage.stream().map(this.beerOrderMapper::beerOrderToDto).collect(Collectors.toList()), 
                    PageRequest.of(beerOrderPage.getPageable().getPageNumber(), beerOrderPage.getPageable().getPageSize()), 
                    beerOrderPage.getTotalPages());
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto) {
        Optional<Customer> customerOptional = this.customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            BeerOrder beerOrder = this.beerOrderMapper.dtoToBeerOrder(beerOrderDto);
            beerOrder.setId(null);//should be null
            beerOrder.setCustomer(customerOptional.get());
            beerOrder.setBeerOrderStatus(BeerOrderStatusEnum.NEW);
            beerOrder.getBeerOrderLines().forEach(line -> {
                line.setBeerOrder(beerOrder);
                this.beerService.getBeerByUpc(line.getUpc()).ifPresent(beerDto-> {
                    line.setBeerId(beerDto.getId());
                });
            });
            
            BeerOrder savedBeerOrder = this.beerOrderRepository.saveAndFlush(beerOrder);
            
            log.debug("Saved Beer Object " + savedBeerOrder.getId());
            
//            TODO
//            publisher.publishEvent(savedBeerOrder);
            
            return this.beerOrderMapper.beerOrderToDto(savedBeerOrder);
        }
        throw new RuntimeException("Customer Not Found");
    }

    @Override
    public BeerOrderDto getOrderById(UUID customerId, UUID orderId) {
        return this.beerOrderMapper.beerOrderToDto(getOrder(customerId, orderId));
    }

    @Override
    public void pickupOrder(UUID customerId, UUID orderId) {
        BeerOrder beerOrder = getOrder(customerId, orderId);
        beerOrder.setBeerOrderStatus(BeerOrderStatusEnum.PICKED_UP);
        this.beerOrderRepository.save(beerOrder);
    }
    
    private BeerOrder getOrder(UUID customerId, UUID orderId) {
        Optional<Customer> customerOptional = this.customerRepository.findById(customerId);
        
        if (customerOptional.isPresent()) {
            Optional<BeerOrder> beerOrderOptional = this.beerOrderRepository.findById(orderId);
            
            if (beerOrderOptional.isPresent()) {
                BeerOrder beerOrder = beerOrderOptional.get();
                
                // fall to exception if customer id's do not match - order not for customer
                if (beerOrder.getCustomer().getId().equals(customerId)) {
                    return beerOrder;
                }
            }
            throw new RuntimeException("Beer Order Not Found");
        }
        
        throw new RuntimeException("Customer Not Found");
    }
    
}
