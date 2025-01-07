package com.nt.rewardsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.nt.rewardsystem.entity.Customer;
import com.nt.rewardsystem.service.CustomerService;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        // Arrange
        List<Customer> mockCustomers = new ArrayList<>();
        mockCustomers.add(new Customer(1L, "sachin", "sachin@gmail.com"));
        mockCustomers.add(new Customer(2L, "Jane Doe", "jane.doe@example.com"));

        when(customerService.getAllCustomers()).thenReturn(mockCustomers);

        // Act
        ResponseEntity<List<Customer>> response = customerController.getAllCustomers();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void testGetCustomerById() {
        // Arrange
        Long customerId = 1L;
        Customer mockCustomer = new Customer(customerId, "Sachin", "sachin@gmail.com");

        when(customerService.getCustomerById(customerId)).thenReturn(mockCustomer);

       
        ResponseEntity<Customer> response = customerController.getCustomerById(customerId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Sachin", response.getBody().getName());
        verify(customerService, times(1)).getCustomerById(customerId);
    }

    @Test
    void testCreateCustomer() {
        // Arrange
        Customer mockCustomer = new Customer(null, "Sachin", "sachin@gmail.com");
        Customer savedCustomer = new Customer(1L, "Sachin", "sachine@gmail.com");

        when(customerService.saveCustomer(mockCustomer)).thenReturn(savedCustomer);

       
        ResponseEntity<Customer> response = customerController.createCustomer(mockCustomer);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Sachin", response.getBody().getName());
        verify(customerService, times(1)).saveCustomer(mockCustomer);
    }

    @Test
    void testDeleteCustomerById() {
       
        Long customerId = 1L;

        doNothing().when(customerService).deleteCustomerById(customerId);
        ResponseEntity<String> response = customerController.deleteCustomerById(customerId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Customer with ID 1 has been deleted successfully.", response.getBody());
        verify(customerService, times(1)).deleteCustomerById(customerId);
    }

    @Test
    void testUpdateCustomer() {
        // Arrange
        Long customerId = 1L;
        Customer updatedCustomer = new Customer(customerId, "John Smith", "john.smith@gmail.com");
        Customer savedCustomer = new Customer(customerId, "John Smith", "john.smith@gmail.com");

        when(customerService.updateCustomer(customerId, updatedCustomer)).thenReturn(savedCustomer);

        // Act
        ResponseEntity<Customer> response = customerController.updateCustomer(customerId, updatedCustomer);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Smith", response.getBody().getName());
        verify(customerService, times(1)).updateCustomer(customerId, updatedCustomer);
    }

 
}
