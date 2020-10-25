package twolak.springframework.beer.order.service.statemachine.actions;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import twolak.springframework.beer.order.service.config.JmsConfig;
import twolak.springframework.beer.order.service.domain.BeerOrder;
import twolak.springframework.beer.order.service.domain.BeerOrderEventEnum;
import twolak.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import twolak.springframework.beer.order.service.repositories.BeerOrderRepository;
import twolak.springframework.beer.order.service.services.BeerOrderManagerImpl;
import twolak.springframework.beer.order.service.web.mappers.BeerOrderMapper;
import twolak.springframework.brewery.model.events.DeallocateBeerOrderRequest;

/**
 *
 * @author twolak
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DeallocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        UUID beerOrderId = UUID.fromString((String) context.getMessage()
                .getHeaders().get(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER));
        Optional<BeerOrder> beerOrderOptional = this.beerOrderRepository.findById(beerOrderId);
        beerOrderOptional.ifPresentOrElse((beerOrder) -> {
            this.jmsTemplate.convertAndSend(JmsConfig.DEALLOCATE_ORDER_QUEUE, DeallocateBeerOrderRequest
                    .builder().beerOrderDto(this.beerOrderMapper.beerOrderToDto(beerOrder)).build());
            log.debug("Sent Deallocation request to queue for order id: " + beerOrder.getId());
        }, () -> {
            String errorMessage = "Beer Order not found! Id: " + beerOrderId.toString();
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        });
    }
}
