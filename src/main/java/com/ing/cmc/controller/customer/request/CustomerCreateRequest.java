package com.ing.cmc.controller.customer.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreateRequest {
    private String name;
    private String surname;
    private Long userId;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit;
}
