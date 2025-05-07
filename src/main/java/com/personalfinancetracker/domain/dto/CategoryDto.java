package com.personalfinancetracker.domain.dto;

import com.personalfinancetracker.domain.dto.validation.CreateGroup;
import com.personalfinancetracker.domain.dto.validation.PartialUpdateGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long id;

    @NotNull(groups = {CreateGroup.class, PartialUpdateGroup.class})
    private String name;

    @NotNull(groups = {CreateGroup.class, PartialUpdateGroup.class})
    private CategoryDto parentCategory;

    private String description;

//    @NotNull(message = "user id cannot be null", groups = CreateGroup.class)
//    private UserDto userDto;

    @NotNull(message = "Category type cannot be null", groups = {CreateGroup.class, PartialUpdateGroup.class})
    private String type;
}