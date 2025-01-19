package com.personalfinancetracker.personal_finance_tracker.controllers;

import com.personalfinancetracker.personal_finance_tracker.domain.dto.UserDto;
import com.personalfinancetracker.personal_finance_tracker.domain.entities.UserEntity;
import com.personalfinancetracker.personal_finance_tracker.exceptions.ErrorResponse;
import com.personalfinancetracker.personal_finance_tracker.mapper.Mapper;
import com.personalfinancetracker.personal_finance_tracker.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;

    @Autowired
    public UserController(Mapper<UserEntity, UserDto> userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @GetMapping(path = "/v1/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        Optional<UserEntity> foundUser = userService.findOne(id);
        return foundUser.map(userEntity -> {
            userEntity.setId(null);
            userEntity.setPassword(null);
            userEntity.setCreation_date(null);
            UserDto response = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(path= "/v1/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {

        if (userService.isExists(userDto.getEmail())) {
            ErrorResponse errorResponse = ErrorResponse
                                            .builder()
                                            .message("EMAIL ALREADY EXIST")
                                            .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity savedUser = userService.create(userEntity);
            UserDto response = userMapper.mapTo(savedUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/v1/users/{id}")
    public ResponseEntity<Object> fullUpdateAuthor(@PathVariable("id") Long id, @Valid @RequestBody UserDto userDto) {
        if (!userService.isExists(id)) {
            ErrorResponse errorResponse = ErrorResponse
                    .builder()
                    .message("User Does Not Exists")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        userDto.setId(id);
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity savedUser = userService.fullUpdate(userEntity);
            UserDto response = userMapper.mapTo(savedUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/v1/users/{id}")
    public ResponseEntity<Object> partialUpdate(@PathVariable("id") Long id, @Valid @RequestBody UserDto userDto) {
        if (!userService.isExists(id)) {
            ErrorResponse errorResponse = ErrorResponse
                    .builder()
                    .message("User Does Not Exists")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        userDto.setId(id);
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity savedUser = userService.partialUpdate(userEntity);
            UserDto response = userMapper.mapTo(savedUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/v1/users/{id}")
    public ResponseEntity deleteAuthor(@PathVariable("id") Long id) {
        if (userService.isExists(id)) {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}