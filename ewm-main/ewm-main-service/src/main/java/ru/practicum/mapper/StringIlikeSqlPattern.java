package ru.practicum.mapper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringIlikeSqlPattern {
    public static String makeIlikePattern(String rawString) {
        if (rawString == null || rawString.isBlank()) {
            return null;
        }
        String ilikeString = rawString.trim();
        return "%" + ilikeString + "%";

    }

}
