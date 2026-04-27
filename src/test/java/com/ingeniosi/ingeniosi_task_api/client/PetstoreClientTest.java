package com.ingeniosi.ingeniosi_task_api.client;

import com.ingeniosi.ingeniosi_task_api.exception.BadRequestException;
import com.ingeniosi.ingeniosi_task_api.exception.ExternalServiceException;
import com.ingeniosi.ingeniosi_task_api.exception.ResourceNotFoundException;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetRequestDto;
import com.ingeniosi.ingeniosi_task_api.model.PetResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetstoreClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Test
    void getPetByIdShouldCallPetstoreWithExpectedUrl() {
        PetstoreClient petstoreClient = new PetstoreClient(restTemplate, "https://petstore.swagger.io/v2");
        Long petId = 8L;
        PetResponseDto expected = new PetResponseDto(8L, "Toby", "available");
        when(restTemplate.getForObject("https://petstore.swagger.io/v2/pet/8", PetResponseDto.class))
                .thenReturn(expected);

        PetResponseDto result = petstoreClient.getPetById(petId);

        assertEquals(expected, result);
        verify(restTemplate).getForObject("https://petstore.swagger.io/v2/pet/8", PetResponseDto.class);
    }

    @Test
    void createPetShouldCallPetstoreWithExpectedUrl() {
        PetstoreClient petstoreClient = new PetstoreClient(restTemplate, "https://petstore.swagger.io/v2");
        CreatePetRequestDto request = new CreatePetRequestDto(13L, "available", "Nina");
        PetResponseDto expected = new PetResponseDto(13L, "Nina", "available");
        when(restTemplate.postForObject("https://petstore.swagger.io/v2/pet", request, PetResponseDto.class))
                .thenReturn(expected);

        PetResponseDto result = petstoreClient.createPet(request);

        assertEquals(expected, result);
        verify(restTemplate).postForObject("https://petstore.swagger.io/v2/pet", request, PetResponseDto.class);
    }

    @Test
    void getPetByIdShouldThrowResourceNotFoundWhenPetstoreReturns404() {
        PetstoreClient petstoreClient = new PetstoreClient(restTemplate, "https://petstore.swagger.io/v2");
        HttpClientErrorException notFound = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "Not Found",
                HttpHeaders.EMPTY,
                new byte[0],
                null
        );
        when(restTemplate.getForObject("https://petstore.swagger.io/v2/pet/8", PetResponseDto.class))
                .thenThrow(notFound);

        assertThrows(ResourceNotFoundException.class, () -> petstoreClient.getPetById(8L));
    }

    @Test
    void createPetShouldThrowBadRequestWhenPetstoreReturns400() {
        PetstoreClient petstoreClient = new PetstoreClient(restTemplate, "https://petstore.swagger.io/v2");
        CreatePetRequestDto request = new CreatePetRequestDto(13L, "available", "Nina");
        HttpClientErrorException badRequest = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                HttpHeaders.EMPTY,
                new byte[0],
                null
        );
        when(restTemplate.postForObject("https://petstore.swagger.io/v2/pet", request, PetResponseDto.class))
                .thenThrow(badRequest);

        assertThrows(BadRequestException.class, () -> petstoreClient.createPet(request));
    }

    @Test
    void createPetShouldThrowExternalServiceWhenConnectionFails() {
        PetstoreClient petstoreClient = new PetstoreClient(restTemplate, "https://petstore.swagger.io/v2");
        CreatePetRequestDto request = new CreatePetRequestDto(13L, "available", "Nina");
        when(restTemplate.postForObject("https://petstore.swagger.io/v2/pet", request, PetResponseDto.class))
                .thenThrow(new RestClientException("Connection refused"));

        assertThrows(ExternalServiceException.class, () -> petstoreClient.createPet(request));
    }
}
