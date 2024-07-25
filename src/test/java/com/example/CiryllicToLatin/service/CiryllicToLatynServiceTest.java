package com.example.CiryllicToLatin.service;

import com.example.CiryllicToLatin.exception.InputValidateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CiryllicToLatynServiceTest {
    @Autowired
    CiryllicToLatynService ciryllicToLatynService;

    @Test
    void convertCiryllic() throws InputValidateException {
        String outputMessage = ciryllicToLatynService.convertCiryllic("аәбвгғдеёжзийкқлмнңоөыпрстуұүфхһцчшщъіьэюяАӘБВГҒДЕЁЖЗИЙКҚЛМНҢОӨПРСТҰУҮФХҺЦЧШЩЪЫІЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789/-");
        Assertions.assertEquals("aabvggdeezhziikqlmnnooyprstuuufhhcchshsh\"i'ejujaAABVGGDEEZHZIIKQLMNNOOPRSTUUUFHHCCHSHSH\"YI'EJUJAabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789/-", outputMessage);
    }
}