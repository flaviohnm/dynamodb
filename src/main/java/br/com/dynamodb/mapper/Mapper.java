package br.com.dynamodb.mapper;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.model.Customer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static br.com.dynamodb.config.Constants.*;


public class Mapper {
    public Long toEpocDate(String date) {
        return LocalDateTime
                .parse(date)
                .plusMonths(PLUS_MONTH)
                .toEpochSecond(
                        ZoneOffset
                                .ofHours(TIMEZONE)
                );
    }

    public String toStringDate(Long epocDate) {
        return Instant
                .ofEpochSecond(epocDate)
                .atZone(ZoneId.of(TIMEZONE_RECIFE))
                .toLocalDateTime().format(FORMATTER);

    }

    public String toStringLocalDateTime(String stringDate) {
        return LocalDateTime
                .parse(stringDate)
                .format(FORMATTER);
    }

    public Customer toCreateCustomer(CustomerDTO customerDTO) {
        var customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setCompanyName(customerDTO.getCompanyName());
        customer.setCompanyDocumentNumber(customerDTO.getCompanyDocumentNumber());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setCreateDate(LocalDateTime.now().toString());
        customer.setExpirationDate(toEpocDate(customer.getCreateDate()));
        customer.setActive(true);
        return customer;
    }

    public CustomerDTO toCustomerDTO(Customer customer) {
        var customerDTO = new CustomerDTO();

        customerDTO.setCompanyName(customer.getCompanyName());
        customerDTO.setCompanyDocumentNumber(customer.getCompanyDocumentNumber());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setCreateDate(toStringLocalDateTime(customer.getCreateDate()));
        customerDTO.setExpirationDate(toStringDate(customer.getExpirationDate()));
        customerDTO.setUpdatedDate(customer.getUpdatedDate() != null ? toStringLocalDateTime(customer.getUpdatedDate()) : null);
        customerDTO.setActive(customer.getActive());
        return customerDTO;
    }

    public Customer optionalToUpdateCustomer(Customer customer, CustomerDTO customerDTO) {
        var updatedCustomer = new Customer();

        updatedCustomer.setId(customer.getId());
        updatedCustomer.setCompanyDocumentNumber(customer.getCompanyDocumentNumber());
        updatedCustomer.setCompanyName(customerDTO.getCompanyName());
        updatedCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
        updatedCustomer.setCreateDate(customer.getCreateDate());
        updatedCustomer.setExpirationDate(customer.getExpirationDate());
        updatedCustomer.setUpdatedDate(LocalDateTime.now().toString());
        updatedCustomer.setActive(customer.getActive());
        return updatedCustomer;
    }

    public Customer optionalToDisableCustomer(Customer customer) {
        var updatedCustomer = new Customer();
        updatedCustomer.setId(customer.getId());
        updatedCustomer.setCompanyDocumentNumber(customer.getCompanyDocumentNumber());
        updatedCustomer.setCompanyName(customer.getCompanyName());
        updatedCustomer.setPhoneNumber(customer.getPhoneNumber());
        updatedCustomer.setCreateDate(customer.getCreateDate());
        updatedCustomer.setExpirationDate(customer.getExpirationDate());
        updatedCustomer.setUpdatedDate(LocalDateTime.now().toString());
        updatedCustomer.setActive(false);
        return updatedCustomer;
    }

    public List<CustomerDTO> toCustomerDTOList(List<Customer> customers) {
        List<CustomerDTO> CustomersDTO = new ArrayList<>();
        customers
                .iterator()
                .forEachRemaining(customer -> CustomersDTO.add(toCustomerDTO(customer)));
        return CustomersDTO;
    }
}