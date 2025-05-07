package com.personalfinancetracker.controllers;

import com.personalfinancetracker.domain.dto.FinancialGoalDto;
import com.personalfinancetracker.domain.dto.validation.CreateGroup;
import com.personalfinancetracker.domain.dto.validation.PartialUpdateGroup;
import com.personalfinancetracker.domain.entities.FinancialGoalEntity;
import com.personalfinancetracker.exceptions.ErrorResponse;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.service.FinancialGoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.personalfinancetracker.utils.DataSanitizer.sanitize;

@RestController
public class FinancialGoalController {

    private static final Logger logger = LoggerFactory.getLogger(FinancialGoalController.class);
    private final Mapper<FinancialGoalEntity, FinancialGoalDto> financialGoalMapper;
    private final FinancialGoalService financialGoalService;

    @Autowired
    public FinancialGoalController(Mapper<FinancialGoalEntity, FinancialGoalDto> financialGoalMapper, FinancialGoalService financialGoalService) {
        this.financialGoalMapper = financialGoalMapper;
        this.financialGoalService = financialGoalService;
    }
    //TODO get financial-goals by it's id not the user id
    @GetMapping(path = "/v1/financial-goals/{user-id}")
    public Page<FinancialGoalDto> getFinancialGoals(Pageable pageable, @PathVariable("user-id") Long userId) {
        Page<FinancialGoalEntity> financialGoalEntities = financialGoalService.findAllByUserId(pageable, userId);
        Page<FinancialGoalDto> financialGoalDtos =  financialGoalEntities.map(financialGoalMapper::mapTo);
        return financialGoalDtos.map(financialGoalDto -> {
            financialGoalDto.setUserDto(sanitize(financialGoalDto.getUserDto()));
            return financialGoalDto;
        });
    }

    @PostMapping(path = "/v1/financial-goals")
    public ResponseEntity<Object> creatFinancialGoal(@Validated(CreateGroup.class) @RequestBody FinancialGoalDto financialGoalDto) {
        try {
            FinancialGoalEntity financialGoalEntity = financialGoalMapper.mapFrom(financialGoalDto);
            FinancialGoalEntity savedFinancialGoal = financialGoalService.create(financialGoalEntity);
            if (savedFinancialGoal != null) {
                FinancialGoalDto response = financialGoalMapper.mapTo(savedFinancialGoal);
                response.setUserDto(sanitize(response.getUserDto()));
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("The user does not exists",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/v1/financial-goals/{financial-goal-id}")
    public ResponseEntity<Object> partialUpdate(@PathVariable("financial-goal-id") Long financialGoalId,
                                                @Validated(PartialUpdateGroup.class) @RequestBody FinancialGoalDto financialGoalDto) {
        ResponseEntity<Object> errorResponse = doesFinancialGoalExists(financialGoalId);
        if (errorResponse != null) return errorResponse;
        financialGoalService.fillFinancialGoalDtoWithDetails(financialGoalDto, financialGoalId);
        try {
            FinancialGoalEntity financialGoalEntity = financialGoalMapper.mapFrom(financialGoalDto);
            FinancialGoalEntity saveFinancialGoal = financialGoalService.partialUpdate(financialGoalEntity);
            FinancialGoalDto response = financialGoalMapper.mapTo(saveFinancialGoal);
            response.setUserDto(sanitize(response.getUserDto()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(path = "/v1/financial-goals/{financial-goal-id}")
    public ResponseEntity<Object> deleteFinancialGoal(@PathVariable("financial-goal-id") Long financialGoalId) {
        ResponseEntity<Object> errorResponse = doesFinancialGoalExists(financialGoalId);
        if (errorResponse != null) return errorResponse;
        financialGoalService.delete(financialGoalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    private ResponseEntity<Object> doesFinancialGoalExists(Long financialGoalId) {
        if (!financialGoalService.isExists(financialGoalId)) {
            ErrorResponse errorResponse = ErrorResponse
                    .builder()
                    .message("Financial Goal Does Not Exists")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        return null;
    }
}