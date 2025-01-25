package com.ing.cmc.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {
    private boolean success;
    private String message;
    @Nullable
    private T data;

    public static <T> GenericResponse<T> success(T data) {
        return GenericResponse.<T>builder()
                .message("Successful!")
                .data(data)
                .success(true)
                .build();
    }

    public static <T> GenericResponse<T> success() {
        return GenericResponse.<T>builder()
                .message("Successful!")
                .success(true)
                .build();
    }

    public static <T> GenericResponse<T> failure(String message) {
        return GenericResponse.<T>builder()
                .message(message.trim())
                .success(false)
                .build();
    }

    public static <T> GenericResponse<T> failure() {
        return GenericResponse.<T>builder()
                .message("Failed!")
                .success(false)
                .build();
    }
}
