package twolak.springframework.beer.order.service.services.impl;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import twolak.springframework.beer.order.service.domain.Customer;
import twolak.springframework.beer.order.service.repositories.CustomerRepository;
import twolak.springframework.beer.order.service.services.CustomerService;
import twolak.springframework.beer.order.service.web.mappers.CustomerMapper;
import twolak.springframework.brewery.model.CustomerPagedList;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    
    @Override
    public CustomerPagedList listCustomers(Pageable pageable) {
        Page<Customer> customers = this.customerRepository.findAll(pageable);
        return new CustomerPagedList(customers
                .stream()
                .map(this.customerMapper::customerToDto).collect(Collectors.toList()), 
                    PageRequest.of(customers.getPageable().getPageNumber(),
                    customers.getPageable().getPageSize()),
                    customers.getTotalPages());
    }
    
}
