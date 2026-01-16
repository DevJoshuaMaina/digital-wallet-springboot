package com.wallet.digital_wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when attempting to create a resource that violates a uniqueness rule.
 *
 * <p>Examples:
 * <ul>
 *   <li>Username already exists</li>
 *   <li>Email already exists</li>
 *   <li>Merchant code uniqueness violation (rare, but possible)</li>
 * </ul>
 *
 * <p>Mapped to HTTP 409 by {@link GlobalExceptionHandler}.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}