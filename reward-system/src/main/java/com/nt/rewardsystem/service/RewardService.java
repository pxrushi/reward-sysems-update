package com.nt.rewardsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.rewardsystem.entity.Customer;
import com.nt.rewardsystem.entity.CustomerTransaction;
import com.nt.rewardsystem.entity.RewardPoints;
import com.nt.rewardsystem.exception.CustomerNotFoundException;
import com.nt.rewardsystem.repo.RewardPointsRepository;
import com.nt.rewardsystem.util.RewardPointsCalculator;

@Service
public class RewardService {
	@Autowired
	private  RewardPointsRepository rewardPointsRepository;
	
	@Autowired
	private CustomerService customerService;
	
	private static final Logger logger = LoggerFactory.getLogger(RewardService.class);

	//here the logic is if customer is doing transaction in same month and year then we are update the points
	//or we will create new object and save
    public RewardPoints calculateAndSaveRewards(CustomerTransaction transaction) {
    	logger.info("RewardService : calculateAndSaveRewards start"+transaction);
        int points = RewardPointsCalculator.calculateRewardPoints(transaction.getAmount());
        Customer customer = customerService.getCustomerById(transaction.getCustomer().getId()); 
        //create return reward
        RewardPoints returnReward = new RewardPoints();
        returnReward.setPoints(points);
        returnReward.setCustomer(customer);
        returnReward.setCustomer(transaction.getCustomer());
        returnReward.setCustomer(customer);
        returnReward.setMonth(transaction.getTransactionDate().getMonthValue());
        returnReward.setYear(transaction.getTransactionDate().getYear());
    	
        if(transaction.getCustomer().getId()!=null) {
    		List<RewardPoints>rewardsPointsList=rewardPointsRepository.findByCustomerId(transaction.getCustomer().getId());
    		System.out.println(rewardsPointsList);
    		//check date and year 
    		for(RewardPoints point:rewardsPointsList) {
    			//checking the customer id are matching or not
    			if(point.getCustomer().getId().equals(transaction.getCustomer().getId())) {
    				//checking year and month are matching or not
    			    if(point.getYear()==transaction.getTransactionDate().getYear() && 
    					point.getMonth()==transaction.getTransactionDate().getMonthValue()) {
    					//create new reward object
    			    	 RewardPoints reward = new RewardPoints();
    			    	//update the points previous points + new point
    			    	points+=point.getPoints();
    			    	reward.setId(point.getId());
    			    	reward.setPoints(points);
    			    	reward.setCustomer(transaction.getCustomer());
    			        reward.setCustomer(customer);
    			        reward.setMonth(transaction.getTransactionDate().getMonthValue());
    			        reward.setYear(transaction.getTransactionDate().getYear());
    			        logger.info("RewardService : calculateAndSaveRewards rewars points updated to "+points+ "for customer id "+transaction.getCustomer().getId());
    			        rewardPointsRepository.save(reward);
    			        return returnReward;
    				}
    			}
    		}
    		
        }
    	//if its a new object
    	//directly created and sava
        RewardPoints reward = new RewardPoints();
        reward.setCustomer(transaction.getCustomer());
        reward.setCustomer(customer);
        reward.setMonth(transaction.getTransactionDate().getMonthValue());
        reward.setYear(transaction.getTransactionDate().getYear());
        reward.setPoints(points);
        logger.info("RewardService : calculateAndSaveRewards end ");
        return rewardPointsRepository.save(reward);
    }

    public List<RewardPoints> getRewardsByCustomerId(Long customerId) {
    	logger.info("RewardService : getRewardsByCustomerId " + customerId);

        // Filter rewards by customer ID
        List<RewardPoints> rewards = rewardPointsRepository.findAll()
                .stream()
                .filter(r -> r.getCustomer().getId().equals(customerId))
                .toList();

        // Throw exception if no rewards are found for the customer
        if (rewards.isEmpty()) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }

        return rewards;
    }
    
    public List<RewardPoints> fetchAllRewards() {
    	logger.info("RewardService : fetchAllRewards");
        return rewardPointsRepository.findAll();
    }
}
