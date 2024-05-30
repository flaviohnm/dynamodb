package br.com.dynamodb.service.impl;

import br.com.dynamodb.converter.Converter;
import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.model.Customer;
import br.com.dynamodb.repository.CustomerRepository;
import br.com.dynamodb.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public Converter converter = new Converter();

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        if (repository.findByCompanyDocumentNumber(
                        customerDTO.getCompanyDocumentNumber())
                .isPresent()) {
            throw new RuntimeException("There is already a customer with this document number");
        }

        return converter
                .toCustomerDTO(
                        repository.save(
                                converter.toCustomer(customerDTO)
                        ));
    }

    @Override
    public List<CustomerDTO> findAllCustomers() {

        List<CustomerDTO> CustomersDTO = new ArrayList<>();

        var customers = repository.findAll();
        customers
                .iterator().forEachRemaining(customer -> {
                    CustomersDTO.add(converter.toCustomerDTO(customer));
                });

        return CustomersDTO;
    }

    @Override
    public List<CustomerDTO> findByCompanyName(String companyName) {

        return converter.toCustomerDTOList(repository.findByCompanyName(companyName));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        var customer =
                repository.findByCompanyDocumentNumber(customerDTO.getCompanyDocumentNumber());

        if (customer.isEmpty()) {
            throw new RuntimeException("There is no customer with this document number");
        }

        customer.get().setCompanyName(customerDTO.getCompanyName());
        customer.get().setPhoneNumber(customerDTO.getPhoneNumber());

        return converter.toCustomerDTO(repository.save(customer.get()));
    }

    @Override
    public CustomerDTO disableCustomer(String companyDocumentNumber) {
        Optional<Customer> customer =
                repository.findByCompanyDocumentNumber(companyDocumentNumber);

        if (customer.isEmpty()) {
            throw new RuntimeException("There is no customer with this document number");
        }
        customer.get().setActive(false);

        var disabledCostumer = repository.save(customer.get());

        return converter.toCustomerDTO(disabledCostumer);
    }

}
