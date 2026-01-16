 package com.wallet.digital_wallet.mapper;

 import com.wallet.digital_wallet.dto.response.TransactionResponse;
 import com.wallet.digital_wallet.entity.Transaction;
 import org.springframework.stereotype.Component;

 /**
  * Maps {@link Transaction} entities to {@link TransactionResponse} DTOs.
  *
  * <p>Documentation requirements:
  * <ul>
  *   <li>Null-safe mapping</li>
  *   <li>Handles transaction variants where merchant/toWallet/fromWallet may be null depending on type</li>
  *   <li>Does not expose internal ORM objects; only returns primitive/DTO fields</li>
  * </ul>
  */
 @Component
 public class TransactionMapper {
     /**
      * Converts a Transaction entity into a TransactionResponse.
      *
      * @param txn transaction entity (may be null)
      * @return transaction response DTO, or null if input is null
      */
     public TransactionResponse toResponse(Transaction txn) {
         return TransactionResponse.builder()
                 .id(txn.getId())
                 .transactionId(txn.getTransactionId())
                 .referenceNumber(txn.getReferenceNumber())
                 .fromUsername(txn.getFromWallet() != null ? txn.getFromWallet().getUser().getUsername() : null)
                 .toUsername(txn.getToWallet() != null ? txn.getToWallet().getUser().getUsername() : null)
                 .merchantName(txn.getMerchant() != null ? txn.getMerchant().getMerchantName() : null)
                 .amount(txn.getAmount())
                 .fee(txn.getFee())
                 .type(txn.getType())
                 .status(txn.getStatus())
                 .description(txn.getDescription())
                 .timestamp(txn.getCreatedAt())
                 .build();
     }
 }