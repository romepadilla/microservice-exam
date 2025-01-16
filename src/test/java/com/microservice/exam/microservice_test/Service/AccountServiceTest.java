package com.microservice.exam.microservice_test.Service;

import com.microservice.exam.microservice_test.DTO.AccountCreationRequest;
import com.microservice.exam.microservice_test.DTO.SavingsAccountRequest;
import com.microservice.exam.microservice_test.DTO.TransactionResponse;
import com.microservice.exam.microservice_test.Model.Customer;
import com.microservice.exam.microservice_test.Repository.CustomerRepository;
import com.microservice.exam.microservice_test.Repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    private AccountCreationRequest request;

    @BeforeEach
    public void setUp() {
        request = new AccountCreationRequest();
        request.setCustomerName("Test");
        request.setCustomerMobile("123456789");
        request.setCustomerEmail("john.doe@example.com");
        request.setAddress1("123 Street");
        request.setAddress2("456 Road");

        SavingsAccountRequest savingsAccountRequest = new SavingsAccountRequest();
        savingsAccountRequest.setAccountNumber(123456789L);
        savingsAccountRequest.setAccountType("Savings");
        savingsAccountRequest.setAvailableBalance(1000.0);

        request.setSavings(Arrays.asList(savingsAccountRequest));
    }

    @Test
    @Transactional
    public void testCreateAccount() {
        TransactionResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals(201, response.getTransactionStatusCode());
        assertNotNull(response.getCustomerNumber());

        Customer savedCustomer = customerRepository.findById(response.getCustomerNumber()).orElse(null);
        assertNotNull(savedCustomer);
        assertEquals("Test", savedCustomer.getCustomerName());
        assertNotNull(savedCustomer.getCustomerNumber());

        assertNotNull(savedCustomer.getAccounts());
        assertEquals(1, savedCustomer.getAccounts().size());
        assertEquals("Savings", savedCustomer.getAccounts().get(0).getAccountType());
        assertEquals(1000.0, savedCustomer.getAccounts().get(0).getAvailableBalance());
    }

    @Test
    @Transactional
    public void testCreateAccountWithInvalidEmail() {
        request.setCustomerEmail("");

        TransactionResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals(400, response.getTransactionStatusCode());
        assertEquals("Email is a required field", response.getTransactionStatusDescription());
    }

    @Test
    @Transactional
    public void testCreateAccountWithInvalidAccountType() {
        SavingsAccountRequest invalidAccountRequest = new SavingsAccountRequest();
        invalidAccountRequest.setAccountNumber(987654321L);
        invalidAccountRequest.setAccountType("InvalidType");
        invalidAccountRequest.setAvailableBalance(500.0);

        request.setSavings(Arrays.asList(invalidAccountRequest));

        TransactionResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals(400, response.getTransactionStatusCode());
        assertEquals("Account type should only be Savings or Checking", response.getTransactionStatusDescription());
    }

    @Test
    @Transactional
    public void testCreateAccountWithNoSavingsAccounts() {
        request.setSavings(null);

        TransactionResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals(201, response.getTransactionStatusCode());
        assertNotNull(response.getCustomerNumber());
    }
}
