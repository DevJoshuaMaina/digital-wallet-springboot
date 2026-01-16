package com.wallet.digital_wallet.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request payload for updating user profile fields.
 *
 * <p>Used by: {@code PUT /api/v1/users/{id}}
 */
@Data
public class UpdateUserRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;
}