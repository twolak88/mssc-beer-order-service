package twolak.springframework.beer.order.service.web.mappers;

import org.mapstruct.Mapper;
import twolak.springframework.beer.order.service.domain.Customer;
import twolak.springframework.brewery.model.CustomerDto;

/**
 *
 * @author twolak
 */
@Mapper(uses = {DateMapper.class})
public interface CustomerMapper {
    CustomerDto customerToDto(Customer customer);
    Customer dtoToCustomer(CustomerDto customerDto);
}
