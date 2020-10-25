package twolak.springframework.beer.order.service.domain;

/**
 *
 * @author twolak
 */
public enum BeerOrderEventEnum {
    VALIDATE_ORDER,
    VALIDATION_PASSED,
    VALIDATION_FAILED,
    ALLOCATE_ORDER,
    ALLOCATION_SUCCESS,
    ALLOCATION_NO_INVENTORY,
    ALLOCATION_FAILED,
    PICK_UP_ORDER,
    CANCEL_ORDER
    
}
