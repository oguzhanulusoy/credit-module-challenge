package com.ing.cmc.common.mapper.loan;

import com.ing.cmc.controller.loan.request.LoanCreateRequest;
import com.ing.cmc.controller.loan.request.LoanInstallmentListRequest;
import com.ing.cmc.controller.loan.request.LoanListRequest;
import com.ing.cmc.controller.loan.request.LoanPayRequest;
import com.ing.cmc.service.loan.request.LoanCreateRequestDTO;
import com.ing.cmc.service.loan.request.LoanInstallmentListRequestDTO;
import com.ing.cmc.service.loan.request.LoanListRequestDTO;
import com.ing.cmc.service.loan.request.LoanPayRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanObjectToDTOMapper {
    LoanCreateRequestDTO toDTO(LoanCreateRequest loanCreateRequest);

    LoanListRequestDTO toDTO(LoanListRequest loanListRequest);

    LoanInstallmentListRequestDTO toDTO(LoanInstallmentListRequest loanInstallmentListRequest);

    LoanPayRequestDTO toDTO(LoanPayRequest loanPayRequest);
}
