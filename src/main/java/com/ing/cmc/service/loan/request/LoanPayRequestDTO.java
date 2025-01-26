package com.ing.cmc.service.loan.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LoanPayRequestDTO {
    private Long loanId;
    private BigDecimal amount;
}
