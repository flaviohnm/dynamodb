package br.com.dynamodb.converter;

import br.com.dynamodb.dto.CostumerDTO;
import br.com.dynamodb.model.Costumer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");

    public Long toEpocDate(String date) {
        var convertedDate = LocalDateTime.parse(date);
        return convertedDate.plusMonths(3).toEpochSecond(ZoneOffset.ofHours(-3));
    }

    public String toStringDate(Long epocDate) {
        var localDate = Instant.ofEpochSecond(epocDate).atZone(ZoneId.of("America/Recife")).toLocalDateTime();
        return localDate.format(formatter);
    }

    public String toStringLocalDateTime(String stringDate) {
        var localDateTime = LocalDateTime.parse(stringDate);
        return localDateTime.format(formatter);
    }

    public Costumer toCostumer(CostumerDTO costumerDTO) {
        var costumer = new Costumer();

        costumer.setCompanyName(costumerDTO.getCompanyName());
        costumer.setCompanyDocumentNumber(costumerDTO.getCompanyDocumentNumber());
        costumer.setPhoneNumber(costumerDTO.getPhoneNumber());
        costumer.setCreateDate(LocalDateTime.now().toString());
        costumer.setExpirationDate(toEpocDate(costumer.getCreateDate()));
        costumer.setActive(true);

        return costumer;
    }

    public CostumerDTO toCostumerDTO(Costumer costumer) {
        var costumerDTO = new CostumerDTO();

        costumerDTO.setCompanyName(costumer.getCompanyName());
        costumerDTO.setCompanyDocumentNumber(costumer.getCompanyDocumentNumber());
        costumerDTO.setPhoneNumber(costumer.getPhoneNumber());
        costumerDTO.setCreateDate(toStringLocalDateTime(costumer.getCreateDate()));
        costumerDTO.setExpirationDate(toStringDate(costumer.getExpirationDate()));
        costumerDTO.setActive(costumer.getActive());

        return costumerDTO;
    }

    public List<CostumerDTO> toCostumerDTOList(List<Costumer> costumers) {
        List<CostumerDTO> allCostumersDTO = new ArrayList<>();
        costumers
                .iterator().forEachRemaining(costumer -> {
                    allCostumersDTO.add(toCostumerDTO(costumer));
                });
        return allCostumersDTO;
    }

    ;

}
