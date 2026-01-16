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

/**
 * Service responsible for wallet operations: lookup, balance operations, and daily limits.
 *
 * <p>Documentation requirements:
 * <ul>
 *   <li>Uses {@link BigDecimal} for money</li>
 *   <li>Validates amounts are positive</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor @Slf4j
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet", "id", id));
    }

    /**
     * Returns a wallet by user ID (wallet.user.id).
     *
     * @param userId user ID
     * @return wallet
     * @throws ResourceNotFoundException if wallet not found
     */
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet", "userId", userId));
    }

    /**
     * Returns wallet balance.
     *
     * @param walletId wallet ID
     * @return current balance
     */
    public BigDecimal getBalance(Long walletId) {
        Wallet wallet = getWalletById(walletId);
        return wallet.getBalance();
    }

    /**
     * Adds money (credits) to a wallet and records a WALLET_TOPUP transaction.
     *
     * <p>Transactional because wallet balance update and transaction creation must be atomic.
     *
     * @param walletId wallet ID
     * @param request add money request
     * @return updated wallet
     */
    @Transactional
    public Wallet addMoney(Long walletId, AddMoneyRequest request) {
        Wallet wallet = getWalletById(walletId);
        wallet.credit(request.getAmount());
        wallet = walletRepository.save(wallet);
        log.info("Added {} to wallet ID: {}", request.getAmount(), walletId);
        return wallet;
    }

    /**
     * Updates wallet daily limit.
     *
     * @param walletId wallet ID
     * @param limit new daily limit (must be positive)
     * @return updated wallet
     */
    @Transactional
    public Wallet setDailyLimit(Long walletId, BigDecimal limit) {
        Wallet wallet = getWalletById(walletId);
        wallet.setDailyLimit(limit);
        wallet = walletRepository.save(wallet);
        log.info("Set daily limit to {} for wallet {}", limit, walletId);
        return wallet;
    }
}