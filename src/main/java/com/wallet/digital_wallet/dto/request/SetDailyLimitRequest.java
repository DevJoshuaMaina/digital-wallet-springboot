package com.wallet.digital_wallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request payload for updating a wallet's daily limit.
 */
@Data
public class SetDailyLimitRequest {
    @NotNull(message = "limit is required")
    @DecimalMin(value = "0.01", message = "limit must be greater than 0")
    private BigDecimal limit;
}
