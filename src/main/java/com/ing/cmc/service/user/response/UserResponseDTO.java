package com.ing.cmc.service.user.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PUBLIC)
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
}
