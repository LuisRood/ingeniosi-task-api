package com.ingeniosi.ingeniosi_task_api.service;

import com.ingeniosi.ingeniosi_task_api.client.PetstoreClient;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetRequestDto;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetResponseDto;
import com.ingeniosi.ingeniosi_task_api.model.PetResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class PetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final PetstoreClient petstoreClient;

    public PetService(PetstoreClient petstoreClient) {
        this.petstoreClient = petstoreClient;
    }

    public PetResponseDto getPetById(Long idPet) {
        PetResponseDto pet = petstoreClient.getPetById(idPet);
        LOGGER.info("Pet obtenido de Petstore: {}", pet);
        return pet;
    }

    public CreatePetResponseDto createPet(CreatePetRequestDto request) {
        PetResponseDto createdPet = petstoreClient.createPet(request);
        LOGGER.info("Pet creada en Petstore con id {}: {}", createdPet.getId(), createdPet);

        return new CreatePetResponseDto(
                UUID.randomUUID().toString(),
                LocalDateTime.now().format(DATE_FORMATTER),
                createdPet.getStatus(),
                createdPet.getName()
        );
    }
}
