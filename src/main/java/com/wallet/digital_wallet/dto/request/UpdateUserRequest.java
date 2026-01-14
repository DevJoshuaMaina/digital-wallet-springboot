package com.wallet.digital_wallet.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;
}