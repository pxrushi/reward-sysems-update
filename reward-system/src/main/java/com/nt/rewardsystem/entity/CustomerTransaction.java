package com.nt.rewardsystem.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Customer_Transaction_Tab")
public class CustomerTransaction {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    //@NotBlank(message = "customer id cannot be null")
    private Customer customer;

    @Positive(message = "Transaction amount must be positive")
    private Double amount;

    @PastOrPresent(message = "Transaction date must be in the past or present")
   // @NotBlank(message = "transactionDate cannot be null")
    private LocalDate transactionDate;
    
    private Integer points;
 
    public CustomerTransaction() {
		System.out.println("CustomerTransaction.CustomerTransaction()");
	}
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "CustomerTransaction [id=" + id + ", customer=" + customer + ", amount=" + amount + ", transactionDate="
				+ transactionDate + "]";
	}
    
}