package com.ing.cmc.controller.loan.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanInstallmentListRequest {
    private Long loanId;
}
