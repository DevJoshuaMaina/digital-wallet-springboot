package com.wallet.digital_wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a requested resource cannot be found.
 *
 * <p>Typical usage:
 * <ul>
 *   <li>User not found by ID</li>
 *   <li>Wallet not found by ID</li>
 *   <li>Merchant not found by code</li>
 * </ul>
 *
 * <p>Mapped to HTTP 404 by {@link GlobalExceptionHandler}.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: '%s'", resource, field, value));
    }
}