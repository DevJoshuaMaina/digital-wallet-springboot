 package com.wallet.digital_wallet.mapper;

 import com.wallet.digital_wallet.dto.response.TransactionResponse;
 import com.wallet.digital_wallet.entity.Transaction;
 import org.springframework.stereotype.Component;

 @Component
 public class TransactionMapper {
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