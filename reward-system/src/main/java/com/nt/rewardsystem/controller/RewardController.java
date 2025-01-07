package com.nt.rewardsystem.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.rewardsystem.entity.RewardPoints;
import com.nt.rewardsystem.service.RewardService;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {
	@Autowired 
	private  RewardService rewardService;
	
	private static final Logger logger = LoggerFactory.getLogger(RewardController.class);


	//fetch reward by customer id
	    @GetMapping("/customer/{customerId}")
	    public ResponseEntity<List<RewardPoints>> getRewardsByCustomer(@PathVariable Long customerId) {
	    	logger.info("RewardController : getRewardsByCustomer "+customerId);
	        return ResponseEntity.ok(rewardService.getRewardsByCustomerId(customerId));
	    }
	    
	    //fetch all rewards
	    @GetMapping("/fetchAllRewards")
	    public ResponseEntity<List<RewardPoints>>getAllRewardPoints(){
	    	logger.info("RewardController : getAllRewardPoints ");
	    	List<RewardPoints>list=rewardService.fetchAllRewards();
	    	return new ResponseEntity<List<RewardPoints>>(list,HttpStatus.OK);
	    }
	    
	    
	}