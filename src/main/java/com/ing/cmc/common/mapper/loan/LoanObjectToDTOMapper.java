package com.ing.cmc.common.mapper.loan;

import com.ing.cmc.controller.loan.request.LoanCreateRequest;
import com.ing.cmc.service.loan.request.LoanCreateRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanObjectToDTOMapper {
    LoanCreateRequestDTO toDTO(LoanCreateRequest loanCreateRequest);
}
