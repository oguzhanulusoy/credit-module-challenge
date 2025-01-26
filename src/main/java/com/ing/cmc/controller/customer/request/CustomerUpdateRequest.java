package com.ing.cmc.controller.customer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateRequest {
    private Long id;
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Surname cannot be null")
    @NotBlank(message = "Surname cannot be blank")
    private String surname;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit;
}
