package com.example.CiryllicToLatin.service;

import com.example.CiryllicToLatin.exception.InputValidateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CiryllicToLatynService {
    @Value("${properties.legalChars}")
    private String legalChars;
    @Value("${properties.abcLat}")
    private String[] abcLat;

    public String convertCiryllic(String message) throws InputValidateException{
        StringBuilder builder = new StringBuilder();
        try{
            message.chars().forEach(c -> {
                if (c == ' ') builder.append(" ");
                else {
                    builder.append(abcLat[legalChars.indexOf((c))]);
                }
            });
        } catch (Exception e){
            throw new InputValidateException();
        }
        return builder.toString();
//        Старый функционал оставил чтобы можно было сравнить
//        int index = 0;
//        while (index < message.length()) {
//            int indexOfChar = legalChars.indexOf(message.charAt(index));
//            if (message.charAt(index) == ' ') {
//                builder.append(" ");
//            } else {
//                if (indexOfChar == -1) {
//                    throw new InputValidateException();
//                }
//                builder.append(abcLat[indexOfChar]);
//            }
//            index++;
//        }
//        return builder.toString();
    }
}
