package com.personalfinancetracker.controllers;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.dto.FinancialGoalDto;
import com.personalfinancetracker.domain.dto.UserDto;
import com.personalfinancetracker.domain.dto.validation.CreateGroup;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.domain.entities.FinancialGoalEntity;
import com.personalfinancetracker.domain.entities.UserEntity;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.service.FinancialGoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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



    @PostMapping(path = "/v1/financial-goal")
    public ResponseEntity<Object> creatFinancialGoal(@Validated(CreateGroup.class) @RequestBody FinancialGoalDto financialGoalDto) {
        try {
            FinancialGoalEntity financialGoalEntity = financialGoalMapper.mapFrom(financialGoalDto);
            logger.info("MAP FROM DTO TO ENTITY IS WILL");
            FinancialGoalEntity savedFinancialGoal = financialGoalService.create(financialGoalEntity);
            if (savedFinancialGoal != null) {
                FinancialGoalDto response = financialGoalMapper.mapTo(savedFinancialGoal);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("The user does not exists",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("ERROR", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}


