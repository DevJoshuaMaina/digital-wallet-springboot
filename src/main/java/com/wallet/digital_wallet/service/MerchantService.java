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

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantService {
    private final MerchantRepository merchantRepository;
    private final TransactionRepository transactionRepository;

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

    public Merchant getMerchantById(Long id) {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", "id", id));
    }

    public Merchant getMerchantByCode(String code) {
        return merchantRepository.findByMerchantCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", "code", code));
    }

    public List<Merchant> getMerchantsByCategory(MerchantCategory category) {
        return merchantRepository.findByCategory(category);
    }

    public Page<Merchant> getAllMerchants(Pageable pageable) {
        return merchantRepository.findAll(pageable);
    }

    @Transactional
    public Merchant updateMerchant(Long id, CreateMerchantRequest request) {
        Merchant merchant = getMerchantById(id);
        merchant.setMerchantName(request.getMerchantName());
        merchant.setEmail(request.getEmail());
        merchant.setCategory(request.getCategory());
        return merchantRepository.save(merchant);
    }

    public Page<Transaction> getMerchantTransactions(Long merchantId, Pageable pageable) {
        getMerchantById(merchantId);
        return transactionRepository.findByMerchantId(merchantId, pageable);  // Assuming TransactionRepository is accessible; adjust if needed
    }
}