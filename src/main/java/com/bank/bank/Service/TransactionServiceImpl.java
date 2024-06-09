package com.bank.bank.Service;

import com.bank.bank.Dto.Transaction;
import com.bank.bank.Entity.Transactions;
import com.bank.bank.Repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepo transactionRepo;
    @Override
    public void saveTransaction(Transaction transaction) {
        Transactions thisTransaction = Transactions.builder()
                .transactionType(transaction.getTransactionType())
                .accountNumber(transaction.getAccountNumber())
                .amount(transaction.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepo.save(thisTransaction);
        System.out.println("Transaction saved");
    }
}
