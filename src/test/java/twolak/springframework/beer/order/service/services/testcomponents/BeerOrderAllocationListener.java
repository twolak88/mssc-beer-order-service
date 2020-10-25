package twolak.springframework.beer.order.service.services.testcomponents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import twolak.springframework.beer.order.service.config.JmsConfig;
import twolak.springframework.brewery.model.events.AllocateBeerOrderRequest;
import twolak.springframework.brewery.model.events.AllocateBeerOrderResult;

/**
 *
 * @author twolak
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationListener {
    
    private final JmsTemplate jmsTemplate;
    
    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(Message message) {
        
        AllocateBeerOrderRequest allocateBeerOrderRequest = (AllocateBeerOrderRequest) message.getPayload();
        
        allocateBeerOrderRequest.getBeerOrderDto().getBeerOrderLines().forEach((beerOrderLineDto) -> {
            beerOrderLineDto.setQuantityAllocated(beerOrderLineDto.getOrderQuantity());
        });
        
        this.jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE, 
                AllocateBeerOrderResult.builder()
                .beerOrderDto(allocateBeerOrderRequest.getBeerOrderDto())
                .allocationError(Boolean.FALSE)
                .pendingInventory(Boolean.FALSE)
                .build());
    }
}
