package com.ing.cmc.service.loan;

import com.ing.cmc.common.exception.ExceptionLogger;
import com.ing.cmc.common.exception.InvalidRequestException;
import com.ing.cmc.entity.Loan;
import com.ing.cmc.entity.LoanInstallment;
import com.ing.cmc.repository.LoanInstallmentRepository;
import com.ing.cmc.repository.LoanRepository;
import com.ing.cmc.service.authentication.AuthenticationService;
import com.ing.cmc.service.authorization.AuthorizationService;
import com.ing.cmc.service.loan.request.LoanCreateRequestDTO;
import com.ing.cmc.service.loan.request.LoanInstallmentListRequestDTO;
import com.ing.cmc.service.loan.request.LoanListRequestDTO;
import com.ing.cmc.service.loan.response.LoanInstallmentResponseDTO;
import com.ing.cmc.service.loan.response.LoanResponseDTO;
import com.ing.cmc.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {

    // Services
    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;
    private final MessageService messageService;

    // Repositories
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;

    // Message codes
    private static final String MSG_ID_CANNOT_BE_NULL = "id.cannot.be.null";
    private static final String MSG_NO_LOAN_FOR_CUSTOMER = "no.loan.for.customer";

    public void checkIfIdExists(Long id) throws InvalidRequestException {
        if (id == null || id == 0) {
            throw new InvalidRequestException(messageService.getMessage(MSG_ID_CANNOT_BE_NULL), null);
        }
    }

    public Loan createLoan(LoanCreateRequestDTO loanCreateRequestDTO) {
        return null;
    }

    public List<LoanResponseDTO> listLoans(LoanListRequestDTO loanListRequestDTO) throws InvalidRequestException {
        checkIfIdExists(loanListRequestDTO.getCustomerId());
        if (!loanRepository.existsByCustomerId(loanListRequestDTO.getCustomerId())) {
            throw new InvalidRequestException(messageService.getMessage(MSG_NO_LOAN_FOR_CUSTOMER), null);
        }
        try {
            List<Loan> loans = loanRepository.findByCustomerId(loanListRequestDTO.getCustomerId());
            List<LoanResponseDTO> loanResponseDTOList = new ArrayList<>();
            for (Loan loan : loans) {
                loanResponseDTOList.add(convertLoanToLoanResponseDTO(loan));
            }
            return loanResponseDTOList;
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return null;
    }

    public LoanResponseDTO convertLoanToLoanResponseDTO(Loan loan) {
        return LoanResponseDTO.builder()
                .id(loan.getId())
                .customerId(loan.getCustomer().getId())
                .customerName(loan.getCustomer().getName())
                .customerSurname(loan.getCustomer().getSurname())
                .loanAmount(loan.getLoanAmount())
                .numberOfInstallments(loan.getNumberOfInstallments())
                .isPaid(loan.isPaid())
                .createdDate(loan.getCreatedDate())
                .build();
    }

    public List<LoanInstallmentResponseDTO> listInstallments(LoanInstallmentListRequestDTO loanInstallmentListRequestDTO) throws InvalidRequestException {
        checkIfIdExists(loanInstallmentListRequestDTO.getLoanId());
        if (!loanInstallmentRepository.existsByLoanId(loanInstallmentListRequestDTO.getLoanId())) {
            throw new InvalidRequestException(messageService.getMessage(MSG_NO_LOAN_FOR_CUSTOMER), null);
        }
        try {
            List<LoanInstallment> loanInstallments = loanInstallmentRepository.findByLoanId(loanInstallmentListRequestDTO.getLoanId());
            List<LoanInstallmentResponseDTO> loanInstallmentResponseDTOList = new ArrayList<>();
            for (LoanInstallment loanInstallment : loanInstallments) {
                loanInstallmentResponseDTOList.add(convertLoanInstallmentToLoanInstallmentResponseDTO(loanInstallment));
            }
            return loanInstallmentResponseDTOList;
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return null;

    }

    public LoanInstallmentResponseDTO convertLoanInstallmentToLoanInstallmentResponseDTO(LoanInstallment loanInstallment) {
        return LoanInstallmentResponseDTO.builder()
                .id(loanInstallment.getId())
                .loanId(loanInstallment.getLoan().getId())
                .amount(loanInstallment.getAmount())
                .paidAmount(loanInstallment.getPaidAmount())
                .dueDate(loanInstallment.getDueDate())
                .paymentDate(loanInstallment.getPaymentDate())
                .isPaid(loanInstallment.isPaid())
                .build();
    }

    public void payLoan(Long loanId, BigDecimal amount) {
    }
}
