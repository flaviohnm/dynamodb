package br.com.dynamodb.service.impl;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.mapper.Mapper;
import br.com.dynamodb.model.Customer;
import br.com.dynamodb.service.CustomerService;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

@Service

public class CustomerServiceImpl implements CustomerService {


    @Autowired
    DynamoDbTemplate dynamoDbTemplate;

    public Mapper mapper = new Mapper();


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        if (findByCompanyDocumentNumber(customerDTO.getCompanyDocumentNumber()).isEmpty()) {
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

        List<CustomerDTO> CustomersDTO = new ArrayList<>();

        var customers = dynamoDbTemplate.scanAll(Customer.class);
        customers
                .stream()
                .forEach(customerPage -> customerPage
                        .items()
                        .iterator()
                        .forEachRemaining(customer -> CustomersDTO.add(mapper.toCustomerDTO(customer))));

        return CustomersDTO;
    }

    @Override
    public List<CustomerDTO> findByCompanyName(String companyName) {
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":company_name", AttributeValue.fromS(companyName));

        Expression filterExpression = Expression.builder()
                .expression("company_name = :company_name")
                .expressionValues(expressionValues)
                .build();

        ScanEnhancedRequest scanEnhancedRequest = ScanEnhancedRequest.builder()
                .filterExpression(filterExpression).build();
        PageIterable<Customer> customerList = dynamoDbTemplate.scan(scanEnhancedRequest,
                Customer.class);

        return mapper.toCustomerDTOList(customerList.items().stream().toList());
    }

    public Optional<Customer> findByCompanyDocumentNumber(String companyDocumentNumber) {
        Key key = Key.builder().partitionValue(companyDocumentNumber).build();
        return Optional.of(dynamoDbTemplate.load(key, Customer.class));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        var recoveryCustomer =
                findByCompanyDocumentNumber(customerDTO.getCompanyDocumentNumber());

        if (recoveryCustomer.isEmpty()){
            throw new RuntimeException("There is no customer with this document number");
        }

        return mapper.toCustomerDTO(
                dynamoDbTemplate.update(
                        mapper.optionalToUpdateCustomer(recoveryCustomer, customerDTO)));
    }

    @Override
    public CustomerDTO disableCustomer(String companyDocumentNumber) {
        Optional<Customer> customer =
                findByCompanyDocumentNumber(companyDocumentNumber);

        if (customer.isEmpty()) {
            throw new RuntimeException("There is no customer with this document number");
        }

        return mapper.toCustomerDTO(
                dynamoDbTemplate.update(
                        mapper.optionalToDisableCustomer(customer)
                ));
    }

}
