package com.wallet.digital_wallet.entity;

import com.wallet.digital_wallet.enums.MerchantCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Merchant entity represents a business that can receive payments from wallets.
 *
 * <p>Merchants are identified by a unique merchant code (merchantCode) used by clients during payment.
 */
@Entity
@Table(name = "merchants")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique merchant code used for payments (e.g., MERABC1234).
     */
    @Column(name = "merchant_code", unique = true, nullable = false)
    private String merchantCode;

    @Column(name = "merchant_name", nullable = false)
    private String merchantName;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private MerchantCategory category;

    /**
     * Status string (e.g., ACTIVE). Kept as String for simplicity.
     * You can convert to enum if you want stricter typing.
     */
    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE";

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
}