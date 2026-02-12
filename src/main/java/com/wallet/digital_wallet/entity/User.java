package com.wallet.digital_wallet.entity;

import com.wallet.digital_wallet.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User entity representing a wallet account holder.
 *
 * <p>Stores user identity data and a hashed PIN used to authorize wallet operations.
 * A {@link Wallet} is created automatically when a user registers.
 */
@Entity
@Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    /**
     * Database primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username used for user lookup and transfers.
     */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /**
     * Unique email for the user.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * User's full name.
     */
    @Column(unique = true, nullable = false)
    private String fullName;

    /**
     * Phone number for the user (format validation is done at DTO level).
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Hashed PIN used to authorize transactions.
     * Never store raw PIN.
     */
    @Column(name = "pin_hash", nullable = false)
    private String pinHash;

    /**
     * User's role for authorization (e.g., "USER").
     */
    @Builder.Default
    private String role = "USER";

    /**
     * Soft status for user accounts.
     */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * User's wallet. Created during registration.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;

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