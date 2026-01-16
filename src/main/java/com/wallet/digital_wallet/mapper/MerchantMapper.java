package com.wallet.digital_wallet.mapper;

import com.wallet.digital_wallet.dto.response.MerchantResponse;
import com.wallet.digital_wallet.entity.Merchant;
import org.springframework.stereotype.Component;

/**
 * Maps {@link Merchant} entities to {@link MerchantResponse} DTOs.
 *
 * <p>Documentation requirements:
 * <ul>
 *   <li>Entity → DTO mapping only</li>
 *   <li>Null-safe behavior</li>
 * </ul>
 */
@Component
public class MerchantMapper {
    /**
     * Converts a Merchant entity into MerchantResponse.
     *
     * @param merchant merchant entity (may be null)
     * @return merchant response DTO, or null if input is null
     */
    public MerchantResponse toResponse(Merchant merchant) {
        return MerchantResponse.builder()
                .id(merchant.getId())
                .merchantCode(merchant.getMerchantCode())
                .merchantName(merchant.getMerchantName())
                .email(merchant.getEmail())
                .category(merchant.getCategory())
                .status(merchant.getStatus())
                .createdAt(merchant.getCreatedAt())
                .build();
    }
}