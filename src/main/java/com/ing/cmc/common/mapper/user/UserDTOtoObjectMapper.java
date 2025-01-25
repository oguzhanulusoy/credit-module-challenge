package com.ing.cmc.common.mapper.user;

import com.ing.cmc.controller.user.response.UserResponse;
import com.ing.cmc.service.user.response.UserResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOtoObjectMapper {
    List<UserResponse> fromDTO(List<UserResponseDTO> userResponseDTOList);

    UserResponse fromDTO(UserResponseDTO userResponseDTO);


}
