package com.personalfinancetracker.controllers;

import com.personalfinancetracker.domain.dto.UserDto;
import com.personalfinancetracker.domain.dto.validation.CreateGroup;
import com.personalfinancetracker.domain.dto.validation.FullUpdateGroup;
import com.personalfinancetracker.domain.dto.validation.PartialUpdateGroup;
import com.personalfinancetracker.domain.entities.UserEntity;
import com.personalfinancetracker.exceptions.ErrorResponse;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.personalfinancetracker.utils.DataSanitizer.sanitize;

@RestController
@Tag(name = "User")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;

    @Autowired
    public UserController(Mapper<UserEntity, UserDto> userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Operation(
          description = "Get endpoint for a specific user (e.g the user that has the given id)",
            responses = {
                  @ApiResponse(
                          description = "OK",
                          responseCode = "200"
                  ),
                    @ApiResponse(
                            description = "NOT FOUND",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping(path = "/v1/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        Optional<UserEntity> foundUser = userService.findOne(id);
        return foundUser.map(userEntity -> {
            UserDto response = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(sanitize(response), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(path= "/v1/users")
    public ResponseEntity<Object> createUser(@Validated(CreateGroup.class) @RequestBody UserDto userDto) {
        ResponseEntity<Object> errorResponse = emailExistsException(userDto);
        if (errorResponse != null) return errorResponse;
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity savedUser = userService.create(userEntity);
            UserDto response = userMapper.mapTo(savedUser);
            return new ResponseEntity<>(sanitize(response), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/v1/users/{id}")
    public ResponseEntity<Object> fullUpdateAuthor(@PathVariable("id") Long id, @Validated(FullUpdateGroup.class) @RequestBody UserDto userDto) {
        ResponseEntity<Object> errorResponse1 = userDoesNotExistsException(id);
        if (errorResponse1 != null) return errorResponse1;
        ResponseEntity<Object> errorResponse = emailExistsException(userDto);
        if (errorResponse != null) return errorResponse;
        userDto.setId(id);
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity savedUser = userService.fullUpdate(userEntity);
            UserDto response = userMapper.mapTo(savedUser);
            return new ResponseEntity<>(sanitize(response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/v1/users/{id}")
    public ResponseEntity<Object> partialUpdate(@PathVariable("id") Long id, @Validated(PartialUpdateGroup.class) @RequestBody UserDto userDto) {
        ResponseEntity<Object> errorResponse1 = userDoesNotExistsException(id);
        if (errorResponse1 != null) return errorResponse1;
        ResponseEntity<Object> errorResponse = emailExistsException(userDto);
        if (errorResponse != null) return errorResponse;
        userDto.setId(id);
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity savedUser = userService.partialUpdate(userEntity);
            UserDto response = userMapper.mapTo(savedUser);
            return new ResponseEntity<>(sanitize(response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/v1/users/{id}")
    public ResponseEntity<Object> deleteAuthor(@PathVariable("id") Long id) {
        if (userService.isExists(id)) {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> emailExistsException(UserDto userDto) {
        if (userService.isExists(userDto.getEmail())) {
            ErrorResponse errorResponse = ErrorResponse
                    .builder()
                    .message("EMAIL ALREADY EXIST")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private ResponseEntity<Object> userDoesNotExistsException(Long id) {
        if (!userService.isExists(id)) {
            ErrorResponse errorResponse = ErrorResponse
                    .builder()
                    .message("User Does Not Exists")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        return null;
    }
}