package com.wallet.digital_wallet.service;

import com.wallet.digital_wallet.dto.request.AddMoneyRequest;
import com.wallet.digital_wallet.entity.Wallet;
import com.wallet.digital_wallet.exception.ResourceNotFoundException;
import com.wallet.digital_wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor @Slf4j
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet", "id", id));
    }

    public Wallet getWalletByUSerId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet", "userId", userId));
    }

    public BigDecimal getBalance(Long walletId) {
        Wallet wallet = getWalletById(walletId);
        return wallet.getBalance();
    }

    @Transactional
    public Wallet addMoney(Long walletId, AddMoneyRequest request) {
        Wallet wallet = getWalletById(walletId);
        wallet.credit(request.getAmount());
        wallet = walletRepository.save(wallet);
        log.info("Added {} to wallet ID: {}", request.getAmount(), walletId);
        return wallet;
    }

    @Transactional
    public Wallet setDailyLimit(Long walletId, BigDecimal limit) {
        Wallet wallet = getWalletById(walletId);
        wallet.setDailyLimit(limit);
        wallet = walletRepository.save(wallet);
        log.info("Set daily limit to {} for wallet {}", limit, walletId);
        return wallet;
    }
}