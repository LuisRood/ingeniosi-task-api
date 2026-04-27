package com.ingeniosi.ingeniosi_task_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingeniosi.ingeniosi_task_api.exception.ResourceNotFoundException;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetRequestDto;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetResponseDto;
import com.ingeniosi.ingeniosi_task_api.model.PetResponseDto;
import com.ingeniosi.ingeniosi_task_api.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetService petService;

    @Test
    void getPetByIdShouldReturnPetResponse() throws Exception {
        Long petId = 12L;
        PetResponseDto response = new PetResponseDto(petId, "Milo", "available");
        when(petService.getPetById(petId)).thenReturn(response);

        mockMvc.perform(get("/api/pet/{petId}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.name").value("Milo"))
                .andExpect(jsonPath("$.status").value("available"));

        verify(petService).getPetById(petId);
    }

    @Test
    void createPetShouldReturnCreatePetResponse() throws Exception {
        CreatePetRequestDto request = new CreatePetRequestDto(31L, "available", "Rocky");
        CreatePetResponseDto response = new CreatePetResponseDto(
                "tx-123",
                "2026-04-26T19:45:00",
                "available",
                "Rocky"
        );
        when(petService.createPet(request)).thenReturn(response);

        mockMvc.perform(post("/api/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx-123"))
                .andExpect(jsonPath("$.dateCreated").value("2026-04-26T19:45:00"))
                .andExpect(jsonPath("$.status").value("available"))
                .andExpect(jsonPath("$.name").value("Rocky"));

        verify(petService).createPet(request);
    }

    @Test
    void getPetByIdShouldReturnNotFoundWhenServiceThrowsResourceNotFound() throws Exception {
        Long petId = 999L;
        when(petService.getPetById(petId)).thenThrow(new ResourceNotFoundException("Pet with id 999 was not found"));

        mockMvc.perform(get("/api/pet/{petId}", petId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Pet with id 999 was not found"))
                .andExpect(jsonPath("$.path").value("/api/pet/999"));
    }

    @Test
    void createPetShouldReturnBadRequestWhenValidationFails() throws Exception {
        Map<String, Object> invalidRequest = Map.of(
                "id", -3,
                "status", "",
                "name", ""
        );

        mockMvc.perform(post("/api/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.path").value("/api/pet"))
                .andExpect(jsonPath("$.details").isArray());

        verifyNoInteractions(petService);
    }
}
