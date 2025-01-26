package com.ing.cmc.entity;

import com.ing.cmc.common.enums.RoleEnum;
import com.ing.cmc.repository.CustomerRepository;
import com.ing.cmc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExampleDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        final String adminEmail = "admin@email.com";
        final String adminUsername = "admin";
        final String adminPassword = "password";
        User adminUser = User.builder()
                .email(adminEmail)
                .username(adminUsername)
                .password(this.passwordEncoder.encode(adminPassword))
                .roles(new ArrayList<>(Arrays.asList(RoleEnum.ADMIN.toString(), RoleEnum.STANDARD.toString())))
                .build();
        userRepository.save(adminUser);
        log.info("Admin user has been created");

        final String customerEmail = "customer@email.com";
        final String customerUsername = "tckn";
        final String customerPassword = "password";
        User customerUser = User.builder()
                .email(customerEmail)
                .username(customerUsername)
                .password(customerPassword)
                .roles(new ArrayList<>(Arrays.asList(RoleEnum.STANDARD.toString())))
                .build();
        userRepository.save(customerUser);
        log.info("Customer user has been created and customer user id= " + customerUser.getId());

        final String customerName = "customer_name";
        final String customerSurname = "customer_surname";
        final BigDecimal customerCreditLimit = BigDecimal.valueOf(10000);
        final BigDecimal customerUsedCreditLimit = BigDecimal.valueOf(100);
        Customer customer = Customer.builder()
                .name(customerName)
                .surname(customerSurname)
                .user(customerUser)
                .creditLimit(customerCreditLimit)
                .usedCreditLimit(customerUsedCreditLimit)
                .build();
        customerRepository.save(customer);
        log.info("Customer details have been created and customer id= " + customer.getId());
    }
}
