package com.bank.bank.Service;

import com.bank.bank.Dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
