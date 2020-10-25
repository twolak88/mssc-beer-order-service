package twolak.springframework.beer.order.service.services.beer.impl;

import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import twolak.springframework.beer.order.service.services.beer.BeerService;
import twolak.springframework.brewery.model.BeerDto;

/**
 *
 * @author twolak
 */
@ConfigurationProperties(prefix = "tw.brewery", ignoreInvalidFields = false)
@Service
public class BeerServiceImpl implements BeerService {
    
    public static final String BEER_PATH_V1 = "/api/v1/beer/";
    public static final String BEER_UPC_PATH_V1 = "/api/v1/beerUpc/";
    
    private final RestTemplate restTemplate;
    private String beerServiceHost;
    
    public BeerServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    
    public void setBeerServiceHost(String beerServiceHost) {
        this.beerServiceHost = beerServiceHost;
    }
    
    @Override
    public Optional<BeerDto> getBeerById(UUID beerId) {
        return Optional.of(this.restTemplate.getForObject(beerServiceHost + BEER_PATH_V1 + beerId, BeerDto.class));
    }

    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {
        return Optional.of(this.restTemplate.getForObject(this.beerServiceHost + BEER_UPC_PATH_V1 + upc, BeerDto.class));
    }
    
}
