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
public class LoanInstallmentResponse {
    private Long id;
    private Long loanId;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private boolean isPaid;
}
