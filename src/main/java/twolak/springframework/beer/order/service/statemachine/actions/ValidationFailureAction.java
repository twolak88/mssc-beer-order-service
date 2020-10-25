package twolak.springframework.beer.order.service.statemachine.actions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import twolak.springframework.beer.order.service.domain.BeerOrderEventEnum;
import twolak.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import twolak.springframework.beer.order.service.services.BeerOrderManagerImpl;

/**
 *
 * @author twolak
 */
@Slf4j
@Component
public class ValidationFailureAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        String beerOrderId = (String) context.getMessage()
                .getHeaders().get(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER);
        log.error("Compensating Transaction... Validation failed! Id: " + beerOrderId);
    }
    
}
