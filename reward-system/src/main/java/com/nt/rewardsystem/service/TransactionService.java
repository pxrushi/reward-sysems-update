package com.nt.rewardsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.rewardsystem.entity.CustomerTransaction;
import com.nt.rewardsystem.exception.CustomerNotFoundException;
import com.nt.rewardsystem.exception.TransactionNotFoundException;
import com.nt.rewardsystem.repo.CustomerTransactionRepository;
import com.nt.rewardsystem.util.RewardPointsCalculator;

@Service
public class TransactionService {
	
	@Autowired
	 private  CustomerTransactionRepository transactionRepository;
	
	 private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);


	    public List<CustomerTransaction> getAllTransactions() {
	    	logger.info("TransactionService : getAllTransactions");
	        return transactionRepository.findAll();
	    }

	    public List<CustomerTransaction> getTransactionsByCustomerId(Long customerId) {
	        logger.info("TransactionService : getTransactionsByCustomerId " + customerId);

	        // Fetch transactions for the customer ID
	        List<CustomerTransaction> transactions = transactionRepository.findAll()
	                .stream()
	                .filter(t -> t.getCustomer().getId().equals(customerId))
	                .toList();

	        // Check if transactions are empty and throw exception 
	        if (transactions.isEmpty()) {
	            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found or has no transactions.");
	        }

	        return transactions;
	    }


	    public CustomerTransaction saveTransaction(CustomerTransaction transaction) {
	    	logger.info("TransactionService : saveTransaction "+transaction);
	    	//get rewards points
	    	int points = RewardPointsCalculator.calculateRewardPoints(transaction.getAmount());
	    	transaction.setPoints(points);
	        return transactionRepository.save(transaction);
	    }

	    public void deleteTransaction(Long id) {
	        logger.info("TransactionService : deleteTransaction " + id);

	        // Check if the transaction before delete
	        if (!transactionRepository.existsById(id)) {
	            throw new TransactionNotFoundException("Transaction with ID " + id + " not found.");
	        }

	        transactionRepository.deleteById(id);
	    }
	}