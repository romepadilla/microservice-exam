package com.microservice.exam.microservice_test.DTO;

import com.microservice.exam.microservice_test.Model.Account;
import com.microservice.exam.microservice_test.Model.Customer;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomerInquiryResponse {
    private Long customerNumber;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String address1;
    private String address2;
    private List<AccountResponse> savings;

    public CustomerInquiryResponse(Customer customer, List<Account> accounts) {
        this.customerNumber = customer.getCustomerNumber();
        this.customerName = customer.getCustomerName();
        this.customerMobile = customer.getCustomerMobile();
        this.customerEmail = customer.getCustomerEmail();
        this.address1 = customer.getAddress1();
        this.address2 = customer.getAddress2();
        this.savings = accounts.stream().map(AccountResponse::new).collect(Collectors.toList());
    }

    @Data
    public static class AccountResponse {
        private Long accountNumber;
        private String accountType;
        private Double availableBalance;

        public AccountResponse(Account account) {
            this.accountNumber = account.getAccountNumber();
            this.accountType = account.getAccountType();
            this.availableBalance = account.getAvailableBalance();
        }
    }
}
