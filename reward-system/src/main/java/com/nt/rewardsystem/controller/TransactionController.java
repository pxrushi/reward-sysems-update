package com.nt.rewardsystem.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.rewardsystem.entity.CustomerTransaction;
import com.nt.rewardsystem.entity.RewardPoints;
import com.nt.rewardsystem.service.RewardService;
import com.nt.rewardsystem.service.TransactionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Controller", description = "Operations related to transactions")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private RewardService rewardService;

	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

	// fetch all transaction
	@GetMapping
	public ResponseEntity<List<CustomerTransaction>> getAllTransactions() {
		logger.info("TransactionController : getAllTransactions");
		return ResponseEntity.ok(transactionService.getAllTransactions());
	}

	@GetMapping("/customer/rewards/{customerId}")
	public ResponseEntity<List<RewardPoints>> getRewardsByCustomer(@PathVariable Long customerId) {
		logger.info("RewardController : getRewardsByCustomer " + customerId);
		return ResponseEntity.ok(rewardService.getRewardsByCustomerId(customerId));
	}

	// get transaction by transactin id
	@GetMapping("/customerTransaction/{customerId}")
	public ResponseEntity<List<CustomerTransaction>> getTransactionsByCustomer(@PathVariable Long customerId) {
		logger.info("TransactionController : getTransactionsByCustomer " + customerId);
		return ResponseEntity.ok(transactionService.getTransactionsByCustomerId(customerId));
	}

	// create transaction
	@PostMapping
	public ResponseEntity<RewardPoints> createTransaction(@Valid @RequestBody CustomerTransaction transaction) {
		logger.info("TransactionController : createTransaction " + transaction);
		CustomerTransaction savedTransaction = transactionService.saveTransaction(transaction);
		logger.info("TransactionController : after save transaction " + transaction);
		RewardPoints rewardPoits = rewardService.calculateAndSaveRewards(savedTransaction);
		return ResponseEntity.ok(rewardPoits);
	}

	// delete transaction
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
		logger.info("TransactionController : deleteTransaction " + id);
		transactionService.deleteTransaction(id);
		return ResponseEntity.ok("Transaction with ID " + id + " has been deleted successfully.");
	}
}