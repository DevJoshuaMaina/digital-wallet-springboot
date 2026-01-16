package com.wallet.digital_wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a wallet does not have enough funds to complete a debit operation.
 *
 * <p>Mapped to HTTP 400 by {@link GlobalExceptionHandler}.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}