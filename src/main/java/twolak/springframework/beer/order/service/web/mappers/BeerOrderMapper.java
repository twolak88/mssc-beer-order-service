package twolak.springframework.beer.order.service.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import twolak.springframework.beer.order.service.domain.BeerOrder;
import twolak.springframework.brewery.model.BeerOrderDto;

/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class, BeerOrderLineMapper.class})
public interface BeerOrderMapper {
    
    @Mapping(target = "customerId", source = "customer.id")
    BeerOrderDto beerOrderToDto(BeerOrder beerOrder);
    
    BeerOrder dtoToBeerOrder(BeerOrderDto beerOrderDto);
}
