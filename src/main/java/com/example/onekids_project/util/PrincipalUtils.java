package com.example.onekids_project.util;

import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

/**
 * date 2021-09-23 16:13
 *
 * @author lavanviet
 */
public class PrincipalUtils {
    public static UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Do not token for access");
        }
        return (UserPrincipal) authentication.getPrincipal();
    }
}
