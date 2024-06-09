package com.bank.bank.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class Transaction {
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}
