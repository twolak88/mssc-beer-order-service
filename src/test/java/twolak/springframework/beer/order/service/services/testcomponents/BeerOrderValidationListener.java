package twolak.springframework.beer.order.service.services.testcomponents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import twolak.springframework.beer.order.service.config.JmsConfig;
import twolak.springframework.brewery.model.events.ValidateBeerOrderRequest;
import twolak.springframework.brewery.model.events.ValidateBeerOrderResult;

/**
 *
 * @author twolak
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {

    public static final String FAIL_VALIDATION = "fail-validation";
    public static final String CANCEL_VALIDATION = "cancel-validation";

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(Message message) {

        ValidateBeerOrderRequest beerOrderRequest = (ValidateBeerOrderRequest) message.getPayload();

        log.debug("Validating order... ID: " + beerOrderRequest.getBeerOrderDto().getId());

        if (!CANCEL_VALIDATION.equals(beerOrderRequest.getBeerOrderDto().getCustomerRef())) {
            boolean isValid = !FAIL_VALIDATION.equals(beerOrderRequest.getBeerOrderDto().getCustomerRef());

            this.jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESULT_QUEUE,
                    ValidateBeerOrderResult.builder()
                            .isValid(isValid)
                            .orderId(beerOrderRequest.getBeerOrderDto().getId())
                            .build());
        }
    }
}
