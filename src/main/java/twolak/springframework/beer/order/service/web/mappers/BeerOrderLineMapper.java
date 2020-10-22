package twolak.springframework.beer.order.service.web.mappers;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import twolak.springframework.beer.order.service.domain.BeerOrderLine;
import twolak.springframework.brewery.model.BeerOrderLineDto;

/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class})
@DecoratedWith(BeerOrderLineMapperDecorator.class)
public interface BeerOrderLineMapper {
    BeerOrderLineDto beerOrderLineToDto(BeerOrderLine beerOrderLine);
    
    BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto beerOrderLineDto);
}
