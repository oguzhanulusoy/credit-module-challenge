package com.ing.cmc;

import com.ing.cmc.common.exception.InvalidRequestException;
import com.ing.cmc.entity.Customer;
import com.ing.cmc.entity.Loan;
import com.ing.cmc.entity.LoanInstallment;
import com.ing.cmc.repository.CustomerRepository;
import com.ing.cmc.repository.LoanInstallmentRepository;
import com.ing.cmc.repository.LoanRepository;
import com.ing.cmc.service.loan.LoanService;
import com.ing.cmc.service.loan.request.LoanCreateRequestDTO;
import com.ing.cmc.service.loan.request.LoanInstallmentListRequestDTO;
import com.ing.cmc.service.loan.request.LoanListRequestDTO;
import com.ing.cmc.service.loan.request.LoanPayRequestDTO;
import com.ing.cmc.service.loan.response.LoanInstallmentResponseDTO;
import com.ing.cmc.service.loan.response.LoanResponseDTO;
import com.ing.cmc.service.message.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoanServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @InjectMocks
    private LoanService loanService;

    @Mock
    private MessageService messageService;

    private LoanCreateRequestDTO loanCreateRequestDTO;
    private Customer customer;

    @BeforeEach
    void setUp() {
        loanCreateRequestDTO = new LoanCreateRequestDTO();
        loanCreateRequestDTO.setCustomerId(1L);
        loanCreateRequestDTO.setAmount(new BigDecimal("1000"));
        loanCreateRequestDTO.setInterestRate(new BigDecimal("0.1"));
        loanCreateRequestDTO.setNumberOfInstallment(12);

        customer = new Customer();
        customer.setId(1L);
        customer.setCreditLimit(new BigDecimal("5000"));
        customer.setUsedCreditLimit(new BigDecimal("1000"));

        when(messageService.getMessage(anyString())).thenReturn("No loan for customer");
    }

    @Test
    void createLoan_success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArguments()[0]);
        when(loanInstallmentRepository.saveAll(anyList())).thenReturn(null);

        LoanResponseDTO response = loanService.createLoan(loanCreateRequestDTO);

        assertNotNull(response);
        assertEquals(new BigDecimal("1100.0"), response.getLoanAmount());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createLoan_customerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(loanCreateRequestDTO);
        });

        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void createLoan_invalidCustomerId() {
        loanCreateRequestDTO.setCustomerId(999L);

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(loanCreateRequestDTO);
        });

        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void createLoan_invalidNumberOfInstallments() {
        loanCreateRequestDTO.setNumberOfInstallment(8);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(loanCreateRequestDTO);
        });

        assertEquals("Number of installments can only be 6, 9, 12, or 24", exception.getMessage());
    }

    @Test
    void createLoan_invalidInterestRate() {
        loanCreateRequestDTO.setInterestRate(new BigDecimal("0.6"));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(loanCreateRequestDTO);
        });

        assertEquals("Interest rate must be between 0.1 and 0.5", exception.getMessage());
    }

    @Test
    void createLoan_insufficientCreditLimit() {
        customer.setCreditLimit(new BigDecimal("1000"));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(loanCreateRequestDTO);
        });

        assertEquals("Customer does not have enough credit limit", exception.getMessage());
    }

    @Test
    void createLoan_invalidLoanAmount() {
        loanCreateRequestDTO.setAmount(new BigDecimal("-1000"));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            loanService.createLoan(loanCreateRequestDTO);
        });

        assertEquals("Cannot invoke \"com.ing.cmc.entity.Loan.getId()\" because \"loan\" is null", exception.getMessage());
    }

    @Test
    void createLoan_exceedingCreditLimit() {
        loanCreateRequestDTO.setAmount(new BigDecimal("5000"));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(loanCreateRequestDTO);
        });

        assertEquals("Customer does not have enough credit limit", exception.getMessage());
    }


    @Test
    void createLoan_exceedingUsedCreditLimit() {
        customer.setUsedCreditLimit(new BigDecimal("4000"));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(loanCreateRequestDTO);
        });

        assertEquals("Customer does not have enough credit limit", exception.getMessage());
    }

    @Test
    void listLoans_success() throws InvalidRequestException {
        Loan loan = Loan.builder()
                .customer(customer)
                .loanAmount(new BigDecimal("1000"))
                .numberOfInstallments(12)
                .isPaid(false)
                .build();

        List<Loan> loanList = new ArrayList<>();
        loanList.add(loan);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(loanRepository.existsByCustomerId(1L)).thenReturn(true);
        when(loanRepository.findByCustomerId(1L)).thenReturn(loanList);

        List<LoanResponseDTO> responseList = loanService.listLoans(new LoanListRequestDTO(1L));

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        assertEquals(new BigDecimal("1000"), responseList.get(0).getLoanAmount());
        verify(loanRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void listLoans_customerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            loanService.listLoans(new LoanListRequestDTO(1L));
        });

        assertEquals("No loan for customer", exception.getMessage());
    }

    @Test
    void listLoans_noLoansForCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(loanRepository.existsByCustomerId(1L)).thenReturn(false);
        when(messageService.getMessage(anyString())).thenReturn("No loan for customer");

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            loanService.listLoans(new LoanListRequestDTO(1L));
        });

        assertEquals("No loan for customer", exception.getMessage());
    }

    @SneakyThrows
    @Test
    void listLoans_exceptionHandling() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(loanRepository.existsByCustomerId(1L)).thenReturn(true);
        when(loanRepository.findByCustomerId(1L)).thenThrow(new RuntimeException("Database error"));

        List<LoanResponseDTO> responseList = loanService.listLoans(new LoanListRequestDTO(1L));

        assertNull(responseList);
        verify(loanRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void listInstallments_success() throws InvalidRequestException {
        Loan loan = Loan.builder()
                .id(1L)
                .customer(customer)
                .loanAmount(new BigDecimal("1000"))
                .numberOfInstallments(12)
                .isPaid(false)
                .build();

        LoanInstallment installment = LoanInstallment.builder()
                .loan(loan)
                .amount(new BigDecimal("100"))
                .paidAmount(BigDecimal.ZERO)
                .dueDate(LocalDate.now().plusMonths(1))
                .paymentDate(null)
                .isPaid(false)
                .build();

        List<LoanInstallment> installmentList = new ArrayList<>();
        installmentList.add(installment);

        when(loanInstallmentRepository.existsByLoanId(1L)).thenReturn(true);
        when(loanInstallmentRepository.findByLoanId(1L)).thenReturn(installmentList);

        List<LoanInstallmentResponseDTO> responseList = loanService.listInstallments(new LoanInstallmentListRequestDTO(1L));

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        assertEquals(new BigDecimal("100"), responseList.get(0).getAmount());
        verify(loanInstallmentRepository, times(1)).findByLoanId(1L);
    }

    @Test
    void listInstallments_loanNotFound() {
        when(loanInstallmentRepository.existsByLoanId(1L)).thenReturn(false);
        when(messageService.getMessage(anyString())).thenReturn("No loan for customer");

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            loanService.listInstallments(new LoanInstallmentListRequestDTO(1L));
        });

        assertEquals("No loan for customer", exception.getMessage());
    }

    @SneakyThrows
    @Test
    void listInstallments_exceptionHandling() {
        when(loanInstallmentRepository.existsByLoanId(1L)).thenReturn(true);
        when(loanInstallmentRepository.findByLoanId(1L)).thenThrow(new RuntimeException("Database error"));

        List<LoanInstallmentResponseDTO> responseList = loanService.listInstallments(new LoanInstallmentListRequestDTO(1L));

        assertNull(responseList);
        verify(loanInstallmentRepository, times(1)).findByLoanId(1L);
    }

    @Test
    void payLoan_success() {
        Loan loan = Loan.builder()
                .id(1L)
                .customer(customer)
                .loanAmount(new BigDecimal("1000"))
                .numberOfInstallments(12)
                .isPaid(false)
                .build();

        LoanInstallment installment1 = LoanInstallment.builder()
                .loan(loan)
                .amount(new BigDecimal("100"))
                .paidAmount(BigDecimal.ZERO)
                .dueDate(LocalDate.now().plusMonths(1))
                .paymentDate(null)
                .isPaid(false)
                .build();

        LoanInstallment installment2 = LoanInstallment.builder()
                .loan(loan)
                .amount(new BigDecimal("100"))
                .paidAmount(BigDecimal.ZERO)
                .dueDate(LocalDate.now().plusMonths(2))
                .paymentDate(null)
                .isPaid(false)
                .build();

        List<LoanInstallment> installmentList = new ArrayList<>();
        installmentList.add(installment1);
        installmentList.add(installment2);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanInstallmentRepository.findByLoanAndDueDateLessThanEqualAndIsPaidFalseOrderByDueDate(any(Loan.class), any(LocalDate.class)))
                .thenReturn(installmentList);

        LoanPayRequestDTO loanPayRequestDTO = new LoanPayRequestDTO(1L, new BigDecimal("200"));
        List<LoanInstallmentResponseDTO> responseList = loanService.payLoan(loanPayRequestDTO);

        assertNotNull(responseList);
        assertEquals(0, responseList.size());
        assertTrue(responseList.stream().allMatch(LoanInstallmentResponseDTO::isPaid));
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(loanInstallmentRepository, times(2)).save(any(LoanInstallment.class));
    }


    @Test
    void payLoan_loanNotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        LoanPayRequestDTO loanPayRequestDTO = new LoanPayRequestDTO(1L, new BigDecimal("200"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            loanService.payLoan(loanPayRequestDTO);
        });

        assertEquals("Loan not found", exception.getMessage());
    }
}
