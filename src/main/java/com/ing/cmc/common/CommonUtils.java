package com.ing.cmc.common;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;

public class CommonUtils {

    public static Locale getLocaleFromRequest(final HttpServletRequest req) {
        String reqLocale = "en-us";
        if (neitherEmptyNorBlank(req.getHeader("Accept-Language"))) {
            reqLocale = req.getHeader("Accept-Language");
        }
        String[] localeInfos = {"", "", ""};
        int i = 0;
        for (String reqLocaleInfo : reqLocale.split("-")) {
            localeInfos[i] = reqLocaleInfo;
            i++;
        }
        return new Locale(localeInfos[0], localeInfos[1], localeInfos[2]);
    }

    public static boolean neitherEmptyNorBlank(String s) {
        return s != null && !s.isEmpty() && !s.isBlank();
    }

}
