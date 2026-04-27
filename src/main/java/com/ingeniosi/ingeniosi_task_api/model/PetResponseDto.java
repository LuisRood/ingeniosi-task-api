package com.ingeniosi.ingeniosi_task_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponseDto {
    private Long id;
    private String name;
    private String status;
}
