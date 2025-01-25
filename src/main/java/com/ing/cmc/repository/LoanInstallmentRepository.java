package com.ing.cmc.repository;

import com.ing.cmc.entity.Loan;
import com.ing.cmc.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {

    List<LoanInstallment> findByLoanAndDueDateLessThanEqualAndIsPaidFalseOrderByDueDate(Loan loan, LocalDate dueDate);

    List<LoanInstallment> findByLoanId(Long loanId);

    boolean existsByLoanId(Long loanId);
}
