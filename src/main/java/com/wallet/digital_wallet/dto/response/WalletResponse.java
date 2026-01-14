package com.wallet.digital_wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class WalletResponse {
    private Long id;
    private String walletNumber;
    private BigDecimal balance;
    private BigDecimal dailyLimit;
}