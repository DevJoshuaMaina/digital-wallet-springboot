package com.wallet.digital_wallet.mapper;

import com.wallet.digital_wallet.dto.response.UserResponse;
import com.wallet.digital_wallet.dto.response.WalletResponse;
import com.wallet.digital_wallet.entity.User;
import com.wallet.digital_wallet.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .wallet(toWalletResponse(user.getWallet()))
                .createdAt(user.getCreatedAt())
                .build();
    }

    private WalletResponse toWalletResponse(Wallet wallet) {
        if (wallet == null) return null;
        return WalletResponse.builder()
                .id(wallet.getId())
                .walletNumber(wallet.getWalletNumber())
                .balance(wallet.getBalance())
                .dailyLimit(wallet.getDailyLimit())
                .build();
    }
}