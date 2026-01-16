package com.wallet.digital_wallet.dto.response;

import com.wallet.digital_wallet.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User response DTO.
 *
 * <p>Returned by user endpoints. Contains embedded wallet summary.
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private UserStatus status;
    private WalletResponse wallet;
    private LocalDateTime createdAt;
}