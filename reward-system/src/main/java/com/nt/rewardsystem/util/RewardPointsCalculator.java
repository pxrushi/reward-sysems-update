package com.nt.rewardsystem.util;

public class RewardPointsCalculator {
	
	public static int calculateRewardPoints(double amount) {
		 int points = 0;

	        // If the amount spent is more than 100
	        if (amount > 100) {
	            // 2 points for every dollar over 100
	            points += (amount - 100) * 2;
	            // 1 point for every dollar from 50 to 100
	            points += 50;  
	        } 
	        // If the amount is between 50 and 100
	        else if (amount >= 50) {
	            // 1 point for every dollar spent from 50 to 100
	            points += amount - 50;
	        }

	        return points;
	    }
}