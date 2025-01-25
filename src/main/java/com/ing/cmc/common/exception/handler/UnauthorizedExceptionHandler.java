package com.ing.cmc.common.exception.handler;

import com.ing.cmc.common.exception.ExceptionLogger;
import com.ing.cmc.common.exception.UnauthorizedException;
import com.ing.cmc.common.response.CommonResponses;
import com.ing.cmc.common.response.GenericResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Hidden
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UnauthorizedExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(UnauthorizedException e, final HttpServletRequest req) {
        ExceptionLogger.log(e);
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_403)).body(GenericResponse.failure(e.getMessage()));
    }
}