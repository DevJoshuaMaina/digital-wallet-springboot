package com.wallet.digital_wallet.dto.response;

import com.wallet.digital_wallet.enums.MerchantCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Merchant response DTO.
 *
 * <p>Returned by merchant endpoints.
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class MerchantResponse {
    private Long id;
    private String merchantCode;
    private String merchantName;
    private String email;
    private MerchantCategory category;
    private String status;
    private LocalDateTime createdAt;
}