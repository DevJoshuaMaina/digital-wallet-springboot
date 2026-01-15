package com.wallet.digital_wallet.mapper;

import com.wallet.digital_wallet.dto.response.MerchantResponse;
import com.wallet.digital_wallet.entity.Merchant;
import org.springframework.stereotype.Component;

@Component
public class MerchantMapper {
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