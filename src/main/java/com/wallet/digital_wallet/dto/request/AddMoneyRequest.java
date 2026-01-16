package com.wallet.digital_wallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request payload for adding money to a wallet.
 *
 * <p>Used by: {@code POST /api/v1/wallets/{walletId}/add-money}
 */
@Data
public class AddMoneyRequest {
    /**
     * Amount to credit into the wallet.
     * Must be positive.
     */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
}