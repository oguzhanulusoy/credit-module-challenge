package com.ing.cmc;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import com.ing.cmc.common.enums.RoleEnum;
import com.ing.cmc.entity.User;
import com.ing.cmc.repository.UserRepository;
import com.ing.cmc.service.loan.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.ing.cmc.entity.Customer;
import com.ing.cmc.entity.Loan;
import com.ing.cmc.repository.CustomerRepository;
import com.ing.cmc.repository.LoanInstallmentRepository;
import com.ing.cmc.repository.LoanRepository;
import com.ing.cmc.service.loan.request.LoanCreateRequestDTO;
import com.ing.cmc.service.loan.response.LoanResponseDTO;
import com.ing.cmc.service.message.MessageService;

@SpringBootTest
public class LoanServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLoan() {
        // Arrange
        Long customerId = 1L;
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal interestRate = BigDecimal.valueOf(0.2);
        int numberOfInstallments = 12;

        final String customerEmail = "unit_test_customer@email.com";
        final String customerUsername = "unit_test_tcn";
        final String customerPassword = "password";
        final String customerName = "unit_test_name";
        final String customerSurname = "unit_test_surname";
        User customerUser = User.builder()
                .email(customerEmail)
                .username(customerUsername)
                .password(customerPassword)
                .roles(new ArrayList<>(Arrays.asList(RoleEnum.STANDARD.toString())))
                .build();
        userRepository.save(customerUser);

        Customer customer = new Customer();
        customer.setUser(customerUser);
        customer.setName(customerName);
        customer.setSurname(customerSurname);
        customer.setCreditLimit(BigDecimal.valueOf(5000));
        customer.setUsedCreditLimit(BigDecimal.valueOf(0));
        customerRepository.save(customer);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArguments()[0]);

        LoanCreateRequestDTO loanCreateRequestDTO = new LoanCreateRequestDTO();
        loanCreateRequestDTO.setCustomerId(customerId);
        loanCreateRequestDTO.setAmount(amount);
        loanCreateRequestDTO.setInterestRate(interestRate);
        loanCreateRequestDTO.setNumberOfInstallment(numberOfInstallments);

        // Act
        LoanResponseDTO loanResponseDTO = loanService.createLoan(loanCreateRequestDTO);

        // Assert
        assertNotNull(loanResponseDTO);
        assertEquals(customerId, loanResponseDTO.getCustomerId());
        assertEquals(amount.multiply(BigDecimal.ONE.add(interestRate)), loanResponseDTO.getLoanAmount());
    }
}
