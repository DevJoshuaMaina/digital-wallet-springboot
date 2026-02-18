package com.wallet.digital_wallet.exception;

import com.wallet.digital_wallet.dto.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception handler for the Digital Wallet API.
 *
 * <p>Documentation requirements:
 * <ul>
 *   <li>All errors return {@code ApiResponse} wrapper with {@code success=false}</li>
 *   <li>HTTP status codes are mapped per exception type</li>
 *   <li>Validation errors return a field->message map in the response "data"</li>
 * </ul>
 *
 * <p>Error response example:
 * <pre>
 * {
 *   "success": false,
 *   "message": "Validation failed",
 *   "data": {
 *     "email": "Email must be valid"
 *   },
 *   "timestamp": "2026-01-15T12:00:00"
 * }
 * </pre>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles resource-not-found errors.
     */
     @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND)
                 .body(ApiResponse.<Void>builder()
                         .success(false)
                         .message(ex.getMessage())
                         .timestamp(LocalDateTime.now())
                         .build());
     }

    /**
     * Handles insufficient funds errors.
     */
     @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse<Void>> handleInsufficientBalance(InsufficientBalanceException ex) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                 .body(ApiResponse.<Void>builder()
                         .success(false)
                         .message(ex.getMessage())
                         .timestamp(LocalDateTime.now())
                         .build());
     }

    /**
     * Handles business-rule violations for transactions (e.g., invalid PIN, transfer to self).
     */
     @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTransactionException(InvalidTransactionException ex) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                 .body(ApiResponse.<Void>builder()
                         .success(false)
                         .message(ex.getMessage())
                         .timestamp(LocalDateTime.now())
                         .build());
     }

    /**
     * Handles duplicate resource errors (unique constraints or business checks).
     */
     @ExceptionHandler(DuplicateResourceException.class)
     public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex) {
         return ResponseEntity.status(HttpStatus.CONFLICT)
                 .body(ApiResponse.<Void>builder()
                         .success(false)
                         .message(ex.getMessage())
                         .timestamp(LocalDateTime.now())
                         .build());
     }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message("Duplicate or invalid data violates database constraints")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /**
     * Handles DTO validation errors (@Valid).
     *
     * @param ex validation exception
     * @return ApiResponse containing field-level errors
     */
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

    /**
     * Fallback handler for unexpected exceptions.
     *
     * <p>Do not leak internal stack traces to clients in production.
     */
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
