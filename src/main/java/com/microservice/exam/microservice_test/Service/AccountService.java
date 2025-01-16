package com.microservice.exam.microservice_test.Service;

import com.microservice.exam.microservice_test.DTO.AccountCreationRequest;
import com.microservice.exam.microservice_test.DTO.SavingsAccountRequest;
import com.microservice.exam.microservice_test.DTO.TransactionResponse;
import com.microservice.exam.microservice_test.Exception.CustomerNotFoundException;
import com.microservice.exam.microservice_test.Model.Account;
import com.microservice.exam.microservice_test.Model.Customer;
import com.microservice.exam.microservice_test.Repository.AccountRepository;
import com.microservice.exam.microservice_test.Repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public AccountService(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public TransactionResponse createAccount(AccountCreationRequest request){
        Customer customer = new Customer();
        customer.setCustomerName(request.getCustomerName());
        customer.setCustomerMobile(request.getCustomerMobile());
        customer.setCustomerEmail(request.getCustomerEmail());
        customer.setAddress1(request.getAddress1());
        customer.setAddress2(request.getAddress2());


        if (request.getSavings() != null) {
            Customer finalCustomer = customer;
            customer.setAccounts(request.getSavings().stream().map(savingsAccount -> {
                Account account = new Account();
                account.setAccountNumber(savingsAccount.getAccountNumber());
                account.setAccountType(savingsAccount.getAccountType());
                account.setAvailableBalance(savingsAccount.getAvailableBalance());
                account.setCustomer(finalCustomer);
                return account;
            }).collect(Collectors.toList()));
        }

        if (customer.getAccounts() == null) {
            customer.setAccounts(new ArrayList<>());
        }

        for (Account account : customer.getAccounts()) {
            if (!"Savings".equals(account.getAccountType()) && !"Checking".equals(account.getAccountType())) {
                return new TransactionResponse(null, 400, "Account type should only be Savings or Checking");
            }
        }

        if (customer.getCustomerEmail().isEmpty()) {
            return new TransactionResponse(null, 400, "Email is a required field");
        }

        customer = customerRepository.save(customer);
        return new TransactionResponse(customer.getCustomerNumber(), 201, "Customer account created");
    }

    public TransactionResponse getCustomerDetails(Long customerNumber) {
        Customer customer = customerRepository.findById(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        List<Account> accountList = accountRepository.findByCustomerCustomerNumber(customerNumber);
        List<SavingsAccountRequest> savingsAccounts = new ArrayList<>();

        for (Account account : accountList) {
            if ("Savings".equals(account.getAccountType())) {
                SavingsAccountRequest savingsAccount = new SavingsAccountRequest(account.getAccountNumber(),
                        account.getAccountType(), account.getAvailableBalance());
                savingsAccounts.add(savingsAccount);
            }
        }

        return new TransactionResponse(customer.getCustomerNumber(), customer.getCustomerName(),
                customer.getCustomerMobile(), customer.getCustomerEmail(), customer.getAddress1(),
                customer.getAddress2(), savingsAccounts, 302, "Customer Account found");
    }

}
