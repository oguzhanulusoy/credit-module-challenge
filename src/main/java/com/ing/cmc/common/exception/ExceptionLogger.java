package com.ing.cmc.common.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class ExceptionLogger {
    private static final boolean IS_EXCEPTION_LOGGING = true; // TODO

    public static void log(Exception e) {
        if (IS_EXCEPTION_LOGGING) {
            log.error("{}\n{}",
                    e.toString(),
                    Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n\t")));
        }
    }
}