package com.nt.rewardsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nt.rewardsystem.entity.Customer;
import com.nt.rewardsystem.entity.CustomerTransaction;
import com.nt.rewardsystem.entity.RewardPoints;
import com.nt.rewardsystem.exception.CustomerNotFoundException;
import com.nt.rewardsystem.repo.RewardPointsRepository;
import com.nt.rewardsystem.util.RewardPointsCalculator;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {

    @Mock
    private RewardPointsRepository rewardPointsRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private RewardService rewardService;

    private Customer customer;
    private CustomerTransaction transaction;
    private RewardPoints existingRewardPoints;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("sachin");
        customer.setEmail("sachin@gmail.com");

        transaction = new CustomerTransaction();
        transaction.setId(1L);
        transaction.setCustomer(customer);
        transaction.setAmount(150.0);
        transaction.setTransactionDate(LocalDate.now());

        existingRewardPoints = new RewardPoints();
        existingRewardPoints.setId(1L);
        existingRewardPoints.setCustomer(customer);
        existingRewardPoints.setMonth(LocalDate.now().getMonthValue());
        existingRewardPoints.setYear(LocalDate.now().getYear());
        existingRewardPoints.setPoints(100);
    }

    @Test
    void testCalculateAndSaveRewards_NewTransaction() {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        when(rewardPointsRepository.findByCustomerId(customer.getId())).thenReturn(List.of());
        when(rewardPointsRepository.save(any(RewardPoints.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RewardPoints rewardPoints = rewardService.calculateAndSaveRewards(transaction);

        assertThat(rewardPoints.getPoints()).isEqualTo(RewardPointsCalculator.calculateRewardPoints(transaction.getAmount()));
        assertThat(rewardPoints.getMonth()).isEqualTo(transaction.getTransactionDate().getMonthValue());
        assertThat(rewardPoints.getYear()).isEqualTo(transaction.getTransactionDate().getYear());
        verify(rewardPointsRepository).save(any(RewardPoints.class));
    }

    @Test
    void testCalculateAndSaveRewards_UpdateExistingRewards() {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        when(rewardPointsRepository.findByCustomerId(customer.getId())).thenReturn(List.of(existingRewardPoints));
        when(rewardPointsRepository.save(any(RewardPoints.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RewardPoints rewardPoints = rewardService.calculateAndSaveRewards(transaction);

        assertThat(rewardPoints.getPoints()).isEqualTo(RewardPointsCalculator.calculateRewardPoints(transaction.getAmount()) + existingRewardPoints.getPoints());
        assertThat(rewardPoints.getId()).isEqualTo(existingRewardPoints.getId());
        verify(rewardPointsRepository).save(any(RewardPoints.class));
    }

    @Test
    void testGetRewardsByCustomerId_ExistingCustomer() {
        when(rewardPointsRepository.findAll()).thenReturn(List.of(existingRewardPoints));

        List<RewardPoints> rewards = rewardService.getRewardsByCustomerId(customer.getId());

        assertThat(rewards).hasSize(1);
        assertThat(rewards.get(0)).isEqualTo(existingRewardPoints);
    }

    @Test
    void testGetRewardsByCustomerId_NonExistingCustomer() {
        when(rewardPointsRepository.findAll()).thenReturn(List.of());

        assertThrows(CustomerNotFoundException.class, () -> rewardService.getRewardsByCustomerId(customer.getId()));
    }

    @Test
    void testFetchAllRewards() {
        when(rewardPointsRepository.findAll()).thenReturn(List.of(existingRewardPoints));

        List<RewardPoints> rewards = rewardService.fetchAllRewards();

        assertThat(rewards).hasSize(1);
        assertThat(rewards.get(0)).isEqualTo(existingRewardPoints);
    }
}
