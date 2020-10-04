package twolak.springframework.beer.order.service.web.controllers;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import twolak.springframework.beer.order.service.services.BeerOrderService;
import twolak.springframework.beer.order.service.web.model.BeerOrderDto;
import twolak.springframework.beer.order.service.web.model.BeerOrderPagedList;

/**
 *
 * @author twolak
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers/{customerId}")
@RestController
public class BeerOrderController {
    
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 20;
    
    private final BeerOrderService beerOrderService;
    
    @GetMapping("orders")
    public BeerOrderPagedList listOrders(@PathVariable("customerId") UUID customerId,
                                        @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return this.beerOrderService.listOrders(customerId, PageRequest.of(pageNumber, pageSize));
    }
    
    @PostMapping("orders")
    @ResponseStatus(HttpStatus.CREATED)
    public BeerOrderDto placeOrder(@PathVariable("customerId") UUID customerId, @RequestBody BeerOrderDto beerOrderDto) {
        return this.beerOrderService.placeOrder(customerId, beerOrderDto);
    }
    
    @GetMapping("orders/{orderId}")
    public BeerOrderDto getOrder(@PathVariable("customerId") UUID customerId, @PathVariable("orderId") UUID orderId) {
        return this.beerOrderService.getOrderById(customerId, orderId);
    }
    
    @PutMapping("orders/{orderId}/pickup")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pickupOrder(@PathVariable("customerId") UUID customerId, @PathVariable("orderId") UUID orderId) {
        this.beerOrderService.pickupOrder(customerId, orderId);
    }
}
