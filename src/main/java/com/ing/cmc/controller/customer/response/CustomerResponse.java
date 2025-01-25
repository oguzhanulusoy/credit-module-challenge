package com.ing.cmc.controller.customer.response;

import com.ing.cmc.service.user.response.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private String name;
    private String surname;
    private UserResponseDTO userResponseDTO;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit;
}
