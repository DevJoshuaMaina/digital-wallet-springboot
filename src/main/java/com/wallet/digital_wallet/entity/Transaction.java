package com.wallet.digital_wallet.entity;

import com.wallet.digital_wallet.enums.TransactionStatus;
import com.wallet.digital_wallet.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction entity records wallet operations such as transfers, top-ups, and merchant payments.
 *
 * <p>Depending on {@link TransactionType}:
 * <ul>
 *   <li>PEER_TRANSFER: fromWallet != null, toWallet != null, merchant == null</li>
 *   <li>MERCHANT_PAYMENT: fromWallet != null, merchant != null, toWallet == null</li>
 *   <li>WALLET_TOPUP: fromWallet == null, toWallet != null, merchant == null</li>
 * </ul>
 */
@Entity
@Table(name = "transactions")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Internal transaction identifier (can be shown to client).
     */
    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

/**
 * Reference number used to query transactions.
 */
    @Column(name = "reference_number", unique = true, nullable = false)
    private String referenceNumber;

    /**
     * Wallet debited for transfer/payment. Null for wallet top-up.
     */
    @ManyToOne
    @JoinColumn(name = "from_wallet_id")
    private Wallet fromWallet;

    /**
     * Wallet credited for transfer/top-up. Null for merchant payment.
     */
    @ManyToOne
    @JoinColumn(name = "to_wallet_id")
    private Wallet toWallet;

    /**
     * Merchant receiving payment. Only used for merchant payment transactions.
     */
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal fee =BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}