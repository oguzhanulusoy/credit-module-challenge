package com.ing.cmc.repository;

import com.ing.cmc.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    boolean existsByCustomerId(Long customerId);

    List<Loan> findByCustomerId(Long customerId);
}
