package com.personalfinancetracker.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public interface AuthenticationService {
    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);
}
