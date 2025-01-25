package com.ing.cmc.common.response.doc;

import com.ing.cmc.common.response.CommonResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CmcSuccessApiResponses_200_204 {
    ApiResponse[] value() default {
            @ApiResponse(responseCode = CommonResponses.CODE_204, description = CommonResponses.DESC_204, content = @Content),
            @ApiResponse(responseCode = CommonResponses.CODE_200, description = CommonResponses.DESC_200)
    };
}
