package com.wallet.digital_wallet.repository;

import com.wallet.digital_wallet.entity.Merchant;
import com.wallet.digital_wallet.enums.MerchantCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByMerchantCode(String merchantCode);
    List<Merchant> findByCategory(MerchantCategory category);
    Page<Merchant> findByCategory(MerchantCategory category, Pageable pageable);
}