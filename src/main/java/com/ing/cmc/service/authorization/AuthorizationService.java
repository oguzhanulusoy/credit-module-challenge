package com.ing.cmc.service.authorization;

import com.ing.cmc.common.enums.RoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorizationService {

    public boolean isAdmin() {
        for (String role : getSessionUserRoles()) {
            if (role.equals(RoleEnum.ADMIN.toString())) {
                return true;
            }
        }
        return false;
    }

    public List<String> getSessionUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public String getSessionUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
