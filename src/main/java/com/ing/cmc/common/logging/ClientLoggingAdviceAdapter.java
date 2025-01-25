package com.ing.cmc.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ClientLoggingAdviceAdapter extends RequestBodyAdviceAdapter implements ResponseBodyAdvice<Object> {
    @Autowired
    HttpServletRequest httpServletRequest;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("[ClientLoggingAdviceAdapter.handleEmtpyBody]");
        return super.handleEmptyBody(body, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            String requestBody = body.toString();
            String payloadRequest = !requestBody.isEmpty() ? requestBody : ClientLoggingUtils.getParameters(httpServletRequest);
            Map<String, Object> logData = new HashMap<>();
            logData.put("Method", httpServletRequest.getMethod());
            logData.put("RequestURI", httpServletRequest.getRequestURI());
            logData.put("Payload", payloadRequest);
            logData.put("Originator", httpServletRequest.getRemoteUser());
            logData.put("Protocol", httpServletRequest.getProtocol());
            logData.put("Locale", httpServletRequest.getLocale());
            logData.put("Content-Type", httpServletRequest.getContentType());
            String jsonLog = objectMapper.writeValueAsString(logData);
            log.info("[ClientLoggingAdviceAdapter.afterBodyRead] " + jsonLog);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (serverHttpRequest instanceof ServletServerHttpRequest && serverHttpResponse instanceof ServletServerHttpResponse) {
            log.info("[ClientLoggingAdviceAdapter.beforeBodyWrite] Status: " + String.valueOf(((ServletServerHttpResponse) serverHttpResponse).getServletResponse().getStatus()));
        }
        return o;
    }
}