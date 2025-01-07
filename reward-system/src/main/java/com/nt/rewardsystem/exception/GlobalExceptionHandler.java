package com.nt.rewardsystem.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nt.rewardsystem.controller.CustomerController;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	 public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) { 
		logger.info("GlobalExceptionHandler : handleValidationExceptions");
        BindingResult result = ex.getBindingResult();
        
     // Collect error messages for each validation failure
        List<ErrorResponse> errorMessages = result.getAllErrors().stream()
            .map(error -> new ErrorResponse(error.getDefaultMessage())) 
            .collect(Collectors.toList());  

        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex) {
		logger.info("GlobalExceptionHandler : handleCustomerNotFoundException");
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        response.put("path", "/api/customers"); 

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Object> handleTransactionNotFoundException(TransactionNotFoundException ex) {
		logger.info("GlobalExceptionHandler : handleTransactionNotFoundException");
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        response.put("path", "/api/transaction"); 

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

	
}