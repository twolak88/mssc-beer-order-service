package twolak.springframework.beer.order.service.services;

import org.springframework.data.domain.Pageable;
import twolak.springframework.brewery.model.CustomerPagedList;

/**
 *
 * @author twolak
 */
public interface CustomerService {
    CustomerPagedList listCustomers(Pageable pageable);
}
