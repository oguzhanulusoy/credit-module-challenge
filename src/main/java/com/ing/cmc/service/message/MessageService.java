package com.ing.cmc.service.message;

import com.ing.cmc.common.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    HttpServletRequest req;

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, CommonUtils.getLocaleFromRequest(req));
    }
}
