package dev.cisnux.learnspringframework.services;

import dev.cisnux.learnspringframework.repositories.CustomerRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CustomerService {
    // not recommended
    @Autowired
    @Qualifier(value = "normalCustomerRepository")
    private CustomerRepository normalCustomerRepository;

    @Autowired
    @Qualifier(value = "premiumCustomerRepository")
    private CustomerRepository premiumCustomerRepository;
}
