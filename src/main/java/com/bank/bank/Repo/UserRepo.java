package com.bank.bank.Repo;

import com.bank.bank.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    Optional<User> findByAccountNumber(String accountNumber);
    Optional<User> findByEmail(String email);

}
