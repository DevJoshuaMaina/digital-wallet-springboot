package com.wallet.digital_wallet.service;

import com.wallet.digital_wallet.dto.request.CreateMerchantRequest;
import com.wallet.digital_wallet.entity.Merchant;
import com.wallet.digital_wallet.entity.Transaction;
import com.wallet.digital_wallet.enums.MerchantCategory;
import com.wallet.digital_wallet.exception.DuplicateResourceException;
import com.wallet.digital_wallet.exception.ResourceNotFoundException;
import com.wallet.digital_wallet.repository.MerchantRepository;
import com.wallet.digital_wallet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service responsible for merchant registration and lookup.
 *
 * <p>Documentation requirements:
 * <ul>
 *   <li>Merchant codes are generated server-side and must be unique</li>
 *   <li>Supports category filtering and merchant transaction history</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantService {
    private final MerchantRepository merchantRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Creates a merchant.
     *
     * @param request merchant create request
     * @return created merchant
     */
    @Transactional
    public Merchant createMerchant(CreateMerchantRequest request) {
        if (merchantRepository.findByMerchantCode(request.getMerchantName().toUpperCase().replace(" ", "")).isPresent()) {
            throw new DuplicateResourceException("Merchant code already exists");
        }

        Merchant merchant = Merchant.builder()
                .merchantCode("MER" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .merchantName(request.getMerchantName())
                .email(request.getEmail())
                .category(request.getCategory())
                .status("ACTIVE")
                .build();

        merchant = merchantRepository.save(merchant);
        log.info("Merchant created with code: {}", merchant.getMerchantCode());
        return merchant;
    }

    /**
     * Returns merchant by ID.
     *
     * @param id merchant ID
     * @return merchant
     */
    public Merchant getMerchantById(Long id) {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", "id", id));
    }

    /**
     * Returns merchant by merchant code.
     *
     * @param code merchant code
     * @return merchant
     */
    public Merchant getMerchantByCode(String code) {
        return merchantRepository.findByMerchantCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", "code", code));
    }

    /**
     * Returns merchants filtered by category (paginated).
     */
    public List<Merchant> getMerchantsByCategory(MerchantCategory category) {
        return merchantRepository.findByCategory(category);
    }

    /**
     * Returns all merchants (paginated).
     */
    public Page<Merchant> getAllMerchants(Pageable pageable) {
        return merchantRepository.findAll(pageable);
    }

    /**
     * Updates merchant details by ID.
     */
    @Transactional
    public Merchant updateMerchant(Long id, CreateMerchantRequest request) {
        Merchant merchant = getMerchantById(id);
        merchant.setMerchantName(request.getMerchantName());
        merchant.setEmail(request.getEmail());
        merchant.setCategory(request.getCategory());
        return merchantRepository.save(merchant);
    }

    /**
     * Returns transaction history for a merchant (paginated).
     *
     * @param merchantId merchant ID
     * @param pageable pagination
     * @return merchant transactions page
     */
    public Page<Transaction> getMerchantTransactions(Long merchantId, Pageable pageable) {
        getMerchantById(merchantId);
        return transactionRepository.findByMerchantId(merchantId, pageable);  // Assuming TransactionRepository is accessible; adjust if needed
    }
}