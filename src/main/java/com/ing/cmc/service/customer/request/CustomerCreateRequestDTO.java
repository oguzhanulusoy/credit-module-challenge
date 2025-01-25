package com.ing.cmc.service.customer.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerCreateRequestDTO {
    private String name;
    private String surname;
    private Long userId;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit;
}
