package com.wallet.digital_wallet.controller;

import com.wallet.digital_wallet.dto.request.MerchantPaymentRequest;
import com.wallet.digital_wallet.dto.request.TransferRequest;
import com.wallet.digital_wallet.dto.response.ApiResponse;
import com.wallet.digital_wallet.dto.response.PagedResponse;
import com.wallet.digital_wallet.dto.response.TransactionResponse;
import com.wallet.digital_wallet.entity.Transaction;
import com.wallet.digital_wallet.enums.TransactionType;
import com.wallet.digital_wallet.mapper.TransactionMapper;
import com.wallet.digital_wallet.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST controller for transaction operations (transfers and merchant payments).
 *
 * <p>Base path: {@code /api/v1/transactions}
 * <p>Provides endpoints for initiating transactions and viewing transaction history.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping("/transfer")
    @Operation(summary = "Transfer money to another user")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(@Valid @RequestBody TransferRequest request) {
        Transaction txn = transactionService.transfer(request);
        return ResponseEntity.ok(ApiResponse.success("Transfer completed successfully", transactionMapper.toResponse(txn)));
    }

    @PostMapping("/merchant-payment")
    @Operation(summary = "Pay merchant")
    public ResponseEntity<ApiResponse<TransactionResponse>> payMerchant(@Valid @RequestBody MerchantPaymentRequest request) {
        Transaction txn = transactionService.payMerchant(request);
        return ResponseEntity.ok(ApiResponse.success("Payment completed successfully", transactionMapper.toResponse(txn)));
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransaction(@PathVariable Long transactionId) {
        Transaction txn = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(ApiResponse.success("Success", transactionMapper.toResponse(txn)));
    }

    @GetMapping("/reference/{refNumber}")
    @Operation(summary = "Get transaction by reference number")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionByReference(@PathVariable String refNumber) {
        Transaction txn = transactionService.getTransactionByReference(refNumber);
        return ResponseEntity.ok(ApiResponse.success("Success", transactionMapper.toResponse(txn)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user transactions (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> txns = transactionService.getUserTransactions(userId, pageable);
        PagedResponse<TransactionResponse> response = PagedResponse.fromPage(txns.map(transactionMapper::toResponse));
        return ResponseEntity.ok(ApiResponse.success("Success", response));
    }

    @GetMapping("/user/{userId}/filter")
    @Operation(summary = "Filter user transactions")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getFilteredTransactions(
            @PathVariable Long userId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> txns = transactionService.getFilteredTransactions(userId, type, startDate, endDate, pageable);
        PagedResponse<TransactionResponse> response = PagedResponse.fromPage(txns.map(transactionMapper::toResponse));
        return ResponseEntity.ok(ApiResponse.success("Success", response));
    }

    @GetMapping("/user/{userId}/stats")
    @Operation(summary = "Get transaction statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTransactionStats(@PathVariable Long userId) {
        Map<String, Object> stats = transactionService.getTransactionStats(userId);
        return ResponseEntity.ok(ApiResponse.success("Success", stats));
    }
}