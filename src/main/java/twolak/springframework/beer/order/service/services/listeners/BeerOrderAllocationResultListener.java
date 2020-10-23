package twolak.springframework.beer.order.service.services.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import twolak.springframework.beer.order.service.config.JmsConfig;
import twolak.springframework.beer.order.service.services.BeerOrderManager;
import twolak.springframework.beer.order.service.web.mappers.BeerOrderMapper;
import twolak.springframework.brewery.model.events.AllocateBeerOrderResult;

/**
 *
 * @author twolak
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationResultListener {
    
    private final BeerOrderManager beerOrderManager;
    private final BeerOrderMapper beerOrderMapper;
    
    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE)
    public void listen(AllocateBeerOrderResult allocateBeerOrderResult) {
        log.debug("Allocation result for Beer Order Id: " + allocateBeerOrderResult.getBeerOrderDto().getId());
        
        if (!allocateBeerOrderResult.getAllocationError()) {
            if(!allocateBeerOrderResult.getPendingInventory()) {
                this.beerOrderManager.beerOrderAllocationPassed(allocateBeerOrderResult.getBeerOrderDto());
            } else {
                this.beerOrderManager.beerOrderAllocationPendingInventory(allocateBeerOrderResult.getBeerOrderDto());
            }
        } else {
            this.beerOrderManager.beerOrderAllocationFailed(allocateBeerOrderResult.getBeerOrderDto());
        }
    }
}
