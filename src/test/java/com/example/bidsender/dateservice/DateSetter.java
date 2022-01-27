package com.example.bidsender.dateservice;

import java.time.LocalDateTime;

public class DateSetter {

    public static LocalDateTime setDate(int day){
        return LocalDateTime.of(
                2022,
                1,
                day,
                0,
                0,
                0);
    }
}
