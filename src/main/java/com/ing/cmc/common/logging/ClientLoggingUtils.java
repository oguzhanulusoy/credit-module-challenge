package com.ing.cmc.common.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ClientLoggingUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String getParameters(HttpServletRequest request) {
        Enumeration<?> e = request.getParameterNames();
        try {
            Map<String, Object> logData = new HashMap<>();
            if (e != null) {
                while (e.hasMoreElements()) {
                    String curr = (String) e.nextElement();
                    if (curr.contains("password") || curr.contains("pwd")) {
                        logData.put(curr, "*****");
                    } else {
                        if (!request.getParameter(curr).isEmpty() && !request.getParameter(curr).isBlank()) {
                            logData.put(curr, request.getParameter(curr));
                        }
                    }
                }
            }
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(logData);
        } catch (JsonProcessingException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public String getRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException | HttpMessageNotReadableException ex) {
            System.out.println(ex);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException | HttpMessageNotReadableException ex) {
                    System.out.println(ex);
                }
            }
        }
        return stringBuilder.toString();
    }

}
