package com.wallet.digital_wallet.service;

import com.wallet.digital_wallet.entity.Wallet;
import com.wallet.digital_wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void setDailyLimit_updatesWalletWhenLimitIsValid() {
        Wallet wallet = Wallet.builder()
                .id(10L)
                .userId(99L)
                .dailyLimit(new BigDecimal("10000"))
                .balance(BigDecimal.ZERO)
                .walletNumber("WAL123")
                .build();

        when(walletRepository.findById(10L)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet updated = walletService.setDailyLimit(10L, new BigDecimal("25000"));

        assertEquals(new BigDecimal("25000"), updated.getDailyLimit());
        verify(walletRepository).save(wallet);
    }

    @Test
    void setDailyLimit_throwsBadRequestWhenLimitIsZeroOrNull() {
        assertThrows(IllegalArgumentException.class, () -> walletService.setDailyLimit(10L, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> walletService.setDailyLimit(10L, null));
    }
}
