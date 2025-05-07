package com.personalfinancetracker.security;

import com.personalfinancetracker.domain.entities.UserEntity;
import com.personalfinancetracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         UserEntity user = userRepository.findByEmail(email)
                 .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new AppUserDetails(user);
    }
}
