package com.phucx.phucxfoodshop.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverterUtils {
    public static LocalDateTime convertToLocalDateTime(Object obj) {
        if (obj instanceof String) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse((String) obj, formatter);
        }
        // Handle other possible object types if needed
        return null;
    }
}
