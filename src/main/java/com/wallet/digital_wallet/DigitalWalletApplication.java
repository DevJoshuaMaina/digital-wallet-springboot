package com.wallet.digital_wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Digital Wallet System API.
 *
 * <p>Run the application and access:
 * <ul>
 *   <li>Swagger UI: http://localhost:8080/swagger-ui/index.html </li>
 *   <li>Base API: http://localhost:8080/api/v1 </li>
 * </ul>
 */
@SpringBootApplication
public class DigitalWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalWalletApplication.class, args);
	}

}
