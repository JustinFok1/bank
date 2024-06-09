package com.bank.bank.Controllers;

import com.bank.bank.Dto.*;
import com.bank.bank.Repo.UserRepo;
import com.bank.bank.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@Tag(name = "User Account Management APIs")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(
            summary = "Create New User Account",
            description = "Creating a new user and assigning account ID"
    )
   @PostMapping
   public BankResponse createAccount(@RequestBody UserRequest userRequest){
       return userService.createAccount(userRequest);
   }
    @Operation(
            summary = "Credit Money",
            description = "Credit money"
    )
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditRequest){
        return userService.creditAccount(creditRequest);
    }
    @Operation(
            summary = "Debit Money",
            description = "Debit money"
    )
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest debitRequest){
        return userService.debitAccount(debitRequest);
    }
    @Operation(
            summary = "Transfer Money",
            description = "Transfer money"
    )
    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest){
        return userService.transfer(transferRequest);
    }
    @Operation(
            summary = "Login user",
            description = "Authenticate login token"
    )
    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }
    @Operation(
            summary = "Delete Account",
            description = "Delete account"
    )
   @DeleteMapping("/delete")
    public BankResponse deleteAccount(@RequestBody AccountInfo accountInfo){
       return userService.deleteAccount(accountInfo);
   }
    @Operation(
            summary = "Check Balance",
            description = "Check balance"
    )
   @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryReq enquiryReq){
       return userService.balanceEnquiry(enquiryReq);
   }
    @Operation(
            summary = "Check Username",
            description = "Check username"
    )
   @GetMapping("/nameEnquiry")
   public String nameEnquiry(@RequestBody EnquiryReq enquiryReq){
       return userService.nameEnquiry(enquiryReq);
   }
}
