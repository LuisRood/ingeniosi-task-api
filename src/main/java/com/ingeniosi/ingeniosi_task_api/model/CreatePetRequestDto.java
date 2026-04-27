package com.ingeniosi.ingeniosi_task_api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePetRequestDto {
    @NotNull(message = "id is required")
    @Positive(message = "id must be greater than zero")
    private Long id;

    @NotBlank(message = "status is required")
    private String status;

    @NotBlank(message = "name is required")
    private String name;
}
