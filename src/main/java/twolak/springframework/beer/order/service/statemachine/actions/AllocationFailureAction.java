package twolak.springframework.beer.order.service.statemachine.actions;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import twolak.springframework.beer.order.service.config.JmsConfig;
import twolak.springframework.beer.order.service.domain.BeerOrderEventEnum;
import twolak.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import twolak.springframework.beer.order.service.services.BeerOrderManagerImpl;
import twolak.springframework.brewery.model.events.AllocationFailureEvent;

/**
 *
 * @author twolak
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AllocationFailureAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        UUID beerOrderId = UUID.fromString((String) context.getMessage()
                .getHeaders().get(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER));

        this.jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_FAILURE_QUEUE, AllocationFailureEvent
                .builder().orderId(beerOrderId).build());
        log.debug("Sent Allocation Failure Message to queue for order id: " + beerOrderId);
    }
}
