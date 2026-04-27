package com.ingeniosi.ingeniosi_task_api.service;

import com.ingeniosi.ingeniosi_task_api.client.PetstoreClient;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetRequestDto;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetResponseDto;
import com.ingeniosi.ingeniosi_task_api.model.PetResponseDto;
import com.ingeniosi.ingeniosi_task_api.service.impl.PetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetstoreClient petstoreClient;

    @InjectMocks
    private PetServiceImpl petService;

    @Test
    void getPetByIdShouldReturnPetFromClient() {
        Long petId = 99L;
        PetResponseDto expected = new PetResponseDto(petId, "Luna", "available");
        when(petstoreClient.getPetById(petId)).thenReturn(expected);

        PetResponseDto result = petService.getPetById(petId);

        assertEquals(expected, result);
        verify(petstoreClient).getPetById(petId);
    }

    @Test
    void createPetShouldReturnResponseWithCreatedPetData() {
        CreatePetRequestDto request = new CreatePetRequestDto(15L, "available", "Firulais");
        PetResponseDto createdPet = new PetResponseDto(15L, "Firulais", "available");
        when(petstoreClient.createPet(request)).thenReturn(createdPet);

        CreatePetResponseDto result = petService.createPet(request);

        assertNotNull(result.getTransactionId());
        assertNotNull(result.getDateCreated());
        LocalDateTime createdAt = LocalDateTime.parse(result.getDateCreated(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertEquals(0, createdAt.getNano());
        assertEquals("available", result.getStatus());
        assertEquals("Firulais", result.getName());
        verify(petstoreClient).createPet(request);
    }
}
