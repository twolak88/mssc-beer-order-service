package twolak.springframework.beer.order.service.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import twolak.springframework.beer.order.service.domain.Customer;

/**
 *
 * @author twolak
 */
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findAllByCustomerNameLike(String customerName);
}
