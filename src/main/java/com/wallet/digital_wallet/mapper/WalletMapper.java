package com.wallet.digital_wallet.mapper;

import com.wallet.digital_wallet.dto.response.WalletResponse;
import com.wallet.digital_wallet.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {
    public WalletResponse toResponse(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .walletNumber(wallet.getWalletNumber())
                .balance(wallet.getBalance())
                .dailyLimit(wallet.getDailyLimit())
                .build();
    }
}