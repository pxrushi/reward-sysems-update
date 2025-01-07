package com.nt.rewardsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.rewardsystem.entity.CustomerTransaction;

public interface CustomerTransactionRepository extends JpaRepository<CustomerTransaction, Long> { 
	
}
