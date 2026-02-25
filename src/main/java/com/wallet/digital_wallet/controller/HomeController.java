package com.wallet.digital_wallet.controller;

import com.wallet.digital_wallet.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, String>>> home() {
        return ResponseEntity.ok(ApiResponse.success(
                "Digital Wallet API is running",
                Map.of(
                        "health", "/actuator/health",
                        "docs", "/swagger-ui.html"
                )
        ));
    }
}
