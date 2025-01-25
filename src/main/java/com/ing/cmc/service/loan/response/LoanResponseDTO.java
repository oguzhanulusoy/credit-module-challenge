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
public class LoanResponseDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerSurname;
    private BigDecimal loanAmount;
    private int numberOfInstallments;
    private boolean isPaid;
    private LocalDate createdDate;
}