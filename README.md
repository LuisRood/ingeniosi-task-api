# ingeniosi-task-api

API en Spring Boot que consume la API externa de Swagger Petstore para:
- Consultar una mascota por ID.
- Crear una mascota y devolver una respuesta de negocio con `transactionId` y `dateCreated`.

## Stack
- Java 17
- Spring Boot 3.5.x
- Spring Web
- Spring Validation
- springdoc-openapi (Swagger UI)
- JUnit 5 + Mockito + MockMvc
- Gradle

## Requisitos
- JDK 17
- Gradle Wrapper (incluido en el repo)

## Configuración
Archivo: `src/main/resources/application.properties`

Propiedades principales:
- `server.port=8080`
- `petstore.base-url=https://petstore.swagger.io/v2`

## Ejecución local
```bash
./gradlew bootRun
```

En Windows (PowerShell):
```powershell
.\gradlew.bat bootRun
```

## Documentación API
Con la aplicación levantada:
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints
Base path: `/api/pet`

1. `GET /api/pet/{petId}`

Obtiene una mascota desde Petstore.

Ejemplo:
```bash
curl http://localhost:8080/api/pet/8
```

2. `POST /api/pet`

Crea una mascota en Petstore.

Body:
```json
{
  "id": 13,
  "status": "available",
  "name": "Nina"
}
```

Respuesta esperada:
```json
{
  "transactionId": "<uuid>",
  "dateCreated": "2026-04-26T19:45:00",
  "status": "available",
  "name": "Nina"
}
```

## Validaciones
`CreatePetRequestDto` aplica:
- `id`: requerido y mayor a 0.
- `status`: requerido y no vacío.
- `name`: requerido y no vacío.

## Manejo de errores
Se manejan errores de negocio y técnicos con `GlobalExceptionHandler`.

Formato de error:
```json
{
  "timestamp": "2026-04-26T19:45:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/pet",
  "details": [
    "id: id must be greater than zero"
  ]
}
```

Mapeo principal:
- `400`: validación, JSON malformado, parámetros inválidos o `BadRequestException`.
- `404`: `ResourceNotFoundException`.
- `502`: `ExternalServiceException` (fallas/errores inesperados de Petstore).
- `500`: error no controlado.

## Pruebas
Ejecutar tests:
```bash
./gradlew test
```

En Windows (PowerShell):
```powershell
.\gradlew.bat test
```

Cobertura implementada:
- `PetstoreClientTest`
- `PetServiceTest`
- `PetControllerTest`

## Calidad de código
Se agregó configuración de SonarQube en `build.gradle` (`org.sonarqube`), con:
- `sonar.projectKey = LuisRood_ingeniosi-task-api`
- `sonar.organization = luisrood`
