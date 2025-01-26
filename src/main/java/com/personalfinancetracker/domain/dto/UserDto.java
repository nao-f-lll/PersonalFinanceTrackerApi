package com.personalfinancetracker.domain.dto;


import com.personalfinancetracker.domain.dto.validation.CreateGroup;
import com.personalfinancetracker.domain.dto.validation.FullUpdateGroup;
import com.personalfinancetracker.domain.dto.validation.PartialUpdateGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(groups = {CreateGroup.class, FullUpdateGroup.class})
    private String name;
    @NotNull(groups = {CreateGroup.class, FullUpdateGroup.class})
    @Email(groups = {CreateGroup.class, FullUpdateGroup.class, PartialUpdateGroup.class})
    private String email;
    @NotNull(groups = {CreateGroup.class, FullUpdateGroup.class})
    @Pattern(
           regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&/()])[A-Za-z\\d@$!%*?&/()]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one special character, one number, and be at least 8 characters long.",
            groups = {CreateGroup.class, FullUpdateGroup.class, PartialUpdateGroup.class}
    )
    private String password;
    private Date creation_date;
    private Date update_date;
}
