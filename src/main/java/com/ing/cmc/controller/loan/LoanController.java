package com.ing.cmc.controller.loan;

import com.ing.cmc.common.exception.InvalidRequestException;
import com.ing.cmc.common.mapper.loan.LoanDTOtoObjectMapper;
import com.ing.cmc.common.mapper.loan.LoanObjectToDTOMapper;
import com.ing.cmc.common.response.CommonResponses;
import com.ing.cmc.common.response.GenericResponse;
import com.ing.cmc.common.response.doc.CmcSuccessApiResponses_200;
import com.ing.cmc.common.response.doc.CmcSuccessApiResponses_200_204;
import com.ing.cmc.controller.loan.request.LoanCreateRequest;
import com.ing.cmc.controller.loan.request.LoanInstallmentListRequest;
import com.ing.cmc.controller.loan.request.LoanListRequest;
import com.ing.cmc.controller.loan.response.LoanInstallmentResponse;
import com.ing.cmc.controller.loan.response.LoanResponse;
import com.ing.cmc.service.authorization.AuthorizationService;
import com.ing.cmc.service.loan.LoanService;
import com.ing.cmc.service.loan.request.LoanCreateRequestDTO;
import com.ing.cmc.service.loan.request.LoanInstallmentListRequestDTO;
import com.ing.cmc.service.loan.request.LoanListRequestDTO;
import com.ing.cmc.service.loan.response.LoanInstallmentResponseDTO;
import com.ing.cmc.service.loan.response.LoanResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan")
@AllArgsConstructor
@Tag(name = "Loan", description = "Loan API documentation")
public class LoanController {

    private final AuthorizationService authorizationService;
    private final LoanService loanService;
    private final LoanDTOtoObjectMapper loanDTOtoObjectMapper;
    private final LoanObjectToDTOMapper loanObjectToDTOMapper;

    @PostMapping("/create")
    @Operation(summary = "Create loan", description = "Create a new loan for a given customer, amount, interest rate and number of installments")
    @CmcSuccessApiResponses_200
    @PreAuthorize("isAuthenticated() and @authorizationService.isAdmin()")
    public ResponseEntity<GenericResponse<Void>> createLoan(
            @RequestBody LoanCreateRequest loanCreateRequest) {
        LoanCreateRequestDTO loanCreateRequestDTO = loanObjectToDTOMapper.toDTO(loanCreateRequest);
        loanService.createLoan(loanCreateRequestDTO);
        //CuratorAddResponseDTO curatorAddResponseDTO = curatorService.addCurator(curatorAddRequestDTO, authentication.getName());
        //CuratorAddResponse result = curatorDTOtoResponseMapper.fromDTO(curatorAddResponseDTO);
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success());
    }

    @GetMapping("/list")
    @Operation(summary = "List loans", description = "List loans for a given customer")
    @CmcSuccessApiResponses_200_204
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericResponse<List<LoanResponse>>> listLoans(@RequestBody LoanListRequest loanListRequest) throws InvalidRequestException {
        LoanListRequestDTO loanListRequestDTO = loanObjectToDTOMapper.toDTO(loanListRequest);
        List<LoanResponseDTO> loanResponseDTOList = loanService.listLoans(loanListRequestDTO);
        List<LoanResponse> result = loanDTOtoObjectMapper.fromDTO(loanResponseDTOList);
        if (result.isEmpty()) {
            return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_204)).body(GenericResponse.success());
        }
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success(result));
    }

    @GetMapping("/list-installments")
    @Operation(summary = "List installments", description = "List installments for a given loan")
    @CmcSuccessApiResponses_200_204
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericResponse<List<LoanInstallmentResponse>>> listLoanInstallments(@RequestBody LoanInstallmentListRequest loanInstallmentListRequest) throws InvalidRequestException {
        LoanInstallmentListRequestDTO loanInstallmentListRequestDTO = loanObjectToDTOMapper.toDTO(loanInstallmentListRequest);
        List<LoanInstallmentResponseDTO> loanInstallmentResponseDTOList = loanService.listInstallments(loanInstallmentListRequestDTO);
        List<LoanInstallmentResponse> result = loanDTOtoObjectMapper.fromDTOForInstallment(loanInstallmentResponseDTOList);
        if (result.isEmpty()) {
            return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_204)).body(GenericResponse.success());
        }
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success(result));
    }

}
