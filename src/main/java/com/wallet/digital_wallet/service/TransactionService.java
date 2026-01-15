package com.wallet.digital_wallet.service;

import com.wallet.digital_wallet.dto.request.MerchantPaymentRequest;
import com.wallet.digital_wallet.dto.request.TransferRequest;
import com.wallet.digital_wallet.entity.Merchant;
import com.wallet.digital_wallet.entity.Transaction;
import com.wallet.digital_wallet.entity.Wallet;
import com.wallet.digital_wallet.enums.TransactionStatus;
import com.wallet.digital_wallet.enums.TransactionType;
import com.wallet.digital_wallet.exception.InsufficientBalanceException;
import com.wallet.digital_wallet.exception.InvalidTransactionException;
import com.wallet.digital_wallet.exception.ResourceNotFoundException;
import com.wallet.digital_wallet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor @Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final UserService userService;
    private final MerchantService merchantService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Transaction transfer(TransferRequest request) {
        Wallet fromWallet = walletService.getWalletById(request.getFromWalletId());
        Wallet toWallet = userService.getUserByUsername(request.getToUsername()).getWallet();

        if (!passwordEncoder.matches(request.getPin(), fromWallet.getUser().getPinHash())) {
            throw new InvalidTransactionException("Invalid PIN");
        }
        if (fromWallet.getId().equals(toWallet.getId())) {
            throw new InvalidTransactionException("Cannot transfer to self");
        }
        if (!fromWallet.hasSufficientBalance(request.getAmount())) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        fromWallet.debit(request.getAmount());
        toWallet.credit(request.getAmount());

        Transaction txn = Transaction.builder()
                .transactionId("TXN" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .referenceNumber("REF" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .fromWallet(fromWallet)
                .toWallet(toWallet)
                .amount(request.getAmount())
                .type(TransactionType.PEER_TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription())
                .build();

        txn = transactionRepository.save(txn);
        log.info("Transfer completed: {}", txn.getTransactionId());
        return txn;
    }

    @Transactional
    public Transaction payMerchant(MerchantPaymentRequest request) {
        Wallet fromWallet = walletService.getWalletById(request.getFromWalletId());
        Merchant merchant = merchantService.getMerchantByCode(request.getMerchantCode());

        if (!passwordEncoder.matches(request.getPin(), fromWallet.getUser().getPinHash())) {
            throw new InvalidTransactionException("Invalid PIN");
        }
        if (!fromWallet.hasSufficientBalance(request.getAmount())) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        fromWallet.debit(request.getAmount());

        Transaction txn = Transaction.builder()
                .transactionId("TXN" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .referenceNumber("REF" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .fromWallet(fromWallet)
                .merchant(merchant)
                .amount(request.getAmount())
                .type(TransactionType.MERCHANT_PAYMENT)
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription())
                .build();

        txn = transactionRepository.save(txn);
        log.info("Merchant payment completed: {}", txn.getTransactionId());
        return txn;
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
    }

    public Transaction getTransactionByReference(String ref) {
        return transactionRepository.findByReferenceNumber(ref)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "reference", ref));
    }

    public Page<Transaction> getUserTransactions(Long userId, Pageable pageable) {
        return transactionRepository.findByWalletId(userId, pageable);
    }

    public Page<Transaction> getFilteredTransactions(Long userId, TransactionType type, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        if (type != null) {
            return transactionRepository.findByUserIdAndType(userId, type, pageable);
        }
        if (start != null && end != null) {
            return transactionRepository.findByUserIdAndDateRange(userId, start, end, pageable);
        }
        return getUserTransactions(userId, pageable);
    }

    public Map<String, Object> getTransactionStats(Long userId) {
        // Simplified stats; in production, use custom queries
        Page<Transaction> txns = getUserTransactions(userId, Pageable.unpaged());
        BigDecimal totalSent = txns.getContent().stream()
                .filter(t -> t.getFromWallet() != null && t.getFromWallet().getUserId().equals(userId))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalReceived = txns.getContent().stream()
                .filter(t -> t.getToWallet() != null && t.getToWallet().getUserId().equals(userId))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Map.of("totalSent", totalSent, "totalReceived", totalReceived, "transactionCount", txns.getTotalElements());
    }
}