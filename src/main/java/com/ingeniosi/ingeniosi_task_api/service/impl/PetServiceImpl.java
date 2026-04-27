package com.ingeniosi.ingeniosi_task_api.service.impl;

import com.ingeniosi.ingeniosi_task_api.client.PetstoreClient;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetRequestDto;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetResponseDto;
import com.ingeniosi.ingeniosi_task_api.model.PetResponseDto;
import com.ingeniosi.ingeniosi_task_api.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class PetServiceImpl implements PetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final PetstoreClient petstoreClient;

    public PetServiceImpl(PetstoreClient petstoreClient) {
        this.petstoreClient = petstoreClient;
    }

    @Override
    public PetResponseDto getPetById(Long petId) {
        PetResponseDto pet = petstoreClient.getPetById(petId);
        LOGGER.info("Pet obtenido de Petstore: {}", pet);
        return pet;
    }

    @Override
    public CreatePetResponseDto createPet(CreatePetRequestDto request) {
        PetResponseDto createdPet = petstoreClient.createPet(request);
        LOGGER.info("Pet creada en Petstore con id {}: {}", createdPet.getId(), createdPet);

        return new CreatePetResponseDto(
                UUID.randomUUID().toString(),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DATE_FORMATTER),
                createdPet.getStatus(),
                createdPet.getName()
        );
    }
}
