package com.nt.rewardsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.nt.rewardsystem.entity.Customer;
import com.nt.rewardsystem.exception.CustomerNotFoundException;
import com.nt.rewardsystem.repo.CustomerRepository;

@SpringBootTest
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers_Success() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "sachin", "sachin@gmail.com"));
        customers.add(new Customer(2L, "Jane Smith", "jane.smith@gmail.com"));

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetCustomerById_Success() {
        Customer customer = new Customer(1L, "sachin", "sachin@gmail.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals("sachin", result.getName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerById_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveCustomer_Success() {
        Customer customer = new Customer(null, "sachin", "sachin@gmail.com");
        Customer savedCustomer = new Customer(1L, "sachin", "sachin@gmail.com");

        when(customerRepository.save(customer)).thenReturn(savedCustomer);

        Customer result = customerService.saveCustomer(customer);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testDeleteCustomerById_Success() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        customerService.deleteCustomerById(1L);

        verify(customerRepository, times(1)).existsById(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCustomerById_CustomerNotFound() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomerById(1L));
        verify(customerRepository, times(1)).existsById(1L);
        verify(customerRepository, times(0)).deleteById(1L);
    }

    @Test
    void testUpdateCustomer_Success() {
        Customer existingCustomer = new Customer(1L, "sachin", "sachin@gmail.com");
        Customer updatedCustomer = new Customer(null, "Johnny Updated", "johnny.updated@gmail.com");
        Customer savedCustomer = new Customer(1L, "Johnny Updated", "johnny.updated@gmail.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(savedCustomer);

        Customer result = customerService.updateCustomer(1L, updatedCustomer);

        assertNotNull(result);
        assertEquals("Johnny Updated", result.getName());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    void testUpdateCustomer_CustomerNotFound() {
        Customer updatedCustomer = new Customer(null, "Johnny Updated", "johnny.updated@gmail.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(1L, updatedCustomer));
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(0)).save(any(Customer.class));
    }
}
