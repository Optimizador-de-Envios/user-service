# User Service

Este microservicio gestiona las cuentas de usuario dentro del proyecto de optimizacion de envios. Su funcion es registrar usuarios, autenticarlos y emitir tokens JWT para que otros modulos puedan asociar pedidos, historial y operaciones a un usuario autenticado.

URL base en local: `http://localhost:8081/api/users`

## Endpoints

### POST `/register`
Registra un nuevo usuario.

Request:

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "securepass123"
}
```

Response exitosa `201 Created`:

```json
{
  "id": "2d5f8f5b-6e5c-4d95-8f31-5c2e7df6a111",
  "name": "Jane Doe",
  "email": "jane@example.com",
  "createdAt": "2026-04-05T18:30:00Z"
}
```

### POST `/login`
Autentica a un usuario registrado y retorna un JWT.

Request:

```json
{
  "email": "jane@example.com",
  "password": "securepass123"
}
```

Response exitosa `200 OK`:

```json
{
  "accessToken": "<jwt>",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": "2d5f8f5b-6e5c-4d95-8f31-5c2e7df6a111",
    "name": "Jane Doe",
    "email": "jane@example.com"
  }
}
```

## Errores comunes

Formato de error:

```json
{
  "code": "VALIDATION_ERROR",
  "message": "The request contains invalid data",
  "errors": [
    "email: Email must be a valid email"
  ]
}
```

Codigos posibles: `VALIDATION_ERROR`, `INVALID_JSON`, `DUPLICATE_EMAIL`, `INVALID_CREDENTIALS`, `INVALID_USER_DATA`.

## Notas JWT

- El login retorna un JWT firmado con `tokenType: Bearer`.
- Para endpoints protegidos, envia `Authorization: Bearer <accessToken>`.
- Configuracion requerida: `JWT_SECRET`.
- Configuracion opcional: `JWT_EXPIRATION_SECONDS` (por defecto `86400`).
- La configuracion local se toma desde `application.properties` y tambien puede cargarse desde un archivo `.env`.