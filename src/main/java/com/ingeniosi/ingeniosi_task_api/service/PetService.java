package com.ingeniosi.ingeniosi_task_api.service;

import com.ingeniosi.ingeniosi_task_api.model.CreatePetRequestDto;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetResponseDto;
import com.ingeniosi.ingeniosi_task_api.model.PetResponseDto;

public interface PetService {

    PetResponseDto getPetById(Long petId);

    CreatePetResponseDto createPet(CreatePetRequestDto request);
}
