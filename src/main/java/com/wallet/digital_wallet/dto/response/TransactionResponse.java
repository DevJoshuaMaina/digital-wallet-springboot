package com.wallet.digital_wallet.dto.response;

import com.wallet.digital_wallet.enums.TransactionStatus;
import com.wallet.digital_wallet.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class TransactionResponse {
    private Long id;
    private String transactionId;
    private String referenceNumber;
    private String fromUsername;
    private String toUsername;
    private String merchantName;
    private BigDecimal amount;
    private BigDecimal fee;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private LocalDate timestamp;
}