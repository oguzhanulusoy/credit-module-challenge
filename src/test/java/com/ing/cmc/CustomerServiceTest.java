package com.ing.cmc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ing.cmc.service.customer.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.ing.cmc.common.exception.InvalidRequestException;
import com.ing.cmc.entity.Customer;
import com.ing.cmc.repository.CustomerRepository;
import com.ing.cmc.service.authentication.AuthenticationService;
import com.ing.cmc.service.authorization.AuthorizationService;
import com.ing.cmc.service.customer.request.CustomerCreateRequestDTO;
import com.ing.cmc.service.customer.request.CustomerUpdateRequestDTO;
import com.ing.cmc.service.customer.response.CustomerResponseDTO;
import com.ing.cmc.service.message.MessageService;
import com.ing.cmc.service.user.UserService;
import com.ing.cmc.service.user.response.UserResponseDTO;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MessageService messageService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerCreateRequestDTO customerCreateRequestDTO;
    private CustomerUpdateRequestDTO customerUpdateRequestDTO;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .creditLimit(BigDecimal.valueOf(10000))
                .usedCreditLimit(BigDecimal.ZERO)
                .build();

        customerCreateRequestDTO = new CustomerCreateRequestDTO();
        customerCreateRequestDTO.setName("John");
        customerCreateRequestDTO.setSurname("Doe");
        customerCreateRequestDTO.setUserId(1L);
        customerCreateRequestDTO.setCreditLimit(BigDecimal.valueOf(10000));
        customerCreateRequestDTO.setUsedCreditLimit(BigDecimal.ZERO);

        customerUpdateRequestDTO = new CustomerUpdateRequestDTO();
        customerUpdateRequestDTO.setId(1L);
        customerUpdateRequestDTO.setName("John Updated");
        customerUpdateRequestDTO.setSurname("Doe Updated");
        customerUpdateRequestDTO.setCreditLimit(BigDecimal.valueOf(10000));
        customerUpdateRequestDTO.setUsedCreditLimit(BigDecimal.valueOf(1000));
    }
/*
    @Test
    void testCreateCustomer_Success() throws InvalidRequestException {
        when(customerRepository.existsByUserId(customerCreateRequestDTO.getUserId())).thenReturn(false);
        when(userService.getUser(customerCreateRequestDTO.getUserId())).thenReturn(new UserResponseDTO());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDTO response = customerService.createCustomer(customerCreateRequestDTO);

        assertNotNull(response);
        assertEquals(customer.getName(), response.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
*/
    @Test
    void testCreateCustomer_AlreadyExists() {
        when(customerRepository.existsByUserId(customerCreateRequestDTO.getUserId())).thenReturn(true);
        when(messageService.getMessage(anyString())).thenReturn("Customer already exists");

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            customerService.createCustomer(customerCreateRequestDTO);
        });

        assertEquals("Customer already exists", exception.getMessage());
    }

    @Test
    void testUpdateCustomer_Success() throws InvalidRequestException {
        when(customerRepository.existsById(customerUpdateRequestDTO.getId())).thenReturn(true);
        when(customerRepository.findById(customerUpdateRequestDTO.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDTO response = customerService.updateCustomer(customerUpdateRequestDTO);

        assertNotNull(response);
        assertEquals(customerUpdateRequestDTO.getName(), response.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(customerRepository.existsById(customerUpdateRequestDTO.getId())).thenReturn(false);
        when(messageService.getMessage(anyString())).thenReturn("Customer not found");

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            customerService.updateCustomer(customerUpdateRequestDTO);
        });

        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void testListCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerResponseDTO> response = customerService.listCustomers();

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(customerRepository, times(1)).findAll();
    }
}
