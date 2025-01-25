package com.ing.cmc.controller.customer;

import com.ing.cmc.common.exception.InvalidRequestException;
import com.ing.cmc.common.mapper.customer.CustomerDTOtoObjectMapper;
import com.ing.cmc.common.mapper.customer.CustomerObjectToDTOMapper;
import com.ing.cmc.common.response.CommonResponses;
import com.ing.cmc.common.response.GenericResponse;
import com.ing.cmc.common.response.doc.CmcSuccessApiResponses_200;
import com.ing.cmc.common.response.doc.CmcSuccessApiResponses_200_204;
import com.ing.cmc.controller.customer.request.CustomerCreateRequest;
import com.ing.cmc.controller.customer.request.CustomerUpdateRequest;
import com.ing.cmc.controller.customer.response.CustomerResponse;
import com.ing.cmc.controller.user.response.UserResponse;
import com.ing.cmc.service.authorization.AuthorizationService;
import com.ing.cmc.service.customer.CustomerService;
import com.ing.cmc.service.customer.request.CustomerCreateRequestDTO;
import com.ing.cmc.service.customer.request.CustomerUpdateRequestDTO;
import com.ing.cmc.service.customer.response.CustomerResponseDTO;
import com.ing.cmc.service.user.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
@Tag(name = "Customer", description = "Customer API documentation")
public class CustomerController {

    private final AuthorizationService authorizationService;
    private final CustomerService customerService;
    private final CustomerDTOtoObjectMapper customerDTOtoObjectMapper;
    private final CustomerObjectToDTOMapper customerObjectToDTOMapper;

    @PostMapping("/create")
    @Operation(summary = "Create", description = "Create customer")
    @CmcSuccessApiResponses_200
    @PreAuthorize("isAuthenticated() and @authorizationService.isAdmin()")
    public ResponseEntity<GenericResponse<CustomerResponse>> createCustomer(
            @RequestBody CustomerCreateRequest customerCreateRequest) throws InvalidRequestException {
        CustomerCreateRequestDTO customerCreateRequestDTO = customerObjectToDTOMapper.toDTO(customerCreateRequest);
        CustomerResponseDTO customerResponseDTO = customerService.createCustomer(customerCreateRequestDTO);
        CustomerResponse result = customerDTOtoObjectMapper.fromDTO(customerResponseDTO);
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success(result));
    }

    @GetMapping("/list")
    @Operation(summary = "List", description = "Return all customers without any parameter")
    @CmcSuccessApiResponses_200_204
    @PreAuthorize("isAuthenticated() and @authorizationService.isAdmin()")
    public ResponseEntity<GenericResponse<List<CustomerResponse>>> listCustomers() throws EntityNotFoundException {
        List<CustomerResponseDTO> customerResponseDTOList = customerService.listCustomers();
        List<CustomerResponse> result = customerDTOtoObjectMapper.fromDTO(customerResponseDTOList);
        if (result.isEmpty()) {
            return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_204)).body(GenericResponse.success());
        }
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success(result));
    }

    @PutMapping("/update")
    @Operation(summary = "Update", description = "Update customer")
    @CmcSuccessApiResponses_200
    @PreAuthorize("isAuthenticated() and @authorizationService.isAdmin()")
    public ResponseEntity<GenericResponse<CustomerResponse>> updateCustomer(
            @RequestBody CustomerUpdateRequest customerUpdateRequest) throws InvalidRequestException {
        CustomerUpdateRequestDTO customerUpdateRequestDTO = customerObjectToDTOMapper.toDTO(customerUpdateRequest);
        CustomerResponseDTO customerResponseDTO = customerService.updateCustomer(customerUpdateRequestDTO);
        CustomerResponse result = customerDTOtoObjectMapper.fromDTO(customerResponseDTO);
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success(result));
    }
}
