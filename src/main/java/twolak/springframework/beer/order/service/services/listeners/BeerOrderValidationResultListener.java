package twolak.springframework.beer.order.service.services.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import twolak.springframework.beer.order.service.config.JmsConfig;
import twolak.springframework.beer.order.service.services.BeerOrderManager;
import twolak.springframework.brewery.model.events.ValidateBeerOrderResult;

/**
 *
 * @author twolak
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationResultListener {
    
    private final BeerOrderManager beerOrderManager;
    
    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESULT_QUEUE)
    public void listen(ValidateBeerOrderResult validateBeerOrderResult) {
        log.debug("Validation result for Beer Order Id: " + validateBeerOrderResult.getOrderId());
        
        this.beerOrderManager.processValidationResult(validateBeerOrderResult.getOrderId(), validateBeerOrderResult.getIsValid());
    }
}
