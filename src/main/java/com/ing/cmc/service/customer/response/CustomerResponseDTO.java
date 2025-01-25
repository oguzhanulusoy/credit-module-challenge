package com.ing.cmc.service.customer.response;

import com.ing.cmc.service.user.response.UserResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponseDTO {
    private String name;
    private String surname;
    private UserResponseDTO userResponseDTO;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit;
}
