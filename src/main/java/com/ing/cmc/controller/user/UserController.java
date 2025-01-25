package com.ing.cmc.controller.user;

import com.ing.cmc.common.exception.InvalidRequestException;
import com.ing.cmc.common.mapper.user.UserDTOtoObjectMapper;
import com.ing.cmc.common.mapper.user.UserObjectToDTOMapper;
import com.ing.cmc.common.response.CommonResponses;
import com.ing.cmc.common.response.GenericResponse;
import com.ing.cmc.common.response.doc.CmcSuccessApiResponses_200;
import com.ing.cmc.common.response.doc.CmcSuccessApiResponses_200_204;
import com.ing.cmc.common.response.doc.CmcSuccessApiResponses_204;
import com.ing.cmc.controller.user.request.UserCreateRequest;
import com.ing.cmc.controller.user.request.UserLoginRequest;
import com.ing.cmc.controller.user.request.UserRequest;
import com.ing.cmc.controller.user.request.UserUpdateRequest;
import com.ing.cmc.controller.user.response.UserLoginResponse;
import com.ing.cmc.controller.user.response.UserResponse;
import com.ing.cmc.service.authorization.AuthorizationService;
import com.ing.cmc.service.user.UserService;
import com.ing.cmc.service.user.request.UserCreateRequestDTO;
import com.ing.cmc.service.user.request.UserRequestDTO;
import com.ing.cmc.service.user.request.UserUpdateRequestDTO;
import com.ing.cmc.service.user.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "User", description = "User API documentation")
public class UserController {

    private final AuthorizationService authorizationService;
    private final UserService userService;
    private final UserDTOtoObjectMapper userDTOtoObjectMapper;
    private final UserObjectToDTOMapper userObjectToDTOMapper;

    @PostMapping("/create")
    @Operation(summary = "Registration", description = "Need user model")
    @CmcSuccessApiResponses_200
    public ResponseEntity<GenericResponse<Void>> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) throws InvalidRequestException {
        UserCreateRequestDTO userCreateRequestDTO = userObjectToDTOMapper.toDTO(userCreateRequest);
        userService.createUser(userCreateRequestDTO);
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success());
    }

    @PostMapping("/auth")
    @Operation(summary = "Authentication", description = "Authenticate user by taking username and password")
    @CmcSuccessApiResponses_200
    public ResponseEntity<GenericResponse<UserLoginResponse>> auth(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success(userService.auth(userLoginRequest)));
    }

    @GetMapping("/list")
    @Operation(summary = "List", description = "Return all users without any parameter")
    @CmcSuccessApiResponses_200_204
    @PreAuthorize("isAuthenticated() and @authorizationService.isAdmin()")
    public ResponseEntity<GenericResponse<List<UserResponse>>> listUsers() throws EntityNotFoundException {
        List<UserResponseDTO> userResponseDTOList = userService.listUsers();
        List<UserResponse> result = userDTOtoObjectMapper.fromDTO(userResponseDTOList);
        if (result.isEmpty()) {
            return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_204)).body(GenericResponse.success());
        }
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success(result));
    }

    @GetMapping("/get")
    @Operation(summary = "Get", description = "Get specific user per id")
    @CmcSuccessApiResponses_200_204
    @PreAuthorize("isAuthenticated() and @authorizationService.isAdmin()")
    public ResponseEntity<GenericResponse<UserResponse>> getUser(@RequestBody UserRequest userRequest) throws EntityNotFoundException, InvalidRequestException {
        UserRequestDTO userRequestDTO = userObjectToDTOMapper.toDTO(userRequest);
        UserResponseDTO userResponseDTO = userService.getUser(userRequestDTO);
        UserResponse result = userDTOtoObjectMapper.fromDTO(userResponseDTO);
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success(result));
    }

    @PutMapping("/set-role")
    @Operation(summary = "Set role", description = "Assign admin role to a user")
    @CmcSuccessApiResponses_200_204
    @PreAuthorize("isAuthenticated() and @authorizationService.isAdmin()")
    public ResponseEntity<GenericResponse<Void>> setRole(@Valid @RequestBody UserRequest userRequest) throws InvalidRequestException, EntityNotFoundException {
        UserRequestDTO userRequestDTO = userObjectToDTOMapper.toDTO(userRequest);
        if (userService.setRole(userRequestDTO)) {
            return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_204)).body(GenericResponse.success());
        }
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success()); // todo
    }

    @PutMapping("/update")
    @Operation(summary = "Update", description = "Update username and email")
    @CmcSuccessApiResponses_200
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericResponse<UserResponse>> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) throws EntityNotFoundException, InvalidRequestException {
        UserUpdateRequestDTO userUpdateRequestDTO = userObjectToDTOMapper.toDTO(userUpdateRequest);
        UserResponseDTO userResponseDTO = userService.updateUser(userUpdateRequestDTO);
        UserResponse result = userDTOtoObjectMapper.fromDTO(userResponseDTO);
        return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_200)).body(GenericResponse.success(result));
    }

    @DeleteMapping("/delete-logical")
    @Operation(summary = "Delete (logical)", description = "Set enabled parameter")
    @CmcSuccessApiResponses_204
    @PreAuthorize("isAuthenticated() and @authorizationService.isAdmin()")
    public ResponseEntity<GenericResponse<Void>> deleteUserLogical(@Valid @RequestBody UserRequest userRequest) throws InvalidRequestException, EntityNotFoundException {
        UserRequestDTO userRequestDTO = userObjectToDTOMapper.toDTO(userRequest);
        if (userService.deleteUserLogical(userRequestDTO)) {
            return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_204)).body(GenericResponse.success());
        }
        return null;
    }

    @DeleteMapping("/delete-hard")
    @Operation(summary = "Delete (hard)", description = "Not possible to revert")
    @CmcSuccessApiResponses_204
    @PreAuthorize("isAuthenticated() and @authorizationService.isAdmin()")
    public ResponseEntity<GenericResponse<Void>> deleteUserHard(@Valid @RequestBody UserRequest userRequest) throws InvalidRequestException, EntityNotFoundException {
        UserRequestDTO userRequestDTO = userObjectToDTOMapper.toDTO(userRequest);
        if (userService.deleteUserHard(userRequestDTO)) {
            return ResponseEntity.status(Integer.valueOf(CommonResponses.CODE_204)).body(GenericResponse.success());
        }
        return null;
    }

}
