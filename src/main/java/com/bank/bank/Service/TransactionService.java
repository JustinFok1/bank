package com.bank.bank.Service;

import com.bank.bank.Dto.Transaction;
import com.bank.bank.Entity.Transactions;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
}
