package com.ayush.finwiseai.repository;

import com.ayush.finwiseai.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    Page<Expense> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId " +
            "AND e.expenseDate BETWEEN :startDate AND :endDate")
    List<Expense> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}