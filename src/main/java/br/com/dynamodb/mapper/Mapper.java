package br.com.dynamodb.mapper;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.model.Customer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (customer.getUpdatedDate() != null) {
            customerDTO.setUpdatedDate(
                    toStringLocalDateTime(customer.getUpdatedDate())
            );
        }
        customerDTO.setActive(customer.getActive());

        return customerDTO;
    }

    public Customer optionalToUpdateCustomer(Optional<Customer> optionalCustomer, CustomerDTO customerDTO) {
        var customer = new Customer();

        customer.setId(optionalCustomer.get().getId());
        customer.setCompanyDocumentNumber(optionalCustomer.get().getCompanyDocumentNumber());
        customer.setCompanyName(customerDTO.getCompanyName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setCreateDate(optionalCustomer.get().getCreateDate());
        customer.setExpirationDate(optionalCustomer.get().getExpirationDate());
        customer.setUpdatedDate(LocalDateTime.now().toString());
        customer.setActive(optionalCustomer.get().getActive());

        return customer;
    }

    public Customer optionalToDisableCustomer(Optional<Customer> optionalCustomer) {
        var customer = new Customer();

        customer.setId(optionalCustomer.get().getId());
        customer.setCompanyDocumentNumber(optionalCustomer.get().getCompanyDocumentNumber());
        customer.setCompanyName(optionalCustomer.get().getCompanyName());
        customer.setPhoneNumber(optionalCustomer.get().getPhoneNumber());
        customer.setCreateDate(optionalCustomer.get().getCreateDate());
        customer.setExpirationDate(optionalCustomer.get().getExpirationDate());
        customer.setUpdatedDate(LocalDateTime.now().toString());
        customer.setActive(false);

        return customer;
    }

    public List<CustomerDTO> toCustomerDTOList(List<Customer> customers) {
        List<CustomerDTO> CustomersDTO = new ArrayList<>();
        customers
                .iterator()
                .forEachRemaining(customer -> CustomersDTO.add(toCustomerDTO(customer)));

        return CustomersDTO;
    }

}
