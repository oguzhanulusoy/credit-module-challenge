package com.ing.cmc.common.mapper.customer;

import com.ing.cmc.controller.customer.request.CustomerCreateRequest;
import com.ing.cmc.controller.customer.request.CustomerUpdateRequest;
import com.ing.cmc.service.customer.request.CustomerCreateRequestDTO;
import com.ing.cmc.service.customer.request.CustomerUpdateRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerObjectToDTOMapper {
    CustomerCreateRequestDTO toDTO(CustomerCreateRequest customerCreateRequest);

    CustomerUpdateRequestDTO toDTO(CustomerUpdateRequest customerUpdateRequest);


}
