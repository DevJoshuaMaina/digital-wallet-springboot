package com.wallet.digital_wallet.dto.request;

import com.wallet.digital_wallet.enums.MerchantCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request payload for creating or updating a merchant.
 *
 * <p>Used by:
 * <ul>
 *   <li>{@code POST /api/v1/merchants}</li>
 *   <li>{@code PUT /api/v1/merchants/{id}}</li>
 * </ul>
 */
@Data
public class CreateMerchantRequest{
    @NotBlank(message = "Merchant name is required")
    private String merchantName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Category is required")
    private MerchantCategory category;
}