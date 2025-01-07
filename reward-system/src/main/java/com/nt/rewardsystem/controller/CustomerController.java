package com.nt.rewardsystem.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.rewardsystem.entity.Customer;
import com.nt.rewardsystem.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);


	//get all customer
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
    	logger.info("CustomerController : get all customers");
    	//call service
    	List<Customer>list=customerService.getAllCustomers();
    	return new ResponseEntity<List<Customer>>(list,HttpStatus.OK);
    }

    //get one customer
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
    	logger.info("CustomerController : getCustomerById "+ id);
    	//call service
    	Customer customer = customerService.getCustomerById(id);
    	return new ResponseEntity<Customer>(customer,HttpStatus.OK);
    }

    //save customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
    	//call service
    	logger.info("CustomerController : before call save customer");
    	Customer saveCustomer = customerService.saveCustomer(customer);
    	logger.info("CustomerController : after call save customer");
    	return new ResponseEntity<Customer>(saveCustomer,HttpStatus.OK);
    }
    
    //delete customer
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Long id) {
    	logger.info("CustomerController : deleteCustomerById "+id);
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer with ID " + id + " has been deleted successfully.");
    }
    
    //edit customer
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer updatedCustomer) {
    	logger.info("CustomerController : updateCustomer "+id);
        Customer customer = customerService.updateCustomer(id, updatedCustomer);
        return ResponseEntity.ok(customer);
    }
    
    //this api is to just check we are login with which user ADMIN/USER
    @GetMapping("/current-user")
    public ResponseEntity<String> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String role = null;
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            role = "Logged in as: " + userDetails.getUsername() + " with roles: " + userDetails.getAuthorities();
        } else {
            role =  "Logged in as: " + principal.toString();
        }
        return new ResponseEntity<String>(role,HttpStatus.OK);
    }
    
    
}