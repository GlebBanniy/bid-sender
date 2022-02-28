package com.example.bidsender.services;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bank {
    private String bankName;
    private String currency;
    private double sum;
    private int timestamp;
}
