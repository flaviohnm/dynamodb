package br.com.dynamodb.config;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final int TIMEZONE = -3;

    public static final int PLUS_MONTH = 3;

    public static final String TIMEZONE_RECIFE = "America/Recife";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");

}
