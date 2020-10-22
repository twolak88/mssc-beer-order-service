package twolak.springframework.beer.order.service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twolak.springframework.beer.order.service.domain.BeerOrder;
import twolak.springframework.beer.order.service.domain.BeerOrderEventEnum;
import twolak.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import twolak.springframework.beer.order.service.repositories.BeerOrderRepository;

/**
 *
 * @author twolak
 */
@RequiredArgsConstructor
@Service
public class BeerOrderManagerImpl implements BeerOrderManager {

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;
    
    @Transactional
    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {
        beerOrder.setId(null);
        beerOrder.setBeerOrderStatus(BeerOrderStatusEnum.NEW);
        
        BeerOrder savedBeerOrder = this.beerOrderRepository.save(beerOrder);
        sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATE_ORDER);
        return savedBeerOrder;
    }
    
    
    private void sendBeerOrderEvent(BeerOrder beerOrder, BeerOrderEventEnum beerOrderEventEnum) {
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine = build(beerOrder);
        Message message = MessageBuilder.withPayload(beerOrderEventEnum)
                .build();
        stateMachine.sendEvent(message);
    }
    
    private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> build(BeerOrder beerOrder) {
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine = this.stateMachineFactory.getStateMachine(beerOrder.getId());
        stateMachine.stop();
        
        stateMachine.getStateMachineAccessor().doWithAllRegions(stateMachineAccessor -> {
            stateMachineAccessor.resetStateMachine(new DefaultStateMachineContext<>(beerOrder.getBeerOrderStatus(), null, null, null));
        });
        stateMachine.start();
        return stateMachine;
    }
}
