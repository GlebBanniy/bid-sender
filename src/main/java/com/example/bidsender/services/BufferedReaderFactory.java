package com.example.bidsender.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedReaderFactory {

    public static BufferedReader makeBufferedReader(InputStreamReader input) throws IOException {
        return new BufferedReader(input);
    }
}
