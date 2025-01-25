package com.ing.cmc.common.mapper.user;

import com.ing.cmc.controller.user.request.UserCreateRequest;
import com.ing.cmc.controller.user.request.UserRequest;
import com.ing.cmc.controller.user.request.UserUpdateRequest;
import com.ing.cmc.service.user.request.UserCreateRequestDTO;
import com.ing.cmc.service.user.request.UserRequestDTO;
import com.ing.cmc.service.user.request.UserUpdateRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserObjectToDTOMapper {
    UserCreateRequestDTO toDTO(UserCreateRequest userCreateRequest);

    UserRequestDTO toDTO(UserRequest userRequest);

    UserUpdateRequestDTO toDTO(UserUpdateRequest userUpdateRequest);


}
