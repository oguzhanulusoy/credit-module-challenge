package com.ing.cmc.service.loan;

import com.ing.cmc.common.exception.ExceptionLogger;
import com.ing.cmc.common.exception.InvalidRequestException;
import com.ing.cmc.entity.Customer;
import com.ing.cmc.entity.Loan;
import com.ing.cmc.entity.LoanInstallment;
import com.ing.cmc.repository.CustomerRepository;
import com.ing.cmc.repository.LoanInstallmentRepository;
import com.ing.cmc.repository.LoanRepository;
import com.ing.cmc.service.authentication.AuthenticationService;
import com.ing.cmc.service.authorization.AuthorizationService;
import com.ing.cmc.service.loan.request.LoanCreateRequestDTO;
import com.ing.cmc.service.loan.request.LoanInstallmentListRequestDTO;
import com.ing.cmc.service.loan.request.LoanListRequestDTO;
import com.ing.cmc.service.loan.request.LoanPayRequestDTO;
import com.ing.cmc.service.loan.response.LoanInstallmentResponseDTO;
import com.ing.cmc.service.loan.response.LoanResponseDTO;
import com.ing.cmc.service.message.MessageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {

    // Message codes
    private static final String MSG_ID_CANNOT_BE_NULL = "id.cannot.be.null";
    private static final String MSG_NO_LOAN_FOR_CUSTOMER = "no.loan.for.customer";
    private static final String MSG_LOAN_NOT_FOUND = "loan.not.found"; // todo
    // Services
    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;
    private final MessageService messageService;
    // Repositories
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final CustomerRepository customerRepository;

    public void checkIfIdExists(Long id) throws InvalidRequestException {
        if (id == null || id == 0) {
            throw new InvalidRequestException(messageService.getMessage(MSG_ID_CANNOT_BE_NULL), null);
        }
    }

    /**
     * Create a new loan for a given customer, amount, interest rate and number of installments.
     * <p>
     * This method checks if the customer has enough limit to get this new loan.
     * The number of installments can only be 6, 9, 12, or 24.
     * The interest rate can be between 0.1 and 0.5.
     * All installments will have the same amount. The total amount for the loan will be amount * (1 + interest rate).
     * The due date of installments will be the first day of the months. The first installment's due date will be the first day of the next month.
     *
     * @param LoanCreateRequestDTO.customerId           The ID of the customer who is taking the loan.
     * @param LoanCreateRequestDTO.amount               The principal amount of the loan.
     * @param LoanCreateRequestDTO.interestRate         The interest rate for the loan.
     * @param LoanCreateRequestDTO.numberOfInstallments The number of installments for repayment.
     * @return LoanResponseDTO after the created Loan object has been converted to.
     */
    @Transactional
    public LoanResponseDTO createLoan(LoanCreateRequestDTO loanCreateRequestDTO) {
        Customer customer = customerRepository.findById(loanCreateRequestDTO.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));

        // Check if the number of installments is valid
        if (loanCreateRequestDTO.getNumberOfInstallment() != 6 && loanCreateRequestDTO.getNumberOfInstallment() != 9 && loanCreateRequestDTO.getNumberOfInstallment() != 12 && loanCreateRequestDTO.getNumberOfInstallment() != 24) {
            throw new IllegalArgumentException("Number of installments can only be 6, 9, 12, or 24");
        }

        // Check if the interest rate is valid
        if (loanCreateRequestDTO.getInterestRate().compareTo(BigDecimal.valueOf(0.1)) < 0 || loanCreateRequestDTO.getInterestRate().compareTo(BigDecimal.valueOf(0.5)) > 0) {
            throw new IllegalArgumentException("Interest rate must be between 0.1 and 0.5");
        }

        // Calculate the total loan amount
        BigDecimal totalAmount = loanCreateRequestDTO.getAmount().multiply(BigDecimal.ONE.add(loanCreateRequestDTO.getInterestRate()));

        // Check if the customer has enough credit limit
        if (customer.getCreditLimit().subtract(customer.getUsedCreditLimit()).compareTo(totalAmount) < 0) {
            throw new RuntimeException("Customer does not have enough credit limit");
        }

        // Create the loan
        Loan loan = Loan.builder()
                .customer(customer)
                .loanAmount(totalAmount)
                .numberOfInstallments(loanCreateRequestDTO.getNumberOfInstallment())
                .isPaid(false)
                .build();
        loan = loanRepository.save(loan);

        // Update the customer's used credit limit
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(totalAmount));
        customerRepository.save(customer);

        // Calculate the amount for each installment
        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(loanCreateRequestDTO.getNumberOfInstallment()), BigDecimal.ROUND_HALF_UP);

        // Create installments
        List<LoanInstallment> installments = new ArrayList<>();
        LocalDate dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        for (int i = 0; i < loanCreateRequestDTO.getNumberOfInstallment(); i++) {
            LoanInstallment installment = LoanInstallment.builder()
                    .loan(loan)
                    .amount(installmentAmount)
                    .paidAmount(BigDecimal.ZERO)
                    .dueDate(dueDate)
                    .paymentDate(null)
                    .isPaid(false)
                    .build();
            installments.add(installment);
            dueDate = dueDate.plusMonths(1);
        }
        loanInstallmentRepository.saveAll(installments);
        return convertLoanToLoanResponseDTO(loan);
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

    /**
     * Pay installments for a given loan and amount.
     * <p>
     * This method allows paying multiple installments with respect to the amount sent.
     * Installments should be paid wholly or not at all. If the installment amount is 10 and you send 20,
     * 2 installments can be paid. If you send 15, only 1 can be paid. If you send 5, no installments can be paid.
     * <p>
     * The earliest installment should be paid first, and if there is more money, continue to the next installment.
     * Installments that are due in more than 3 calendar months cannot be paid.
     * For example, if the current month is January, you can only pay for January, February, and March installments.
     * <p>
     * A result is returned to inform how many installments were paid, the total amount spent, and if the loan is paid completely.
     * Necessary updates are done in customer, loan, and installment tables.
     *
     * @param LoanPayRequestDTO.loanId The ID of the loan to be paid.
     * @param LoanPayRequestDTO.       amount The amount of money sent to pay the installments.
     * @return A PaymentResult containing the details of the payment process.
     */
    @Transactional
    public List<LoanInstallmentResponseDTO> payLoan(LoanPayRequestDTO loanPayRequestDTO) {
        Loan loan = loanRepository.findById(loanPayRequestDTO.getLoanId()).orElseThrow(() -> new EntityNotFoundException("Loan not found"));
        LocalDate currentDate = LocalDate.now();
        LocalDate maxDate = currentDate.plusMonths(3);
        List<LoanInstallment> installments = loanInstallmentRepository.findByLoanAndDueDateLessThanEqualAndIsPaidFalseOrderByDueDate(loan, maxDate);
        int paidInstallments = 0;
        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal amount = loanPayRequestDTO.getAmount();

        for (LoanInstallment installment : installments) {
            if (amount.compareTo(installment.getAmount()) >= 0) {
                installment.setPaidAmount(installment.getAmount());
                installment.setPaymentDate(LocalDate.now());
                installment.setPaid(true);
                loanInstallmentRepository.save(installment);

                amount = amount.subtract(installment.getAmount());
                totalPaid = totalPaid.add(installment.getAmount());
                paidInstallments++;

                if (loan.getLoanAmount().compareTo(totalPaid) == 0) {
                    loan.setPaid(true);
                }
            } else {
                break;
            }
        }
        loanRepository.save(loan);

        List<LoanInstallment> loanInstallmentList = loanInstallmentRepository.findByLoanId(loan.getId());
        List<LoanInstallmentResponseDTO> loanInstallmentResponseDTOList = new ArrayList<>();
        for (LoanInstallment loanInstallment : loanInstallmentList) {
            loanInstallmentResponseDTOList.add(convertLoanInstallmentToLoanInstallmentResponseDTO(loanInstallment));
        }
        return loanInstallmentResponseDTOList;
    }
}
