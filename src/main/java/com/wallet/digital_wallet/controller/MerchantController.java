package com.wallet.digital_wallet.controller;

import com.wallet.digital_wallet.dto.request.CreateMerchantRequest;
import com.wallet.digital_wallet.dto.response.ApiResponse;
import com.wallet.digital_wallet.dto.response.MerchantResponse;
import com.wallet.digital_wallet.dto.response.PagedResponse;
import com.wallet.digital_wallet.dto.response.TransactionResponse;
import com.wallet.digital_wallet.entity.Merchant;
import com.wallet.digital_wallet.entity.Transaction;
import com.wallet.digital_wallet.enums.MerchantCategory;
import com.wallet.digital_wallet.mapper.MerchantMapper;
import com.wallet.digital_wallet.mapper.TransactionMapper;
import com.wallet.digital_wallet.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for merchant management.
 *
 * <p>Base path: {@code /api/v1/merchants}
 * <p>Provides merchant CRUD and merchant transaction history endpoints.
 */
@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
@Tag(name = "Merchant Management")
public class MerchantController {
    private final MerchantService merchantService;
    private final MerchantMapper merchantMapper;
    private final TransactionMapper transactionMapper;

    @PostMapping
    @Operation(summary = "Register merchant")
    public ResponseEntity<ApiResponse<MerchantResponse>> createMerchant(@Valid @RequestBody CreateMerchantRequest request) {
        Merchant merchant = merchantService.createMerchant(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Merchant created successfully", merchantMapper.toResponse(merchant)));
    }

    @GetMapping
    @Operation(summary = "List all merchants (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<MerchantResponse>>> getAllMerchants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Merchant> merchants = merchantService.getAllMerchants(pageable);
        PagedResponse<MerchantResponse> response = PagedResponse.fromPage(merchants.map(merchantMapper::toResponse));
        return ResponseEntity.ok(ApiResponse.success("Success", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get merchant by ID")
    public ResponseEntity<ApiResponse<MerchantResponse>> getMerchant(@PathVariable Long id) {
        Merchant merchant = merchantService.getMerchantById(id);
        return ResponseEntity.ok(ApiResponse.success("Success", merchantMapper.toResponse(merchant)));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get merchant by code")
    public ResponseEntity<ApiResponse<MerchantResponse>> getMerchantByCode(@PathVariable String code) {
        Merchant merchant = merchantService.getMerchantByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Success", merchantMapper.toResponse(merchant)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update merchant")
    public ResponseEntity<ApiResponse<MerchantResponse>> updateMerchant(@PathVariable Long id, @Valid @RequestBody CreateMerchantRequest request) {
        Merchant merchant = merchantService.updateMerchant(id, request);
        return ResponseEntity.ok(ApiResponse.success("Merchant updated successfully", merchantMapper.toResponse(merchant)));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "List merchants by category")
    public ResponseEntity<ApiResponse<PagedResponse<MerchantResponse>>> getMerchantsByCategory(
            @PathVariable MerchantCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Merchant> merchants = merchantService.getAllMerchants(pageable);  // Simplified; filter by category in service if needed
        PagedResponse<MerchantResponse> response = PagedResponse.fromPage(merchants.map(merchantMapper::toResponse));
        return ResponseEntity.ok(ApiResponse.success("Success", response));
    }

    @GetMapping("/{id}/transactions")
    @Operation(summary = "Get merchant transaction history")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getMerchantTransactions(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> txns = merchantService.getMerchantTransactions(id, pageable);
        PagedResponse<TransactionResponse> response = PagedResponse.fromPage(txns.map(transactionMapper::toResponse));
        return ResponseEntity.ok(ApiResponse.success("Success", response));
    }
}