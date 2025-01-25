package com.ing.cmc.controller.loan.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerSurname;
    private BigDecimal loanAmount;
    private int numberOfInstallments;
    private boolean isPaid;
    private LocalDate createdDate;
}
