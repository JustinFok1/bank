package com.bank.bank.Repo;

import com.bank.bank.Entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transactions, String> {
}
