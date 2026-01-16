package com.wallet.digital_wallet.mapper;

import com.wallet.digital_wallet.dto.response.WalletResponse;
import com.wallet.digital_wallet.entity.Wallet;
import org.springframework.stereotype.Component;

/**
 * Maps {@link Wallet} entities to {@link WalletResponse} DTOs.
 *
 * <p>Documentation requirements:
 * <ul>
 *   <li>Mapping is entity → DTO only (requests are handled by service layer)</li>
 *   <li>Null-safe behavior: mapper returns null when input is null</li>
 * </ul>
 */
@Component
public class WalletMapper {
    /**
     * Converts a Wallet entity into a WalletResponse.
     *
     * @param wallet wallet entity (may be null)
     * @return wallet response DTO, or null if input is null
     */
    public WalletResponse toResponse(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .walletNumber(wallet.getWalletNumber())
                .balance(wallet.getBalance())
                .dailyLimit(wallet.getDailyLimit())
                .build();
    }
}