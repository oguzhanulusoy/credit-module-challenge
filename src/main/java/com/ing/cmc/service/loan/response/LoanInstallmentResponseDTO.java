package com.ing.cmc.service.loan.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanInstallmentResponseDTO {
    private Long id;
    private Long loanId;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private boolean isPaid;
}