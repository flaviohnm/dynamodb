package br.com.dynamodb.repository;

import br.com.dynamodb.model.Customer;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamoDbRepository {

    @Autowired
    DynamoDbTemplate dynamoDbTemplate;

    public List<Customer> findByCompanyDocumentNumber(String companyDocumentNumber) {
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":company_document_number", AttributeValue.fromS(companyDocumentNumber));

        Expression filterExpression = Expression.builder()
                .expression("company_document_number = :company_document_number")
                .expressionValues(expressionValues)
                .build();

        ScanEnhancedRequest scanEnhancedRequest = ScanEnhancedRequest.builder()
                .filterExpression(filterExpression).build();
        PageIterable<Customer> customers = dynamoDbTemplate.scan(scanEnhancedRequest,
                Customer.class);

        return customers
                .stream()
                .toList()
                .getFirst()
                .items();
    }

    public List<Customer> findByCompanyName(String companyName) {
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":company_name", AttributeValue.fromS(companyName));

        Expression filterExpression = Expression.builder()
                .expression("company_name = :company_name")
                .expressionValues(expressionValues)
                .build();

        ScanEnhancedRequest scanEnhancedRequest = ScanEnhancedRequest.builder()
                .filterExpression(filterExpression).build();
        PageIterable<Customer> customers = dynamoDbTemplate.scan(scanEnhancedRequest,
                Customer.class);

        return customers
                .items()
                .stream()
                .toList();
    }

    public List<Customer> findAllCustomers() {
        var customers = dynamoDbTemplate.scanAll(Customer.class);

        return customers
                .items()
                .stream()
                .toList();
    }

}
