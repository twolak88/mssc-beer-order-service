package twolak.springframework.beer.order.service.web.mappers;

import org.mapstruct.Mapper;
import twolak.springframework.beer.order.service.domain.BeerOrder;
import twolak.springframework.beer.order.service.web.model.BeerOrderDto;

/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class, BeerOrderLineMapper.class})
public interface BeerOrderMapper {
    BeerOrderDto beerOrderToDto(BeerOrder beerOrder);
    
    BeerOrder dtoToBeerOrder(BeerOrderDto beerOrderDto);
}
