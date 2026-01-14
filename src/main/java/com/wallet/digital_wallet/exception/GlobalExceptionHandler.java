package com.wallet.digital_wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
     @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND)
                 .body(ApiResponse.<Void>builder()
                         .success(false)
                         .message(ex.getMessage())
                         .timestamp(LocalDateTime.now())
                         .build());
     }

     @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse<Void>> handleInsufficientBalance(InsufficientBalanceException ex) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                 .body(ApiResponse.<Void>builder()
                         .success(false)
                         .message(ex.getMessage())
                         .timestamp(LocalDateTime.now())
                         .build());
     }

     @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTransactionException(InvalidTransactionException ex) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                 .body(ApiResponse.<Void>builder()
                         .success(false)
                         .message(ex.getMessage())
                         .timestamp(LocalDateTime.now())
                         .build());
     }

     @ExceptionHandler(DuplicateResourceException.class)
     public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex) {
         return ResponseEntity.status(HttpStatus.CONFLICT)
                 .body(ApiResponse.<Void>builder()
                         .success(false)
                         .message(ex.getMessage())
                         .timestamp(LocalDateTime.now())
                         .build());
     }

     @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
         Map<String, String> errors = new HashMap<>();
         ex.getBindingResult().getFieldErrors().forEach(error ->
                 errors.put(error.getField(), error.getDefaultMessage()));
         return ResponseEntity.badRequest()
                 .body(ApiResponse.<Map<String, String>> builder()
                         .success(false)
                         .message("Validation failed")
                         .data(errors)
                         .timestamp(LocalDateTime.now())
                         .build());
     }

     @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                 .body(ApiResponse.<Void>builder()
                         .success(false)
                         .message("An unexpected error occurred")
                         .timestamp(LocalDateTime.now())
                         .build());
     }
}