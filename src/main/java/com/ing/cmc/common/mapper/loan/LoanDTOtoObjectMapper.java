package com.ing.cmc.common.mapper.loan;

import com.ing.cmc.controller.loan.response.LoanInstallmentResponse;
import com.ing.cmc.controller.loan.response.LoanResponse;
import com.ing.cmc.service.loan.response.LoanInstallmentResponseDTO;
import com.ing.cmc.service.loan.response.LoanResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanDTOtoObjectMapper {
    List<LoanResponse> fromDTO(List<LoanResponseDTO> loanResponseDTOList);

    // Naming issue :)
    List<LoanInstallmentResponse> fromDTOForInstallment(List<LoanInstallmentResponseDTO> loanInstallmentResponseDTOList);

}