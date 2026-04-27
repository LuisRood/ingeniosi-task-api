package com.ingeniosi.ingeniosi_task_api.client;

import com.ingeniosi.ingeniosi_task_api.exception.BadRequestException;
import com.ingeniosi.ingeniosi_task_api.exception.ExternalServiceException;
import com.ingeniosi.ingeniosi_task_api.exception.ResourceNotFoundException;
import com.ingeniosi.ingeniosi_task_api.model.CreatePetRequestDto;
import com.ingeniosi.ingeniosi_task_api.model.PetResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PetstoreClient {

    private final RestTemplate restTemplate;
    private final String petstoreBaseUrl;

    public PetstoreClient(
            RestTemplate restTemplate,
            @Value("${petstore.base-url}") String petstoreBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.petstoreBaseUrl = petstoreBaseUrl;
    }

    public PetResponseDto getPetById(Long petId) {
        String url = petstoreBaseUrl + "/pet/" + petId;
        try {
            return restTemplate.getForObject(url, PetResponseDto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Pet with id " + petId + " was not found");
        } catch (HttpClientErrorException.BadRequest ex) {
            throw new BadRequestException("Invalid request when fetching pet from Petstore");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ExternalServiceException("Petstore responded with an unexpected error", ex);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Could not connect to Petstore", ex);
        }
    }

    public PetResponseDto createPet(CreatePetRequestDto request) {
        String url = petstoreBaseUrl + "/pet";
        try {
            return restTemplate.postForObject(url, request, PetResponseDto.class);
        } catch (HttpClientErrorException.BadRequest ex) {
            throw new BadRequestException("Invalid request when creating pet in Petstore");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ExternalServiceException("Petstore responded with an unexpected error", ex);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Could not connect to Petstore", ex);
        }
    }
}
