package com.personalfinancetracker.domain.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    @Email
    private String email;
    @Pattern(
           regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&/()])[A-Za-z\\d@$!%*?&/()]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one special character, one number, and be at least 8 characters long."
    )
    private String password;
    private Date creation_date;
    private Date update_date;
}
