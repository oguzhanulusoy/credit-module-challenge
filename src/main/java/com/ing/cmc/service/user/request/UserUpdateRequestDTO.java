package com.ing.cmc.service.user.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequestDTO {
    private Long id;
    private String username;
    private String email;
}
