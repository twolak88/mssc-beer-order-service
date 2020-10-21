package twolak.springframework.beer.order.service.web.model;

/**
 *
 * @author twolak
 */
public enum BeerOrderStatusEnum {
    NEW,
    VALIDATED,
    VALIDATION_EXCEPTION,
    ALLOCATED,
    ALLOCATION_EXCEPTION,
    PENDING_INVENTORY,
    PICKED_UP,
    DELIVERED,
    DELIVERY_EXCEPTION,
    CANCELED
}
