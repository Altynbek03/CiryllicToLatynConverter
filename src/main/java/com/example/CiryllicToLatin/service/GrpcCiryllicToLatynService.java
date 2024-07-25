package com.example.CiryllicToLatin.service;

import com.example.CiryllicToLatin.exception.InputValidateException;
import io.grpc.stub.StreamObserver;
import kz.paspay.paspaytranslateservice.grpc.CiryllicToLatinGrpc;
import kz.paspay.paspaytranslateservice.grpc.ciryllicToLatin;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@GrpcService
public class GrpcCiryllicToLatynService extends CiryllicToLatinGrpc.CiryllicToLatinImplBase {
    @Autowired
    CiryllicToLatynService ciryllicToLatynService;

    @Override
    public void translateCiryllicToLatin(ciryllicToLatin.Request request, StreamObserver<ciryllicToLatin.Response> responseObserver) {
        log.info("Получено сообщение с текстом: {}", request.getMessage());
        ciryllicToLatin.Response response = getResponse(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private ciryllicToLatin.Response getResponse(ciryllicToLatin.Request request) {
        if (request.getMessage().isEmpty()) {
            var response = setResponseFields("Было получено пустое значение", "1", request.getRequestId(),false);
            log.error(request.getMessage());
            return response;
        }
        try {
            var response = setResponseFields(ciryllicToLatynService.convertCiryllic(request.getMessage()), "0", request.getRequestId(),true);
            log.info(response.getTranslatedMessage());
            return response;

        } catch (InputValidateException e) {
            var response = setResponseFields("Ошибка валидации,были получены некорректные данные", "2", request.getRequestId(),false);
            log.error(e.getLocalizedMessage());
            return response;
        }
    }

    private ciryllicToLatin.Response setResponseFields(String message, String errorCode, String requestId,boolean status) {
        return ciryllicToLatin.Response.newBuilder()
                                       .setTranslatedMessage(message)
                                       .setErrorCode(errorCode)
                                       .setRequestId(requestId)
                                       .setSuccess(status)
                                       .build();
    }

}
