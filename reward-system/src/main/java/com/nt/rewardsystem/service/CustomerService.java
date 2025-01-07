package com.nt.rewardsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.rewardsystem.entity.Customer;
import com.nt.rewardsystem.exception.CustomerNotFoundException;
import com.nt.rewardsystem.repo.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public List<Customer> getAllCustomers() {
    	logger.info("CustomerService : getAllCustomers ");
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
    	logger.info("CustomerService : getCustomerById "+id);
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id "+id));
    }

    public Customer saveCustomer(Customer customer) {
    	logger.info("CustomerService : saveCustomer "+customer);
        return customerRepository.save(customer);
    }
    
    @Transactional
    public void deleteCustomerById(Long id) {
    	logger.info("CustomerService : deleteCustomerById "+id);
        if (!customerRepository.existsById(id)) {
        	throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        customerRepository.deleteById(id);
    }
    
    //update customer
    @Transactional
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
    	logger.info("CustomerService : updateCustomer start "+id);
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " does not exist."));

        // Update the fields of the existing customer
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        logger.info("CustomerService : updateCustomer end "+id);
        // Save and return the updated customer
        return customerRepository.save(existingCustomer);
    }
}