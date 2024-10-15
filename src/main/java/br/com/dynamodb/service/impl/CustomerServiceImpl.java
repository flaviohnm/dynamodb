package br.com.dynamodb.service.impl;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.exceptions.ResourceNotFoundException;
import br.com.dynamodb.exceptions.UnprocessableEntityException;
import br.com.dynamodb.mapper.Mapper;
import br.com.dynamodb.repository.DynamoDbRepository;
import br.com.dynamodb.service.CustomerService;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private DynamoDbRepository repository;

    @Autowired
    DynamoDbTemplate dynamoDbTemplate;

    public Mapper mapper = new Mapper();

    private static final String CUSTOMER_IS_ALREADY = "There is already a customer with this document number";
    private static final String CUSTOMER_IS_NOT_EXISTS = "There is not customer with this document number";

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        var recoveryListCustomer =
                repository.findByCompanyDocumentNumber(customerDTO.getCompanyDocumentNumber());

        if (!recoveryListCustomer.isEmpty()) {
            throw new UnprocessableEntityException(CUSTOMER_IS_ALREADY);
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
    public CustomerDTO findCompanyNameByQuery(String companyName) {
        var recoveredCustomer = repository.findCompanyNameByQuery(companyName);

        if (recoveredCustomer.isEmpty()) {
            throw new ResourceNotFoundException(CUSTOMER_IS_NOT_EXISTS);
        }

        return mapper
                .toCustomerDTO(mapper
                        .optionalToCustomer(repository
                                .findCompanyNameByQuery(companyName)));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        var recoveryListCustomer =
                repository.findByCompanyDocumentNumber(customerDTO.getCompanyDocumentNumber());

        if (recoveryListCustomer.isEmpty()) {
            throw new ResourceNotFoundException(CUSTOMER_IS_NOT_EXISTS);
        }

        var recoveryCustomer = recoveryListCustomer.stream().toList().getFirst();

        return mapper.toCustomerDTO(
                dynamoDbTemplate.update(
                        mapper.optionalToUpdateCustomer(recoveryCustomer, customerDTO)));
    }

    @Override
    public CustomerDTO disableCustomer(String companyDocumentNumber) {
        var recoveryListCustomer =
                repository.findByCompanyDocumentNumber(companyDocumentNumber);

        if (recoveryListCustomer.isEmpty()) {
            throw new ResourceNotFoundException(CUSTOMER_IS_NOT_EXISTS);
        }

        var recoveryCustomer = recoveryListCustomer.stream().toList().getFirst();

        return mapper.toCustomerDTO(
                dynamoDbTemplate.update(
                        mapper.optionalToDisableCustomer(recoveryCustomer)
                ));
    }

}
