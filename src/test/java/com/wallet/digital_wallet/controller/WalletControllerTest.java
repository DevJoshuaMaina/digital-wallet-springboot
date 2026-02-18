package com.wallet.digital_wallet.controller;

import com.wallet.digital_wallet.dto.request.SetDailyLimitRequest;
import com.wallet.digital_wallet.dto.response.ApiResponse;
import com.wallet.digital_wallet.dto.response.WalletResponse;
import com.wallet.digital_wallet.entity.Wallet;
import com.wallet.digital_wallet.mapper.WalletMapper;
import com.wallet.digital_wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletController walletController;

    @Test
    void setLimit_usesJsonBodyLimitAndReturnsUpdatedWallet() {
        SetDailyLimitRequest request = new SetDailyLimitRequest();
        request.setLimit(new BigDecimal("25000"));

        Wallet wallet = Wallet.builder()
                .id(7L)
                .userId(3L)
                .dailyLimit(new BigDecimal("25000"))
                .walletNumber("WALTEST")
                .balance(BigDecimal.ZERO)
                .build();

        WalletResponse mapped = WalletResponse.builder()
                .id(7L)
                .dailyLimit(new BigDecimal("25000"))
                .walletNumber("WALTEST")
                .balance(BigDecimal.ZERO)
                .build();

        when(walletService.setDailyLimit(7L, new BigDecimal("25000"))).thenReturn(wallet);
        when(walletMapper.toResponse(wallet)).thenReturn(mapped);

        ResponseEntity<ApiResponse<WalletResponse>> response = walletController.setLimit(7L, request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Daily limit updated successfully", response.getBody().getMessage());
        assertEquals(new BigDecimal("25000"), response.getBody().getData().getDailyLimit());
        verify(walletService).setDailyLimit(7L, new BigDecimal("25000"));
    }
}
