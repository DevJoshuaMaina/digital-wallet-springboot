package com.wallet.digital_wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Wallet entity representing user funds storage.
 *
 * <p>A wallet belongs to exactly one {@link User}. All monetary values are stored using
 * {@link BigDecimal} for precision.
 */
@Entity
@Table(name = "wallets")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Wallet {
    /**
     * Database primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Wallet number for display/lookup (unique).
     */
    @Column(name = "wallet_number", unique = true, nullable = false)
    private String walletNumber;

    /**
     * Current wallet balance.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * Daily limit (simplified model: max allowed amount per transaction/day configuration).
     * If you want true "daily spend tracking", you’d need aggregation by date.
     */
    @Column(name = "daily_limit", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal dailyLimit = new BigDecimal("10000");

    /**
     * Wallet owner. This creates `wallets.user_id` FK.
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if wallet has enough balance for a debit operation.
     *
     * @param amount amount to debit
     * @return true if balance >= amount
     */
    public boolean hasSufficientBalance(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    /**
     * Debits (subtracts) the given amount from balance.
     *
     * @param amount amount to subtract
     */
    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    /**
     * Credits (adds) the given amount to balance.
     *
     * @param amount amount to add
     */
    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}