package com.test.forleven.infra.config.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String obterDataHoraAtualFormatada() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(FORMATTER);
    }

    public static LocalDateTime converterStringParaLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }
}
