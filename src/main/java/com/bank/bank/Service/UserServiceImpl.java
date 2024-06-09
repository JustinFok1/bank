package com.bank.bank.Service;

import com.bank.bank.Dto.*;
import com.bank.bank.Entity.Role;
import com.bank.bank.Entity.Transactions;
import com.bank.bank.Entity.User;
import com.bank.bank.Repo.UserRepo;
import com.bank.bank.Utils.AccountUtils;
import com.bank.bank.config.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if(userRepo.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_USER"))
                .build();

        User savedUser = userRepo.save(newUser);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Congrats on Your New Bank Account!")
                .messageBody("Thank you for enrolling with Justin's Banking Firm. We hope you enjoy your time with us.\nYour Account Details:\n" +
                        "Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + "\n" +
                        "Address: " + savedUser.getAddress() + "\n" +
                        "Phone Number: " + savedUser.getPhoneNumber() + "\n\n" +
                        "Account Number: " + savedUser.getAccountNumber() + "\n" +
                        "Account Balance: " + savedUser.getAccountBalance() + "\n\n" +
                        "Status: " + savedUser.getStatus() + "\n")
                .build();

        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " ")
                        .build())
                .build();
    }
    public BankResponse deleteAccount(AccountInfo accountInfo){
        Optional<User> user  = userRepo.findByAccountNumber(accountInfo.getAccountNumber());
        if(user.isPresent()){
            User userToDelete = user.get();
            userToDelete.setStatus("INACTIVE");
            userRepo.save(userToDelete);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(userToDelete.getEmail())
                    .subject("Successfully deleted account!")
                    .messageBody("Successfully deleted account.\n\nYour Account Details:\n" +
                            "Name: " + userToDelete.getFirstName() + " " + userToDelete.getLastName() + "\n" +
                            "Address: " + userToDelete.getAddress() + "\n" +
                            "Phone Number: " + userToDelete.getPhoneNumber() + "\n\n" +
                            "Account Number: " + userToDelete.getAccountNumber() + "\n" +
                            "Account Balance: " + userToDelete.getAccountBalance() + "\n\n" +
                            "Status: " + userToDelete.getStatus() + "\n")
                    .build();
            emailService.sendEmailAlert(emailDetails);
            userRepo.delete(userToDelete);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DELETED_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DELETED_MESSAGE)
                    .build();
        }
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                .build();
    }

    public BankResponse login(LoginDto loginDto){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        EmailDetails loginAlert = EmailDetails.builder()
                .subject("You're logged in!")
                .recipient(loginDto.getEmail())
                .messageBody("You logged into your account. If you did not initiate, please contact your bank.")
                .build();
        emailService.sendEmailAlert(loginAlert);
        return BankResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();
    }


    @Override
    public BankResponse balanceEnquiry(EnquiryReq enquiryReq) {
        boolean accountExist = userRepo.existsByAccountNumber(enquiryReq.getAccountNumber());
        if(!accountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build();
        }
        Optional<User> user = userRepo.findByAccountNumber(enquiryReq.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(user.get().getAccountBalance())
                        .accountNumber(enquiryReq.getAccountNumber())
                        .accountName(user.get().getFirstName() + user.get().getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryReq enquiryReq) {
        boolean accountExist = userRepo.existsByAccountNumber(enquiryReq.getAccountNumber());
        if(!accountExist) {
            return AccountUtils.ACCOUNT_NOT_FOUND_CODE + "\n" + AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE;
        }
        Optional<User> user = userRepo.findByAccountNumber(enquiryReq.getAccountNumber());
        return user.get().getFirstName() + user.get().getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditRequest) {
        boolean accountExist = userRepo.existsByAccountNumber(creditRequest.getAccountNumber());
        if(!accountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build();
        }
        Optional<User> user = userRepo.findByAccountNumber(creditRequest.getAccountNumber());
        BigDecimal updatedNum = user.get().getAccountBalance().add(creditRequest.getAmount());
        user.get().setAccountBalance(updatedNum);
        userRepo.save(user.get());

        Transaction transaction = Transaction.builder()
                .accountNumber(user.get().getAccountNumber())
                .transactionType("CREDIT")
                .amount(creditRequest.getAmount())
                .build();

        transactionService.saveTransaction(transaction);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(user.get().getAccountBalance())
                        .accountNumber(creditRequest.getAccountNumber())
                        .accountName(user.get().getFirstName() + user.get().getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest debitRequest) {
        boolean accountExist = userRepo.existsByAccountNumber(debitRequest.getAccountNumber());
        if(!accountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build();
        }
        Optional<User> user = userRepo.findByAccountNumber(debitRequest.getAccountNumber());
        BigDecimal availableBalance = user.get().getAccountBalance();
        if(availableBalance.compareTo(debitRequest.getAmount()) <= 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(user.get().getAccountBalance())
                            .accountNumber(debitRequest.getAccountNumber())
                            .accountName(user.get().getFirstName() + user.get().getLastName())
                            .build())
                    .build();
        }
        BigDecimal updatedNum = availableBalance.subtract(debitRequest.getAmount());
        user.get().setAccountBalance(updatedNum);
        userRepo.save(user.get());

        Transaction transaction = Transaction.builder()
                .accountNumber(user.get().getAccountNumber())
                .transactionType("DEBIT")
                .amount(debitRequest.getAmount())
                .build();

        transactionService.saveTransaction(transaction);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(user.get().getAccountBalance())
                        .accountNumber(debitRequest.getAccountNumber())
                        .accountName(user.get().getFirstName() + user.get().getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        boolean sourceAccountExist = userRepo.existsByAccountNumber(transferRequest.getSourceAccountNumber());
        boolean destinationAccountExist = userRepo.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if(!(sourceAccountExist || destinationAccountExist)){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build();
        }
        Optional<User> sourceUser = userRepo.findByAccountNumber(transferRequest.getSourceAccountNumber());
        Optional<User> destinationUser = userRepo.findByAccountNumber(transferRequest.getDestinationAccountNumber());

        BigDecimal sourceUserBalance = sourceUser.get().getAccountBalance();
        BigDecimal destinationUserBalance = destinationUser.get().getAccountBalance();

        if(sourceUserBalance.compareTo(transferRequest.getAmount()) <= 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(sourceUser.get().getAccountBalance())
                            .accountNumber(transferRequest.getSourceAccountNumber())
                            .accountName(sourceUser.get().getFirstName() + sourceUser.get().getLastName())
                            .build())
                    .build();
        }

        BigDecimal sourceUserBalanceNewBalance = sourceUserBalance.subtract(transferRequest.getAmount());
        BigDecimal destinationUserBalanceNewBalance = destinationUserBalance.add(transferRequest.getAmount());
        sourceUser.get().setAccountBalance(sourceUserBalanceNewBalance);
        destinationUser.get().setAccountBalance(destinationUserBalanceNewBalance);
        userRepo.save(sourceUser.get());
        userRepo.save(destinationUser.get());

        Transaction transaction = Transaction.builder()
                .accountNumber(destinationUser.get().getAccountNumber())
                .transactionType("TRANSFER")
                .amount(transferRequest.getAmount())
                .build();

        transactionService.saveTransaction(transaction);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(destinationUser.get().getAccountBalance())
                        .accountNumber(transferRequest.getDestinationAccountNumber())
                        .accountName(destinationUser.get().getFirstName() + destinationUser.get().getLastName())
                        .build())
                .build();
    }
}
