package com.wallet.digital_wallet.controller;

import com.wallet.digital_wallet.dto.request.AddMoneyRequest;
import com.wallet.digital_wallet.dto.response.ApiResponse;
import com.wallet.digital_wallet.dto.response.WalletResponse;
import com.wallet.digital_wallet.entity.Wallet;
import com.wallet.digital_wallet.mapper.WalletMapper;
import com.wallet.digital_wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller for wallet operations.
 *
 * <p>Base path: {@code /api/v1/wallets}
 * <p>Supports:
 * <ul>
 *   <li>Wallet lookup by walletId or userId</li>
 *   <li>Balance retrieval</li>
 *   <li>Adding money to wallet</li>
 *   <li>Daily transaction limit updates</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet Operations")
public class WalletController {
    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get wallet by user ID")
    public ResponseEntity<ApiResponse<WalletResponse>> getWalletByUserId(@PathVariable Long userId) {
        Wallet wallet = walletService.getWalletById(userId);
        return ResponseEntity.ok(ApiResponse.success("Success", walletMapper.toResponse(wallet)));
    }

    @GetMapping("/{walletId}")
    @Operation(summary = "Get wallet by ID")
    public ResponseEntity<ApiResponse<WalletResponse>> getWallet(@PathVariable Long walletId) {
        Wallet wallet = walletService.getWalletById(walletId);
        return ResponseEntity.ok(ApiResponse.success("Success", walletMapper.toResponse(wallet)));
    }

    @PostMapping("/{walletId}/add-money")
    @Operation(summary = "Add money to wallet")
    public ResponseEntity<ApiResponse<WalletResponse>> addMoney(@PathVariable Long walletId, @Valid @RequestBody AddMoneyRequest request) {
        Wallet wallet = walletService.addMoney(walletId, request);
        return ResponseEntity.ok(ApiResponse.success("Money added successfully", walletMapper.toResponse(wallet)));
    }

    @GetMapping("/{walletId}/balance")
    @Operation(summary = "Get wallet balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getBalance(@PathVariable Long walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        return ResponseEntity.ok(ApiResponse.success("Success", balance));
    }

    @PutMapping("/{walletId}/daily-limit")
    @Operation(summary = "Set daily transaction limit")
    public ResponseEntity<ApiResponse<WalletResponse>> setDailyLimit(@PathVariable Long walletId, @RequestParam BigDecimal limit) {
        Wallet wallet = walletService.setDailyLimit(walletId, limit);
        return ResponseEntity.ok(ApiResponse.success("Daily limit updated successfully", walletMapper.toResponse(wallet)));
    }
}