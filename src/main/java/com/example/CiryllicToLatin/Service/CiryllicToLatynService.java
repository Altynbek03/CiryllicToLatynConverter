package com.example.CiryllicToLatin.Service;

import com.example.CiryllicToLatin.Exception.InputValidateException;
import io.grpc.stub.StreamObserver;
import kz.paspay.paspaytranslateservice.grpc.CiryllicToLatinGrpc;
import kz.paspay.paspaytranslateservice.grpc.ciryllicToLatin;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@GrpcService
@Slf4j
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "properties")
public class CiryllicToLatynService extends CiryllicToLatinGrpc.CiryllicToLatinImplBase {
    private static final Logger logger = LoggerFactory.getLogger(Slf4j.class);
    @Value("${properties.legalChars}")
    private String legalChars;
    @Value("${properties.abcLat}")
    private String[] abcLat;

    @Override
    public void translateCiryllicToLatin(ciryllicToLatin.Request request, StreamObserver<ciryllicToLatin.Response> responseObserver) {
        logger.info("Получено сообщение с текстом: {}", request.getMessage());
        ciryllicToLatin.Response response = getResponse(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private ciryllicToLatin.Response getResponse(ciryllicToLatin.Request request) {
        if (request.getMessage().isEmpty()) {
            var response = setResponseFields("Было получено пустое значение", "1", request.getRequestId());
            logger.error(request.getMessage());
            return response;
        }
        try {
            var response = setResponseFields(convertCyrilic(request.getMessage()), "0", request.getRequestId());
            logger.info(response.getTranslatedMessage());
            return response;

        } catch (InputValidateException e) {
            var response = setResponseFields("Ошибка валидации,были получены некорректные данные", "2", request.getRequestId());
            logger.error(e.getLocalizedMessage());
            return response;
        }
    }

    public String convertCyrilic(String message) throws InputValidateException {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        while (index < message.length()) {
            int indexOfChar = legalChars.indexOf(message.charAt(index));
            if (message.charAt(index) == ' ') {
                builder.append(" ");
            } else {
                if (indexOfChar == -1) {
                    throw new InputValidateException();
                }
                builder.append(abcLat[indexOfChar]);
            }
            index++;
        }
        return builder.toString();
    }

    private ciryllicToLatin.Response setResponseFields(String message, String errorCode, String requestId) {
        return ciryllicToLatin.Response.newBuilder()
                                       .setTranslatedMessage(message)
                                       .setErrorCode(errorCode)
                                       .setRequestId(requestId)
                                       .build();
    }

}
