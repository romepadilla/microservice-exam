package com.microservice.exam.microservice_test.DTO;

import lombok.Data;
import java.util.List;

@Data
public class AccountCreationRequest {
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String address1;
    private String address2;
    private String accountType;

    private List<SavingsAccountRequest> savings;
}


