package com.wallet.digital_wallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request payload for peer-to-peer transfers.
 *
 * <p>Used by: {@code POST /api/v1/transactions/transfer}
 */
@Data
public class TransferRequest {
    /**
     * Wallet ID of sender.
     */
    @NotNull(message = "From wallet ID is required")
    private Long fromWalletId;

    /**
     * Recipient username (receiver user).
     */
    @NotBlank(message = "To username is required")
    private String toUsername;

    /**
     * Amount to transfer.
     */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    /**
     * Optional description for transaction narration.
     */
    private String description;

    /**
     * Sender PIN for authorization.
     */
    @NotBlank(message = "PIN is required")
    private String pin;
}