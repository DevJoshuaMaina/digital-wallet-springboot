package com.wallet.digital_wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a transaction request violates business rules.
 *
 * <p>Examples:
 * <ul>
 *   <li>Invalid PIN</li>
 *   <li>Transfer to self</li>
 *   <li>Amount exceeds configured daily limit (if enforced)</li>
 *   <li>Invalid transaction type rules</li>
 * </ul>
 *
 * <p>Mapped to HTTP 400 by {@link GlobalExceptionHandler}.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String message) {
        super(message);
    }
}