package com.wallet.digital_wallet.repository;

import com.wallet.digital_wallet.entity.Transaction;
import com.wallet.digital_wallet.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

/**
 * Repository for {@link Transaction} persistence operations.
 *
 * <p>Documentation requirements:
 * <ul>
 *   <li>Primary key type is {@link Long}</li>
 *   <li>Supports lookup by referenceNumber</li>
 *   <li>Supports user transaction history retrieval using wallet user relationships</li>
 *   <li>Supports merchant transaction history retrieval</li>
 * </ul>
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Finds transaction by transaction ID

     * @param transactionId transaction id
     * @return optional transaction
     */
    Optional<Transaction> findByTransactionId(String transactionId);

    /**
     * Finds transaction by reference number.
     *
     * @param referenceNumber reference number
     * @return optional transaction
     */
    Optional<Transaction> findByReferenceNumber(String referenceNumber);

    @Query("SELECT t FROM Transaction t WHERE t.fromWallet.id = :walletId OR t.toWallet.id = :walletId")
    Page<Transaction> findByWalletId(@Param("walletId") Long walletId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.merchant.id = :merchantId")
    Page<Transaction> findByMerchantId(@Param("merchantId") Long merchantId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.fromWallet.id = :userId OR t.toWallet.id = :userId) AND t.createdAt BETWEEN :start AND :end")
    Page<Transaction> findByUserIdAndType(@Param("userId") Long userId, @Param("type") TransactionType type, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.fromWallet.id = :userId OR t.toWallet.id = :userId) AND t.createdAt BETWEEN :start AND :end")
    Page<Transaction> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);
}