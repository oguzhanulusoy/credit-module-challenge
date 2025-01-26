package com.ing.cmc.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClientLoggingInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(ClientLoggingInterceptor.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name()) && request.getInputStream().isFinished()) {
            try {
                Map<String, Object> logData = new HashMap<>();
                logData.put("Method", request.getMethod());
                logData.put("RequestURI", request.getRequestURI());
                logData.put("Payload", ClientLoggingUtils.getParameters(request));
                logData.put("Originator", request.getRemoteUser());
                logData.put("Protocol", request.getProtocol());
                logData.put("Locale", request.getLocale());
                logData.put("Content-Type", request.getContentType());
                String jsonLog = objectMapper.writeValueAsString(logData);
                logger.info("[ClientLoggingInterceptor.preHandle] " + jsonLog);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return true;
    }
}