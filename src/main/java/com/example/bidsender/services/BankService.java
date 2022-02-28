package com.example.bidsender.services;

import com.example.bidsender.dto.BidDto;
import com.example.bidsender.exceptions.FileProcessingException;
import com.example.bidsender.rabbitmq.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BankService {

    private final Producer messageProducer;
    private final List<Bank> banks;
    private final Pattern pattern;

    @Autowired
    public BankService(Producer messageProducer, @Value("${pattern}") String patternString) {
        this.messageProducer = messageProducer;
        this.banks = new ArrayList<>();
        this.pattern = Pattern.compile(patternString);
    }

    public void parseLine(String line) throws FileProcessingException {
        Matcher m = pattern.matcher(line);
        if (!m.find()) {
            throw new FileProcessingException("File processing error, invalid data");
        } else {
            var rawBankData = setRawBankData(m);
            if (117250000 < rawBankData.getTimestamp()) {
                findDestination(rawBankData);
            }
        }
    }

    private void findDestination(Bank rawBankData) {
        if (banks.stream().anyMatch(x -> x.getBankName().equals(rawBankData.getBankName()))) {
            if (banks.removeIf(x -> x.getBankName().equals(rawBankData.getBankName()) && x.getTimestamp() < rawBankData.getTimestamp()))
                banks.add(rawBankData);
        }
        else
            banks.add(rawBankData);
    }

    private Bank setRawBankData(Matcher matcher) {
        return new Bank(
                matcher.group(1),
                matcher.group(2) + matcher.group(3),
                Double.parseDouble(matcher.group(4)),
                Integer.parseInt(matcher.group(5))
        );
    }

    public void sendData() {
        banks.forEach(bank -> messageProducer.publishMessage(convertToDto(bank)));
    }

    private BidDto convertToDto(Bank bank) {
        return BidDto.builder()
                .userName(bank.getBankName())
                .currency(bank.getCurrency())
                .bidValue(bank.getSum())
                .isActive(true)
                .build();
    }
}
