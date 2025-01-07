package com.nt.rewardsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nt.rewardsystem.entity.Customer;
import com.nt.rewardsystem.entity.CustomerTransaction;
import com.nt.rewardsystem.exception.CustomerNotFoundException;
import com.nt.rewardsystem.exception.TransactionNotFoundException;
import com.nt.rewardsystem.repo.CustomerTransactionRepository;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private CustomerTransactionRepository transactionRepository;

    private Customer customer;
    private CustomerTransaction transaction1;
    private CustomerTransaction transaction2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("sachin");
        customer.setEmail("sachin@gmail.com");

        transaction1 = new CustomerTransaction();
        transaction1.setId(1L);
        transaction1.setCustomer(customer);
        transaction1.setAmount(100.0);

        transaction2 = new CustomerTransaction();
        transaction2.setId(2L);
        transaction2.setCustomer(customer);
        transaction2.setAmount(200.0);
    }

    @Test
    void testGetAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        List<CustomerTransaction> transactions = transactionService.getAllTransactions();

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetTransactionsByCustomerId_ExistingCustomer() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        List<CustomerTransaction> transactions = transactionService.getTransactionsByCustomerId(1L);

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertEquals(1L, transactions.get(0).getCustomer().getId());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetTransactionsByCustomerId_NonExistingCustomer() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            transactionService.getTransactionsByCustomerId(999L);
        });

        assertEquals("Customer with ID 999 not found or has no transactions.", exception.getMessage());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testSaveTransaction() {
        when(transactionRepository.save(transaction1)).thenReturn(transaction1);

        CustomerTransaction savedTransaction = transactionService.saveTransaction(transaction1);

        assertNotNull(savedTransaction);
        assertEquals(1L, savedTransaction.getId());
        verify(transactionRepository, times(1)).save(transaction1);
    }

    @Test
    void testDeleteTransaction_ExistingTransaction() {
        when(transactionRepository.existsById(1L)).thenReturn(true);

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).existsById(1L);
        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTransaction_NonExistingTransaction() {
        when(transactionRepository.existsById(999L)).thenReturn(false);

        Exception exception = assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.deleteTransaction(999L);
        });

        assertEquals("Transaction with ID 999 not found.", exception.getMessage());
        verify(transactionRepository, times(1)).existsById(999L);
        verify(transactionRepository, never()).deleteById(999L);
    }
}
