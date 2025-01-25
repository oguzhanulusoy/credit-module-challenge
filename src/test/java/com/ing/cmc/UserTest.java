package com.ing.cmc;

import com.ing.cmc.service.user.request.UserCreateRequestDTO;
import org.junit.jupiter.api.Test;

public class UserTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "user@example.com";

    @Test
    public void createUser() {
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO();
        userCreateRequestDTO.setUsername(USERNAME);
        userCreateRequestDTO.setPassword(PASSWORD);
        userCreateRequestDTO.setEmail(EMAIL);

        //UserResponseDTO userResponseDTO = new UserResponseDTO();

        //expect actu
        //Assertions.assertEquals();

    }
}
