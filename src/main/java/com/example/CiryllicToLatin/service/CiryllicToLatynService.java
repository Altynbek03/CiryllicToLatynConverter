package com.example.CiryllicToLatin.service;

import com.example.CiryllicToLatin.exception.InputValidateException;
import io.grpc.stub.StreamObserver;
import kz.paspay.paspaytranslateservice.grpc.CiryllicToLatinGrpc;
import kz.paspay.paspaytranslateservice.grpc.ciryllicToLatin;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@GrpcService
@Slf4j
@EnableConfigurationProperties
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "properties")
public class CiryllicToLatynService extends CiryllicToLatinGrpc.CiryllicToLatinImplBase {
    @Value("${properties.legalChars}")
    private String legalChars;
    @Value("${properties.abcLat}")
    private String[] abcLat;

    @Override
    public void translateCiryllicToLatin(ciryllicToLatin.Request request, StreamObserver<ciryllicToLatin.Response> responseObserver) {
//        log.info("Получено сообщение с текстом: {}", request.getMessage());
        ciryllicToLatin.Response response = getResponse(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private ciryllicToLatin.Response getResponse(ciryllicToLatin.Request request) {
        if (request.getMessage().isEmpty()) {
            var response = setResponseFields("Было получено пустое значение", "1", request.getRequestId());
//            log.error(request.getMessage());
            return response;
        }
        try {
            var response = setResponseFields(convertCyrilic(request.getMessage()), "0", request.getRequestId());
//            log.info(response.getTranslatedMessage());
            return response;

        } catch (InputValidateException e) {
            var response = setResponseFields("Ошибка валидации,были получены некорректные данные", "2", request.getRequestId());
//            log.error(e.getLocalizedMessage());
            return response;
        }
    }

    public String convertCyrilic(String message) throws InputValidateException {
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

    private ciryllicToLatin.Response setResponseFields(String message, String errorCode, String requestId) {
        return ciryllicToLatin.Response.newBuilder()
                                       .setTranslatedMessage(message)
                                       .setErrorCode(errorCode)
                                       .setRequestId(requestId)
                                       .build();
    }

}
