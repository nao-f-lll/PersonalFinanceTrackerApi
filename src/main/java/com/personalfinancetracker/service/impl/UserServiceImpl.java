package com.personalfinancetracker.service.impl;

import com.personalfinancetracker.domain.entities.UserEntity;
import com.personalfinancetracker.repositories.UserRepository;
import com.personalfinancetracker.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        userEntity.setCreation_date(currentDateTime);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity fullUpdate(UserEntity userEntity) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return userRepository.findById(userEntity.getId()).map(existingUser -> {
            existingUser.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            existingUser.setName(userEntity.getName());
            existingUser.setEmail(userEntity.getEmail());
            existingUser.setUpdate_date(currentDateTime);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User Does Not Exist"));
    }

    @Override
    public UserEntity partialUpdate(UserEntity userEntity) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return userRepository.findById(userEntity.getId()).map(existingUser -> {
            Optional.ofNullable(userEntity.getName()).ifPresent(existingUser::setName);
            Optional.ofNullable(userEntity.getEmail()).ifPresent(existingUser::setEmail);
            Optional.ofNullable(userEntity.getPassword()).ifPresent(
                    password -> existingUser.setPassword(passwordEncoder.encode(password))
            );
            existingUser.setUpdate_date(currentDateTime);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User Does Not Exist"));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserEntity> findOne(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Boolean isExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean isExists(Long id) {
        return userRepository.existsById(id);
    }

}