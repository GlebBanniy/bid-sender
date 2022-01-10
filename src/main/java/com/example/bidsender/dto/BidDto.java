package com.example.bidsender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class BidDto {

    @NotBlank(message = "Необходимо указать имя")
    private final String userName;

    @NotBlank(message = "Необходимо указать валюту")
    private final String currency;

    @Pattern(regexp = "[0-9]{1,8}\\.[0-9]{2}", message = "Укажите правильный курс, например 72.66")
    private final Double bidValue;

    @NotNull(message = "Активировать заявку? да/нет")
    private final Boolean isActive;
}
