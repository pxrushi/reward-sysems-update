package com.nt.rewardsystem.exception;

public class CustomerNotFoundException extends RuntimeException {
    
	public CustomerNotFoundException(String message) {
        super(message);
    }
}
