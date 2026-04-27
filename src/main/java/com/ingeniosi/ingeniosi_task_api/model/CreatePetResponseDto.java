package com.ingeniosi.ingeniosi_task_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePetResponseDto {
    private String transactionId;
    private String dateCreated;
    private String status;
    private String name;
}
