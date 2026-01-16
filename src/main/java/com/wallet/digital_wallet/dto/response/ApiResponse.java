package com.wallet.digital_wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard API response wrapper.
 *
 * <p>All controllers return {@code ApiResponse<T>} to provide consistent response shape
 * across success and error cases.
 *
 * @param <T> payload type
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ApiResponse<T>{
    private boolean success;
    private String message;
    private T data;

    /**
     * Server timestamp for the response.
     */
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}