package com.ing.cmc.service.user;

import com.ing.cmc.common.enums.RoleEnum;
import com.ing.cmc.common.exception.ExceptionLogger;
import com.ing.cmc.common.exception.InvalidRequestException;
import com.ing.cmc.controller.user.request.UserLoginRequest;
import com.ing.cmc.controller.user.response.UserLoginResponse;
import com.ing.cmc.entity.User;
import com.ing.cmc.repository.UserRepository;
import com.ing.cmc.service.authentication.AuthenticationService;
import com.ing.cmc.service.authorization.AuthorizationService;
import com.ing.cmc.service.message.MessageService;
import com.ing.cmc.service.user.request.UserCreateRequestDTO;
import com.ing.cmc.service.user.request.UserRequestDTO;
import com.ing.cmc.service.user.request.UserUpdateRequestDTO;
import com.ing.cmc.service.user.response.UserResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    // Message codes
    private static final String MSG_ENTITY_NOT_FOUND = "user.entity.not.found";
    private static final String MSG_USER_CANNOT_ASSIGN_ROLE = "user.cannot.assign.role";
    private static final String MSG_USER_CANNOT_DELETE = "user.cannot.delete";
    private static final String MSG_USER_ALREADY_EXIST = "user.already.exist";
    private static final String MSG_ID_CANNOT_BE_NULL = "id.cannot.be.null";
    // Services
    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;
    private final MessageService messageService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    // Repositories
    private final UserRepository userRepository;
    // Others
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(rollbackOn = Exception.class)
    public void createUser(UserCreateRequestDTO userCreateRequestDTO) throws InvalidRequestException {
        if (userRepository.existsByUsername(userCreateRequestDTO.getUsername()) || userRepository.existsByEmail(userCreateRequestDTO.getEmail())) {
            throw new InvalidRequestException(messageService.getMessage(MSG_USER_ALREADY_EXIST), null);
        }
        try {
            User user = User.builder()
                    .email(userCreateRequestDTO.getEmail())
                    .username(userCreateRequestDTO.getUsername())
                    .password(this.passwordEncoder.encode(userCreateRequestDTO.getPassword()))
                    .roles(new ArrayList<>(Collections.singletonList(RoleEnum.STANDARD.toString())))
                    .build();
            userRepository.save(user);
            String token = authenticationService.generateToken(user, RoleEnum.STANDARD.toString());
            log.info("token => " + token);
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
    }

    public UserLoginResponse auth(UserLoginRequest userLoginRequestDTO) {
        User user = getUser(userLoginRequestDTO.getUsername());
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestDTO.getUsername(), userLoginRequestDTO.getPassword()));
            String token = this.authenticationService.generateToken(user, user.getRoles());
            return UserLoginResponse.builder().token(token).build();
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return null;
    }

    public List<UserResponseDTO> listUsers() {
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        try {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                if (user.isEnabled()) {
                    userResponseDTOList.add(convertUserToUserResponseDTO(user));
                }
            }
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return userResponseDTOList;
    }

    public void checkIfIdExists(Long id) throws InvalidRequestException {
        if (id == null || id == 0) {
            throw new InvalidRequestException(messageService.getMessage(MSG_ID_CANNOT_BE_NULL), null);
        }
    }

    public UserResponseDTO getUser(UserRequestDTO userRequestDTO) throws InvalidRequestException {
        checkIfIdExists(userRequestDTO.getId());
        try {
            return convertUserToUserResponseDTO(getUser(userRequestDTO.getId()));
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return null;
    }

    public UserResponseDTO convertUserToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(messageService.getMessage(MSG_ENTITY_NOT_FOUND)));
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(messageService.getMessage(MSG_ENTITY_NOT_FOUND)));
    }

    @Transactional(rollbackOn = Exception.class)
    public boolean setRole(UserRequestDTO userRequestDTO) throws InvalidRequestException {
        checkIfIdExists(userRequestDTO.getId());
        if (getUser(authorizationService.getSessionUsername()).getId().equals(userRequestDTO.getId())) {
            throw new InvalidRequestException(messageService.getMessage(MSG_USER_CANNOT_ASSIGN_ROLE), null);
        }
        try {
            User user = getUser(userRequestDTO.getId());
            List<String> roles = user.getRoles();
            roles.add(RoleEnum.ADMIN.toString());
            user.setRoles(roles);
            userRepository.save(user);
            return true;
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return false;
    }

    @Transactional(rollbackOn = Exception.class)
    public UserResponseDTO updateUser(UserUpdateRequestDTO userUpdateRequestDTO) throws InvalidRequestException {
        checkIfIdExists(userUpdateRequestDTO.getId());
        try {
            User user = getUser(userUpdateRequestDTO.getId());
            user.setUsername(userUpdateRequestDTO.getUsername());
            user.setEmail(userUpdateRequestDTO.getEmail());
            user.setRoles(user.getRoles());
            entityManager.flush();
            user = userRepository.save(user);
            return convertUserToUserResponseDTO(user);
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    public boolean deleteUserLogical(UserRequestDTO userRequestDTO) throws InvalidRequestException {
        checkIfIdExists(userRequestDTO.getId());
        if (getUser(authorizationService.getSessionUsername()).getId().equals(userRequestDTO.getId())) {
            throw new InvalidRequestException(messageService.getMessage(MSG_USER_CANNOT_DELETE), null);
        }
        try {
            User user = getUser(userRequestDTO.getId());
            user.setEnabled(false);
            userRepository.save(user);
            return true;
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return false;
    }

    @Transactional(rollbackOn = Exception.class)
    public boolean deleteUserHard(UserRequestDTO userRequestDTO) throws InvalidRequestException {
        checkIfIdExists(userRequestDTO.getId());
        if (getUser(authorizationService.getSessionUsername()).getId().equals(userRequestDTO.getId())) {
            throw new InvalidRequestException(messageService.getMessage(MSG_USER_CANNOT_DELETE), null);
        }
        try {
            userRepository.delete(getUser(userRequestDTO.getId()));
            return true;
        } catch (Exception exception) {
            ExceptionLogger.log(exception);
        }
        return false;
    }
}
