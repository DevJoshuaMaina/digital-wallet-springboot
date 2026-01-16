package com.wallet.digital_wallet.repository;

import com.wallet.digital_wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Wallet} persistence operations.
 *
 * <p>Documentation requirements:
 * <ul>
 *   <li>Primary key type is {@link Long}</li>
 *   <li>Wallet is linked to {@code User} via {@code wallet.user}</li>
 *   <li>Provides lookup by wallet number and by user ID</li>
 * </ul>
 */
@Repository
public interface WalletRepository  extends JpaRepository<Wallet, Long> {
    /**
     * Finds wallet for a specific user by user ID.
     *
     * <p>Note: This uses property traversal (wallet.user.id) because wallet stores a User relation.
     *
     * @param userId user ID
     * @return optional wallet
     */
    Optional<Wallet> findByUserId(Long userId);

    /**
     * Finds wallet by wallet number.
     *
     * @param walletNumber wallet number
     * @return optional wallet
     */
    Optional<Wallet> findByWalletNumber(String walletNumber);
}