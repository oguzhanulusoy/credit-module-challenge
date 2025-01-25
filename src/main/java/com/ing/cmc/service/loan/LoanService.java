package com.ing.cmc.service.loan;

import com.ing.cmc.entity.Loan;
import com.ing.cmc.entity.LoanInstallment;
import com.ing.cmc.service.loan.request.LoanCreateRequestDTO;
import com.ing.cmc.service.loan.request.LoanInstallmentListRequestDTO;
import com.ing.cmc.service.loan.request.LoanListRequestDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LoanService {

    public Loan createLoan(LoanCreateRequestDTO loanCreateRequestDTO) {
        return null;
    }

    public List<Loan> listLoans(LoanListRequestDTO loanListRequestDTO) {
        return null;
    }

    public List<LoanInstallment> listInstallments(LoanInstallmentListRequestDTO loanInstallmentListRequestDTO) {
        return null;
    }

    public void payLoan(Long loanId, BigDecimal amount) {
        // İş mantığını buraya ekle
    }
}
