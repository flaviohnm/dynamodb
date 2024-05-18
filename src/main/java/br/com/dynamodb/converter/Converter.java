package br.com.dynamodb.converter;

import br.com.dynamodb.dto.CostumerDTO;
import br.com.dynamodb.model.Costumer;

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
                .iterator()
                .forEachRemaining(costumer -> {
                    allCostumersDTO.add(toCostumerDTO(costumer));
                });

        return allCostumersDTO;
    }

}
