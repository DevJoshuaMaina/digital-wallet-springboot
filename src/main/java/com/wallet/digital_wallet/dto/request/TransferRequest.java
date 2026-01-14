package com.wallet.digital_wallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotNull(message = "From wallet ID is required")
    private Long fromWalletId;

    @NotBlank(message = "To username is required")
    private String toUsername;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String description;

    @NotBlank(message = "PIN is required")
    private String pin;
}