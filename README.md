# reward-sysems-update
Main aim of reward systems project is to give points to the customer bases on transaction they do.

This project is a Spring Boot-based application designed to manage and track a reward system. 
It allows users to earn reward points, track their reward points , efficiently, providing a seamless 
and scalable solution for businesses looking to implement for customer.

Features

User Management:

Register, update, and delete user profiles.

Authentication and authorization using Spring Security.

Rewards Management:
As pertransaction calculate reward point and store in database for every user.
if user did transaction in same month and same year then update the reward points else create new object for reward.

getAllCustomers , getCustomerById, createCustomer ,deleteCustomerById ,updateCustomer
getAllTransactions, getRewardsByCustomer, getTransactionsByCustomer ,createTransaction ,deleteTransaction

Track reward points for users.


Transaction Management:

Log transactions related to earning and rewards.

View transaction history per user.

RESTful APIs:

Expose endpoints for all operations, making it easy to integrate with frontend applications or third-party systems.

Database Integration:

Use JPA with Hibernate for ORM.

Technologies Used

Backend Framework: Spring Boot

Database: Oracle Database

ORM: Spring Data JPA

Build Tool: Maven

Security: Spring Security
