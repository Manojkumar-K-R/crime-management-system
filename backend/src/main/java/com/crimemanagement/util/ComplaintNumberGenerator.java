package com.crimemanagement.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class ComplaintNumberGenerator {

    public String generateComplaintNumber(long id) {

        String date = LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE);

        return String.format("CR-%s-%06d", date, id);
    }
}