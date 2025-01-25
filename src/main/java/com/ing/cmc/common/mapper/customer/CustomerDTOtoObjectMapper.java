package com.ing.cmc.common.mapper.customer;

import com.ing.cmc.controller.customer.response.CustomerResponse;
import com.ing.cmc.service.customer.response.CustomerResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerDTOtoObjectMapper {
    CustomerResponse fromDTO(CustomerResponseDTO customerResponseDTO);

    List<CustomerResponse> fromDTO(List<CustomerResponseDTO> customerResponseDTOList);
}
