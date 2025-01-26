package com.ing.cmc.controller.loan.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanPayRequest {
    private Long loanId;
    private BigDecimal amount;
}
