package br.com.dynamodb.service;

import br.com.dynamodb.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> findAllCustomers();

    List<CustomerDTO> findByCompanyName(String companyName);

    CustomerDTO findCompanyNameByQuery(String companyName);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    CustomerDTO disableCustomer(String companyDocumentNumber);
}