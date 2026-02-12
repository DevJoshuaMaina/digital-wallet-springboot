package com.wallet.digital_wallet.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String pin;
}
