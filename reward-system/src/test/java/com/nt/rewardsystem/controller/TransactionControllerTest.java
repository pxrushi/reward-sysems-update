package com.nt.rewardsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import com.nt.rewardsystem.entity.Customer;
import com.nt.rewardsystem.entity.CustomerTransaction;
import com.nt.rewardsystem.entity.RewardPoints;
import com.nt.rewardsystem.service.RewardService;
import com.nt.rewardsystem.service.TransactionService;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @Mock
    private RewardService rewardService;

    private CustomerTransaction customerTransaction;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        // Setting up common test data
        customer = new Customer(1L, "sachin", "sachin@gmail.com");
        customerTransaction = new CustomerTransaction();
        customerTransaction.setId(1L);
        customerTransaction.setCustomer(customer);
        customerTransaction.setAmount(500.0);
        customerTransaction.setTransactionDate(LocalDate.now());
    }

    @Test
    public void testGetAllTransactions() {
        List<CustomerTransaction> transactions = Arrays.asList(customerTransaction);
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        ResponseEntity<List<CustomerTransaction>> response = transactionController.getAllTransactions();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetTransactionsByCustomer() {
        List<CustomerTransaction> transactions = Arrays.asList(customerTransaction);
        when(transactionService.getTransactionsByCustomerId(anyLong())).thenReturn(transactions);

        ResponseEntity<List<CustomerTransaction>> response = transactionController.getTransactionsByCustomer(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testCreateTransaction() {
        RewardPoints rewardPoints = new RewardPoints(); // 
        when(transactionService.saveTransaction(any(CustomerTransaction.class))).thenReturn(customerTransaction);
        when(rewardService.calculateAndSaveRewards(any(CustomerTransaction.class))).thenReturn(rewardPoints);

        ResponseEntity<RewardPoints> response = transactionController.createTransaction(customerTransaction);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteTransaction() {
        doNothing().when(transactionService).deleteTransaction(anyLong());

        ResponseEntity<String> response = transactionController.deleteTransaction(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Transaction with ID 1 has been deleted successfully.", response.getBody());
    }

    /*@Test
    public void testDeleteTransactionUnauthorized() {
        // Testing access control with the wrong role
        doNothing().when(transactionService).deleteTransaction(anyLong());

        // Simulating a user with insufficient rights
        // (The @WithMockUser annotation is used in the test above)
        ResponseEntity<String> response = transactionController.deleteTransaction(1L);

        assertNotEquals(200, response.getStatusCodeValue());
    }*/

}
