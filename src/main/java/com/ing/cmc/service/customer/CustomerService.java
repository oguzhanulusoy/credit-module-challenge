package com.ing.cmc.service.customer;

import com.ing.cmc.common.exception.ExceptionLogger;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    // Message codes
    private static final String MSG_CUSTOMER_ALREADY_EXIST = "customer.already.exist";
    private static final String MSG_CUSTOMER_NOT_FOUND = "customer.not.found"; // todo msg

    // Services
    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;
    private final MessageService messageService;
    private final UserService userService;

    // Repositories
    private final CustomerRepository customerRepository;

    @Transactional(rollbackOn = Exception.class)
    public CustomerResponseDTO createCustomer(CustomerCreateRequestDTO customerCreateRequestDTO) throws InvalidRequestException {
        if (customerRepository.existsByUserId(customerCreateRequestDTO.getUserId())) {
            throw new InvalidRequestException(messageService.getMessage(MSG_CUSTOMER_ALREADY_EXIST), null);
        }
        try {
            Customer customer = Customer.builder()
                    .name(customerCreateRequestDTO.getName())
                    .surname(customerCreateRequestDTO.getSurname())
                    .user(userService.getUser(customerCreateRequestDTO.getUserId()))
                    .creditLimit(customerCreateRequestDTO.getCreditLimit())
                    .usedCreditLimit(customerCreateRequestDTO.getUsedCreditLimit())
                    .build();
            customer = customerRepository.save(customer);
            return convertCustomerToCustomerResponseDTO(customer);
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    public CustomerResponseDTO updateCustomer(CustomerUpdateRequestDTO customerUpdateRequestDTO) throws InvalidRequestException {
        if (!customerRepository.existsById(customerUpdateRequestDTO.getId())) {
            throw new InvalidRequestException(messageService.getMessage(MSG_CUSTOMER_NOT_FOUND), null);
        }
        try {
            Customer customer = customerRepository.findById(customerUpdateRequestDTO.getId()).orElseThrow(() -> new EntityNotFoundException(messageService.getMessage(MSG_CUSTOMER_NOT_FOUND)));
            customer.setName(customerUpdateRequestDTO.getName());
            customer.setSurname(customerUpdateRequestDTO.getSurname());
            customer.setCreditLimit(customerUpdateRequestDTO.getCreditLimit());
            customer.setUsedCreditLimit(customerUpdateRequestDTO.getUsedCreditLimit());
            customer = customerRepository.save(customer);
            return convertCustomerToCustomerResponseDTO(customer);
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return null;
    }

    public List<CustomerResponseDTO> listCustomers() {
        List<CustomerResponseDTO> customerResponseDTOList = new ArrayList<>();
        try {
            List<Customer> customers = customerRepository.findAll();
            for (Customer customer : customers) {
                customerResponseDTOList.add(convertCustomerToCustomerResponseDTO(customer));
            }
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return customerResponseDTOList;
    }

    public CustomerResponseDTO convertCustomerToCustomerResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
                .name(customer.getName())
                .surname(customer.getSurname())
                .userResponseDTO(UserResponseDTO.builder()
                        .id(customer.getUser().getId())
                        .username(customer.getUser().getUsername())
                        .email(customer.getUser().getEmail())
                        .build())
                .creditLimit(customer.getCreditLimit())
                .usedCreditLimit(customer.getUsedCreditLimit())
                .build();
    }
}


