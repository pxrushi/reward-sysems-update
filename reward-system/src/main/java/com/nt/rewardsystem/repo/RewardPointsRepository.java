package com.nt.rewardsystem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.rewardsystem.entity.RewardPoints;

public interface RewardPointsRepository extends JpaRepository<RewardPoints, Long> {
	
	public List<RewardPoints> findByCustomerId(Long customerId);
}