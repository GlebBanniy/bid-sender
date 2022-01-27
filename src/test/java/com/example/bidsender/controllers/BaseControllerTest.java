package com.example.bidsender.controllers;

import com.example.bidsender.base.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class BaseControllerTest extends BaseTest {

    @Autowired
    protected MockMvc mockMvc;

}
