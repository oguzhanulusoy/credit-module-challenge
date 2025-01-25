package com.ing.cmc.service.loan.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanInstallmentListRequestDTO {
    private Long loanId;
}
