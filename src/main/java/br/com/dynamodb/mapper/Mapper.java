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
        var date = LocalDateTime.now().toString();

        return Customer.builder()
                .id(UUID.randomUUID().toString())
                .companyName(customerDTO.getCompanyName())
                .companyDocumentNumber(customerDTO.getCompanyDocumentNumber())
                .phoneNumber(customerDTO.getPhoneNumber())
                .createDate(date)
                .expirationDate(toEpocDate(date))
                .active(true)
                .build();
    }

    public CustomerDTO toCustomerDTO(Customer customer) {

        return CustomerDTO.builder()
                .companyName(customer.getCompanyName())
                .companyDocumentNumber(customer.getCompanyDocumentNumber())
                .phoneNumber(customer.getPhoneNumber())
                .createDate(toStringLocalDateTime(customer.getCreateDate()))
                .expirationDate(toStringDate(customer.getExpirationDate()))
                .updatedDate(customer.getUpdatedDate() != null ? toStringLocalDateTime(customer.getUpdatedDate()) : null)
                .active(customer.getActive())
                .build();
    }

    public Customer optionalToCustomer(Optional<Customer> optCostumer) {
        return Customer.builder()
                .id(optCostumer.get().getId())
                .companyName(optCostumer.get().getCompanyName())
                .companyDocumentNumber(optCostumer.get().getCompanyDocumentNumber())
                .companyName(optCostumer.get().getCompanyName())
                .phoneNumber(optCostumer.get().getPhoneNumber())
                .createDate(optCostumer.get().getCreateDate())
                .expirationDate(optCostumer.get().getExpirationDate())
                .updatedDate(optCostumer.get().getUpdatedDate())
                .active(optCostumer.get().getActive())
                .build();
    }

    public Customer optionalToUpdateCustomer(Customer customer, CustomerDTO customerDTO) {
        return Customer.builder()
                .id(customer.getId())
                .companyName(customer.getCompanyName())
                .companyDocumentNumber(customer.getCompanyDocumentNumber())
                .companyName(customerDTO.getCompanyName())
                .phoneNumber(customerDTO.getPhoneNumber())
                .createDate(customer.getCreateDate())
                .expirationDate(customer.getExpirationDate())
                .updatedDate(LocalDateTime.now().toString())
                .active(true)
                .build();
    }

    public Customer optionalToDisableCustomer(Customer customer) {
        return Customer.builder()
                .id(customer.getId())
                .companyName(customer.getCompanyName())
                .companyDocumentNumber(customer.getCompanyDocumentNumber())
                .companyName(customer.getCompanyName())
                .phoneNumber(customer.getPhoneNumber())
                .createDate(customer.getCreateDate())
                .expirationDate(customer.getExpirationDate())
                .updatedDate(LocalDateTime.now().toString())
                .active(false)
                .build();
    }

    public List<CustomerDTO> toCustomerDTOList(List<Customer> customers) {
        List<CustomerDTO> CustomersDTO = new ArrayList<>();
        customers
                .iterator()
                .forEachRemaining(customer -> CustomersDTO.add(toCustomerDTO(customer)));
        return CustomersDTO;
    }
}