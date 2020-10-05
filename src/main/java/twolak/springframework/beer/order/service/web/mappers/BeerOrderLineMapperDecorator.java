package twolak.springframework.beer.order.service.web.mappers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import twolak.springframework.beer.order.service.domain.BeerOrderLine;
import twolak.springframework.beer.order.service.services.beer.BeerService;
import twolak.springframework.beer.order.service.web.model.BeerDto;
import twolak.springframework.beer.order.service.web.model.BeerOrderLineDto;

/**
 *
 * @author twolak
 */
public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper {
    private BeerService beerService;
    private BeerOrderLineMapper beerOrderLineMapper;

    @Autowired
    public void setBeerService(BeerService beerService) {
        this.beerService = beerService;
    }

    @Autowired
    @Qualifier("delegate")
    public void setBeerOrderLineMapper(BeerOrderLineMapper beerOrderLineMapper) {
        this.beerOrderLineMapper = beerOrderLineMapper;
    }

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine beerOrderLine) {
        BeerOrderLineDto beerOrderLineDto = this.beerOrderLineMapper.beerOrderLineToDto(beerOrderLine);
        Optional<BeerDto> beerDtoOptional = this.beerService.getBeerByUpc(beerOrderLine.getUpc());
        
        beerDtoOptional.ifPresent(beerDto -> {
            beerOrderLineDto.setBeerName(beerDto.getBeerName());
            beerOrderLineDto.setBeerStyle(beerDto.getBeerStyle());
            beerOrderLineDto.setPrice(beerDto.getPrice());
        });
        return beerOrderLineDto;
    }
}
