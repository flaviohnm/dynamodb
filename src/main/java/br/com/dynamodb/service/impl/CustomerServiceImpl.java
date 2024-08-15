package br.com.dynamodb.service.impl;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.mapper.Mapper;
import br.com.dynamodb.model.Customer;
import br.com.dynamodb.repository.CustomerRepository;
import br.com.dynamodb.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class CustomerServiceImpl implements CustomerService {

    public Mapper mapper = new Mapper();

    private final CustomerRepository repository;

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

        return mapper
                .toCustomerDTO(
                        repository.save(
                                mapper.toCreateCustomer(customerDTO)
                        ));

    }

    @Override
    public List<CustomerDTO> findAllCustomers() {

        List<CustomerDTO> CustomersDTO = new ArrayList<>();

        var customers = repository.findAll();
        customers
                .iterator()
                .forEachRemaining(customer -> CustomersDTO.add(mapper.toCustomerDTO(customer)));

        return CustomersDTO;
    }

    @Override
    public List<CustomerDTO> findByCompanyName(String companyName) {

        return mapper.toCustomerDTOList(repository.findByCompanyName(companyName));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        var customer =
                repository.findByCompanyDocumentNumber(customerDTO.getCompanyDocumentNumber());

        if (customer.isEmpty()) {
            throw new RuntimeException("There is no customer with this document number");
        }

        return mapper.toCustomerDTO(
                repository.save(
                        mapper.optionalToUpdateCustomer(customer, customerDTO)));
    }

    @Override
    public CustomerDTO disableCustomer(String companyDocumentNumber) {
        Optional<Customer> customer =
                repository.findByCompanyDocumentNumber(companyDocumentNumber);

        if (customer.isEmpty()) {
            throw new RuntimeException("There is no customer with this document number");
        }

        return mapper.toCustomerDTO(
                repository.save(
                        mapper.optionalToDisableCustomer(customer)
                ));
    }

}
