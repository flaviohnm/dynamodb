package br.com.dynamodb.converter;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.model.Customer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static br.com.dynamodb.config.Constants.*;

public class Converter {

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

    public Customer toCustomer(CustomerDTO customerDTO) {
        var costumer = new Customer();

        costumer.setCompanyName(customerDTO.getCompanyName());
        costumer.setCompanyDocumentNumber(customerDTO.getCompanyDocumentNumber());
        costumer.setPhoneNumber(customerDTO.getPhoneNumber());
        costumer.setCreateDate(LocalDateTime.now().toString());
        costumer.setExpirationDate(toEpocDate(costumer.getCreateDate()));
        costumer.setActive(true);

        return costumer;
    }

    public CustomerDTO toCustomerDTO(Customer customer) {
        var costumerDTO = new CustomerDTO();

        costumerDTO.setCompanyName(customer.getCompanyName());
        costumerDTO.setCompanyDocumentNumber(customer.getCompanyDocumentNumber());
        costumerDTO.setPhoneNumber(customer.getPhoneNumber());
        costumerDTO.setCreateDate(toStringLocalDateTime(customer.getCreateDate()));
        costumerDTO.setExpirationDate(toStringDate(customer.getExpirationDate()));
        costumerDTO.setActive(customer.getActive());

        return costumerDTO;
    }

    public List<CustomerDTO> toCustomerDTOList(List<Customer> customers) {
        List<CustomerDTO> CostumersDTO = new ArrayList<>();
        customers
                .iterator()
                .forEachRemaining(customer -> {
                    CostumersDTO.add(toCustomerDTO(customer));
                });

        return CostumersDTO;
    }

}
