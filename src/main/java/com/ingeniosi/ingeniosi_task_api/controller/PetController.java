package com.ingeniosi.ingeniosi_task_api.controller;

import com.ingeniosi.ingeniosi_task_api.model.CreatePetRequestDto;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetResponseDto;
import com.ingeniosi.ingeniosi_task_api.model.PetResponseDto;
import com.ingeniosi.ingeniosi_task_api.service.PetService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/{petId}")
    public PetResponseDto getPetById(@PathVariable("petId") Long idPet) {
        return petService.getPetById(idPet);
    }

    @PostMapping
    public CreatePetResponseDto createPet(@Valid @RequestBody CreatePetRequestDto request) {
        return petService.createPet(request);
    }
}
