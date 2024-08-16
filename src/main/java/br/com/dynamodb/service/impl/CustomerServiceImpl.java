package br.com.dynamodb.service.impl;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.mapper.Mapper;
import br.com.dynamodb.model.Customer;
import br.com.dynamodb.repository.DynamoDbRepository;
import br.com.dynamodb.service.CustomerService;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private DynamoDbRepository repository;

    @Autowired
    DynamoDbTemplate dynamoDbTemplate;

    public Mapper mapper = new Mapper();


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        var recoveryCustomer =
                repository.findByCompanyDocumentNumber(customerDTO.getCompanyDocumentNumber());

        if (recoveryCustomer.isPresent()) {
            throw new RuntimeException("There is already a customer with this document number");
        }

        return mapper
                .toCustomerDTO(
                        dynamoDbTemplate.save(
                                mapper.toCreateCustomer(customerDTO)
                        ));
    }

    @Override
    public List<CustomerDTO> findAllCustomers() {
        return mapper.toCustomerDTOList(repository.findAllCustomers());
    }

    @Override
    public List<CustomerDTO> findByCompanyName(String companyName) {
        return mapper.toCustomerDTOList(repository.findByCompanyName(companyName));
    }

    @Override
    public Optional<Customer> findByCompanyDocumentNumber(String companyDocumentNumber) {
        return repository.findByCompanyDocumentNumber(companyDocumentNumber);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        var recoveryCustomer =
                repository.findByCompanyDocumentNumber(customerDTO.getCompanyDocumentNumber());

        if (recoveryCustomer.isEmpty()) {
            throw new RuntimeException("There is no customer with this document number");
        }

        return mapper.toCustomerDTO(
                dynamoDbTemplate.update(
                        mapper.optionalToUpdateCustomer(recoveryCustomer, customerDTO)));
    }

    @Override
    public CustomerDTO disableCustomer(String companyDocumentNumber) {
        var recoveryCustomer =
                repository.findByCompanyDocumentNumber(companyDocumentNumber);

        if (recoveryCustomer.isEmpty()) {
            throw new RuntimeException("There is no customer with this document number");
        }

        return mapper.toCustomerDTO(
                dynamoDbTemplate.update(
                        mapper.optionalToDisableCustomer(recoveryCustomer)
                ));
    }

}
