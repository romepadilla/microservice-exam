package com.microservice.exam.microservice_test.Controller;

import com.microservice.exam.microservice_test.DTO.AccountCreationRequest;
import com.microservice.exam.microservice_test.DTO.SavingsAccountRequest;
import com.microservice.exam.microservice_test.Model.Customer;
import com.microservice.exam.microservice_test.Repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountControllerTest {

    @Autowired
    private AccountController accountController;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private AccountCreationRequest request;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        request = new AccountCreationRequest();
        request.setCustomerName("Test");
        request.setCustomerMobile("123456789");
        request.setCustomerEmail("test@example.com");
        request.setAddress1("123 Street");
        request.setAddress2("456 Road");

        SavingsAccountRequest savingsAccountRequest = new SavingsAccountRequest();
        savingsAccountRequest.setAccountNumber(123456789L);
        savingsAccountRequest.setAccountType("Savings");
        savingsAccountRequest.setAvailableBalance(1000.0);

        request.setSavings(Arrays.asList(savingsAccountRequest));
    }

    @Test
    public void testCreateAccount() throws Exception {
        mockMvc.perform(post("/api/v1/account")
                        .contentType("application/json")
                        .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionStatusCode").value(201))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer account created"))
                .andExpect(jsonPath("$.customerNumber").isNotEmpty())
                .andExpect(jsonPath("$.customerName").value("Test"));
    }

    @Test
    //runs independently
    public void testGetCustomerDetails() throws Exception {
        mockMvc.perform(post("/api/v1/account")
                        .contentType("application/json")
                        .content(asJsonString(request)))
                .andExpect(status().isCreated());

        Customer customer = customerRepository.findAll().get(0);
        Long customerNumber = customer.getCustomerNumber();

        mockMvc.perform(get("/api/v1/account/{customerNumber}", customerNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionStatusCode").value(302))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer Account found"))
                .andExpect(jsonPath("$.customerNumber").value(customerNumber))
                .andExpect(jsonPath("$.customerName").value("Test"))
                .andExpect(jsonPath("$.customerEmail").value("test@example.com"));
    }

    @Test
    public void testGetCustomerDetailsCustomerNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/account/{customerNumber}", 999L))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.transactionStatusCode").value(401))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer not found"));
    }

    @Test
    public void testCreateAccountWithInvalidEmail() throws Exception {
        AccountCreationRequest request = new AccountCreationRequest();
        request.setCustomerName("Test");
        request.setCustomerMobile("123456789");
        request.setCustomerEmail("invalid-email");
        request.setAddress1("123 Street");
        request.setAddress2("456 Road");

        mockMvc.perform(post("/api/v1/account")
                        .contentType("application/json")
                        .content(asJsonString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.transactionStatusCode").value(401))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Please enter a correct email format"));
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON string", e);
        }
    }
}
