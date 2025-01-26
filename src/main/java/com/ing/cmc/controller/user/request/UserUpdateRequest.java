package com.ing.cmc.controller.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotNull
    private Long id;
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email
    private String email;
}
