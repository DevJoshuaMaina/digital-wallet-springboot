package com.wallet.digital_wallet.repository;

import com.wallet.digital_wallet.entity.Merchant;
import com.wallet.digital_wallet.enums.MerchantCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Merchant} persistence operations.
 *
 * <p>Documentation requirements:
 * <ul>
 *   <li>Primary key type is {@link Long}</li>
 *   <li>Merchant lookup by unique merchantCode</li>
 *   <li>Supports category filtering with pagination</li>
 * </ul>
 */
@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    /**
     * Finds merchant by merchant code.
     *
     * @param merchantCode unique merchant code
     * @return optional merchant
     */
    Optional<Merchant> findByMerchantCode(String merchantCode);
    List<Merchant> findByCategory(MerchantCategory category);
    /**
     * Lists merchants by category (paginated).
     *
     * @param category merchant category
     * @param pageable pagination
     * @return paginated merchants
     */
    Page<Merchant> findByCategory(MerchantCategory category, Pageable pageable);
}