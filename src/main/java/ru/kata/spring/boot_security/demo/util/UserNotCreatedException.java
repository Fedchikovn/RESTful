package ru.kata.spring.boot_security.demo.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserNotCreatedException extends RuntimeException implements UserCustomException {
    public UserNotCreatedException(String msg) {
        super(msg);
    }

    @Override
    public String getTimeStamp() {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xxx"));
    }
}
