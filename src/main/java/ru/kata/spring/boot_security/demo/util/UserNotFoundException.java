package ru.kata.spring.boot_security.demo.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserNotFoundException extends RuntimeException implements UserCustomException{
    public UserNotFoundException(String errorMsg) {
        super(errorMsg);
    }

    @Override
    public String getTimeStamp() {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xxx"));
    }
}
