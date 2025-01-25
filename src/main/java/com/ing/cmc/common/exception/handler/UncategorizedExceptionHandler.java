package com.ing.cmc.common.exception.handler;

import com.ing.cmc.common.exception.ExceptionLogger;
import com.ing.cmc.common.response.CommonResponses;
import com.ing.cmc.common.response.GenericResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Hidden
@ControllerAdvice
public class UncategorizedExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleUncategorizedException(Exception e) {
        ExceptionLogger.log(e);
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_500)).body(GenericResponse.failure(e.getMessage()));
    }
}
