package twolak.springframework.beer.order.service.services;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import twolak.springframework.beer.order.service.services.Interceptors.BeerOrderStateChangeInterceptor;
import twolak.springframework.brewery.model.BeerOrderDto;

/**
 *
 * @author twolak
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BeerOrderManagerImpl implements BeerOrderManager {

    public static final String BEER_ORDER_ID_HEADER = "beer_order_id";
    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderStateChangeInterceptor beerOrderStateChangeInterceptor;

    @Transactional
    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {
        beerOrder.setId(null);
        beerOrder.setBeerOrderStatus(BeerOrderStatusEnum.NEW);

        BeerOrder savedBeerOrder = this.beerOrderRepository.saveAndFlush(beerOrder);
        sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATE_ORDER);
        return savedBeerOrder;
    }

    @Transactional
    @Override
    public void processValidationResult(UUID beerOrderId, Boolean isValid) {
        log.debug("Process validation result for beerOrderId: " + beerOrderId.toString());

        this.beerOrderRepository.findById(beerOrderId).ifPresentOrElse((beerOrder) -> {
            if (isValid) {
                sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATION_PASSED);
                
                awaitForStatus(beerOrderId, BeerOrderStatusEnum.VALIDATED);

                BeerOrder validatedBeerOrder = this.beerOrderRepository.findById(beerOrderId).get();
                sendBeerOrderEvent(validatedBeerOrder, BeerOrderEventEnum.ALLOCATE_ORDER);
            } else {
                sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATION_FAILED);
            }
        }, () -> {
            String errorMessage = "Beer Order not found! Id: " + beerOrderId;
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        });
    }

    @Override
    public void beerOrderAllocationPassed(BeerOrderDto beerOrderDto) {
        this.beerOrderRepository.findById(beerOrderDto.getId()).ifPresentOrElse((beerOrder) -> {
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.ALLOCATION_SUCCESS);
            awaitForStatus(beerOrderDto.getId(), BeerOrderStatusEnum.ALLOCATED);
            updateAllocationQuantity(beerOrderDto);
        }, () -> {
            String errorMessage = "Beer Order not found! Id: " + beerOrderDto.getId();
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        });
    }

    @Override
    public void beerOrderAllocationPendingInventory(BeerOrderDto beerOrderDto) {
        this.beerOrderRepository.findById(beerOrderDto.getId()).ifPresentOrElse((beerOrder) -> {
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.ALLOCATION_NO_INVENTORY);
            awaitForStatus(beerOrderDto.getId(), BeerOrderStatusEnum.PENDING_INVENTORY);
            updateAllocationQuantity(beerOrderDto);
        }, () -> {
            String errorMessage = "Beer Order not found! Id: " + beerOrderDto.getId();
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        });
    }

    @Override
    public void beerOrderAllocationFailed(BeerOrderDto beerOrderDto) {
        this.beerOrderRepository.findById(beerOrderDto.getId()).ifPresentOrElse((beerOrder) -> {
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.ALLOCATION_FAILED);
        }, () -> {
            String errorMessage = "Beer Order not found! Id: " + beerOrderDto.getId();
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        });
    }

    @Override
    public void beerOrderPickedUp(UUID beerOrderId) {
        this.beerOrderRepository.findById(beerOrderId).ifPresentOrElse((beerOrder) -> {
            //TODO
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.PICK_UP_ORDER);
        }, () -> {
            String errorMessage = "Beer Order not found! Id: " + beerOrderId;
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        });
    }

    @Override
    public void cancelBeerOrder(UUID beerOrderId) {
        this.beerOrderRepository.findById(beerOrderId).ifPresentOrElse((beerOrder) -> {
            //TODO
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.CANCEL_ORDER);
        }, () -> {
            String errorMessage = "Beer Order not found! Id: " + beerOrderId;
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        });
    }

    private void updateAllocationQuantity(BeerOrderDto beerOrderDto) {
        Optional<BeerOrder> allocatedBeerOrderOptional = this.beerOrderRepository.findById(beerOrderDto.getId());
        allocatedBeerOrderOptional.ifPresentOrElse((allocatedBeerOrder) -> {
            allocatedBeerOrder.getBeerOrderLines().forEach((beerOrderLine) -> {
                beerOrderDto.getBeerOrderLines().forEach((beerOrderLineDto) -> {
                    if (beerOrderLine.getBeerId().equals(beerOrderLineDto.getBeerId())) {
                        beerOrderLine.setQuantityAllocated(beerOrderLineDto.getQuantityAllocated());
                    }
                });
            });
            this.beerOrderRepository.saveAndFlush(allocatedBeerOrder);
        }, () -> {
            String errorMessage = "Beer Order not found! Id: " + beerOrderDto.getId();
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        });
    }

    private void sendBeerOrderEvent(BeerOrder beerOrder, BeerOrderEventEnum beerOrderEventEnum) {
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine = build(beerOrder);
        Message message = MessageBuilder.withPayload(beerOrderEventEnum)
                .setHeader(BEER_ORDER_ID_HEADER, beerOrder.getId().toString())
                .build();
        stateMachine.sendEvent(message);
    }
    
    private void awaitForStatus(UUID beerOrderId, BeerOrderStatusEnum beerOrderStatusEnum) {
        AtomicBoolean found = new AtomicBoolean(false);
        AtomicInteger loopCount = new AtomicInteger(0);
        
        while(!found.get()) {
            if (loopCount.incrementAndGet() > 10) {
                found.set(true);
                log.debug("Loop retries executed");
            }
            this.beerOrderRepository.findById(beerOrderId).ifPresentOrElse(beerOrder -> {
                if (beerOrder.getBeerOrderStatus().equals(beerOrderStatusEnum)) {
                    found.set(true);
                    log.debug("Order found");
                } else {
                    log.debug("Order Status not matched. Expected: " + beerOrderStatusEnum.name() + " found: " + beerOrder.getBeerOrderStatus().name());
                }
            }, () -> {
                log.debug("Order Not found! Id: " + beerOrderId);
            });
            if (!found.get()) {
                try {
                    log.debug("Sleeping for retry.");
                    Thread.sleep(100);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> build(BeerOrder beerOrder) {
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine = this.stateMachineFactory.getStateMachine(beerOrder.getId());
        stateMachine.stop();

        stateMachine.getStateMachineAccessor().doWithAllRegions(stateMachineAccessor -> {
            stateMachineAccessor.addStateMachineInterceptor(beerOrderStateChangeInterceptor);
            stateMachineAccessor.resetStateMachine(new DefaultStateMachineContext<>(beerOrder.getBeerOrderStatus(), null, null, null));
        });
        stateMachine.start();
        return stateMachine;
    }
}
