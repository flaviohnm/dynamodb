package br.com.dynamodb.converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Converter {

    public String toEpocDate(String date){
        var convertedDate = LocalDateTime.parse(date);
        Long expiredAt = convertedDate.plusMonths(3).toEpochSecond(ZoneOffset.ofHours(-3));
        return expiredAt.toString();
    }
}
