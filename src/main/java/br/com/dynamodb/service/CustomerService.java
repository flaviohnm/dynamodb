package br.com.dynamodb.service;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> findAllCustomers();

    List<CustomerDTO> findByCompanyName(String companyName);

    Optional<Customer> findByCompanyDocumentNumber(String companyDocumentNumber);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    CustomerDTO disableCustomer(String companyDocumentNumber);
}