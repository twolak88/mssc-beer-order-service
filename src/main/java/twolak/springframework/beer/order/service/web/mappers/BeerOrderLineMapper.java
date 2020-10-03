package twolak.springframework.beer.order.service.web.mappers;

import org.mapstruct.Mapper;
import twolak.springframework.beer.order.service.domain.BeerOrderLine;
import twolak.springframework.beer.order.service.web.model.BeerOrderLineDto;

/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class})
public interface BeerOrderLineMapper {
    BeerOrderLineDto beerOrderLineToDto(BeerOrderLine beerOrderLine);
    
    BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto beerOrderLineDto);
}
