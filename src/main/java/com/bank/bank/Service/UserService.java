package com.bank.bank.Service;

import com.bank.bank.BankApplication;
import com.bank.bank.Dto.*;
import com.bank.bank.Repo.UserRepo;
import com.bank.bank.config.SecurityConfig;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse deleteAccount(AccountInfo accountInfo);
    BankResponse balanceEnquiry(EnquiryReq enquiryReq);
    String nameEnquiry(EnquiryReq enquiryReq);
    BankResponse creditAccount(CreditDebitRequest creditRequest);
    BankResponse debitAccount(CreditDebitRequest debitRequest);
    BankResponse transfer(TransferRequest transferRequest);
    BankResponse login(LoginDto loginDto);
}
