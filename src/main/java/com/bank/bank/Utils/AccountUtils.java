package com.bank.bank.Utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "It looks like you already have a account. Try logging in.";
    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account successfully created";
    public static final String ACCOUNT_DELETED_CODE = "003";
    public static final String ACCOUNT_DELETED_MESSAGE = "Account successfully deleted";
    public static final String ACCOUNT_NOT_FOUND_CODE = "004";
    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";
    public static final String ACCOUNT_FOUND_CODE = "005";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account found";
    public static final String ACCOUNT_CREDITED_CODE = "006";
    public static final String ACCOUNT_CREDITED_MESSAGE = "Account credited";
    public static final String ACCOUNT_DEBITED_CODE = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account debited";
    public static final String INSUFFICIENT_FUNDS_CODE = "008";
    public static final String INSUFFICIENT_FUNDS_MESSAGE = "Error: Not enough funds";
    public static final String TRANSFER_SUCCESSFUL_CODE = "009";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer successful";



    public static String generateAccountNumber(){
        Year currentYear  = Year.now();
        int min = 100000;
        int max = 999999;

        int randomNumber = (int)Math.floor(Math.random() * (max - min + 1) + min);

        String year = String.valueOf(currentYear);
        String randomNum = String.valueOf(randomNumber);

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomNum).toString();
    }
}
