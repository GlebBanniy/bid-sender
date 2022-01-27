package com.example.bidsender.services;

import com.example.bidsender.exceptions.FileProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BankServiceTest extends BaseServiceTest {

    @Autowired
    private BankService bankService;
    private String line;
    private String line2;
    private String badLine;
    private Bank bank;

    @BeforeEach
    void setUp() {
        line = "bank_name=Spb&currency_from=USD&currency_to=RUB&sum=71.13&timestamp=1642151484;";
        line2 = "bank_name=Spb&currency_from=USD&currency_to=RUB&sum=71.13&timestamp=1642151485;";
        badLine = "bank-name=Spb&currency-from=USD&currency-to=RUB&sum=71.13&timestamp=1642151484;";
        bank = new Bank(
                "Spb",
                "USDRUB",
                71.13,
                1642151485
        );
    }

    @Test
    void parseLine() throws FileProcessingException {
        bankService.parseLine(line);
        bankService.parseLine(line2);
        bankService.sendData();
        List<Bank> banksList = null;
        try {
            var field = bankService.getClass().getDeclaredField("banks");
            field.setAccessible(true);
            banksList = (ArrayList<Bank>)field.get(bankService);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        assert banksList != null;
        assertEquals(bank, banksList.get(0));
    }

    @Test
    void willThrowWhenMatchingError() {
        assertThatThrownBy(() -> bankService.parseLine(badLine))
                .isInstanceOf(FileProcessingException.class)
                .hasMessageContaining("File processing error, invalid data");
    }
}
