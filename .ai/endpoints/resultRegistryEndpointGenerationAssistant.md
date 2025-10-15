## 1. Key Points from API Specification

- __Method__: POST

- __Path__: `/api/auth/register`

- __Purpose__: Register a new user account

- __Request Body__:

    - email: User's email address
    - password: User's password
    - timezone: User's timezone (e.g., "Europe/Warsaw")

- __Response__:

    - id: UUID of the created user
    - email: User's email address
    - created_at: Timestamp of account creation

- __Success Status Code__: 201 Created

- __Error Status Codes__:

    - 400 Bad Request - Invalid email or password
    - 409 Conflict - Email already in use

## 2. Parameters Analysis

### Required Parameters:

- email (string): Must be a valid email format
- password (string): Should meet security requirements
- timezone (string): Should be a valid timezone identifier

### Optional Parameters:

- None in the provided specification

## 3. DTO and Command Models

Based on the analysis of `src/types.ts`, we have:

### Existing DTOs:

- `RegisterDto`: Contains required fields (email, password, timezone)
- `UserDto`: Contains fields needed for response (id, email, created_at)

### Existing Command Models:

- `RegisterUserCommand`: Contains required fields for registration logic (email, password, timezone)

These existing types align perfectly with our endpoint requirements, so no new types need to be created.

## 4. Service Logic

The registration process should:

1. Validate input data (email format, password strength, timezone validity)
2. Check if email is already in use
3. Create a user record in Supabase Auth
4. Create a corresponding user record in the `users` table
5. Return the newly created user details

We should extract this logic into a dedicated service function in a new or existing authentication service file, potentially at `src/lib/services/auth.service.ts`.

## 5. Input Validation

We should implement validation using Zod as per the implementation rules:

- Validate email format
- Validate password strength (min length, complexity)
- Validate timezone against a list of valid timezone identifiers

## 6. Security Considerations

Potential security concerns:

1. __Password Storage__: Ensure passwords are properly hashed (handled by Supabase Auth)
2. __Rate Limiting__: Consider implementing rate limiting to prevent brute force attacks
3. __Input Sanitization__: Validate and sanitize all inputs
4. __CSRF Protection__: Ensure proper CSRF protection is in place

## 7. Error Scenarios

Potential error scenarios to handle:

1. Invalid email format (400 Bad Request)
2. Invalid password format/strength (400 Bad Request)
3. Invalid timezone (400 Bad Request)
4. Email already in use (409 Conflict)
5. Supabase Auth service errors (500 Internal Server Error)
6. Database errors (500 Internal Server Error)

## 8. Implementation Approach

We'll need to:

1. Create an API route handler at `/src/pages/api/auth/register.ts`
2. Create or update an authentication service at `/src/lib/services/auth.service.ts`
3. Implement input validation using Zod
4. Use Supabase from context.locals as per the backend rules
5. Implement proper error handling
6. Return appropriate HTTP status codes and response body

This approach aligns with the Astro and backend implementation rules specified in the project.

# API Endpoint Implementation Plan: POST /api/auth/register

## 1. Przegląd punktu końcowego

Endpoint `/api/auth/register` umożliwia rejestrację nowych użytkowników w aplikacji. Przyjmuje dane użytkownika (email, hasło, strefa czasowa), tworzy nowe konto w systemie autentykacji Supabase, a następnie tworzy odpowiadający rekord w tabeli `users`. Po pomyślnym utworzeniu konta, endpoint zwraca podstawowe informacje o utworzonym użytkowniku.

## 2. Szczegóły żądania

- __Metoda HTTP__: POST

- __Struktura URL__: `/api/auth/register`

- __Parametry__:

    - __Wymagane__:

        - email (string): Adres email użytkownika
        - password (string): Hasło użytkownika
        - timezone (string): Strefa czasowa użytkownika (np. "Europe/Warsaw")

    - __Opcjonalne__: Brak

- __Request Body__:

  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "timezone": "Europe/Warsaw"
  }
  ```

## 3. Wykorzystywane typy

### DTOs:

- __RegisterDto__ (już istnieje w `src/types.ts`):

  ```typescript
  export interface RegisterDto {
    email: string;
    password: string;
    timezone: string;
  }
  ```

- __UserDto__ (już istnieje w `src/types.ts`):

  ```typescript
  export interface UserDto {
    id: string;
    email: string | null;
    created_at?: string;
  }
  ```

### Command Models:

- __RegisterUserCommand__ (już istnieje w `src/types.ts`):

  ```typescript
  export interface RegisterUserCommand {
    email: string;
    password: string;
    timezone: string;
  }
  ```

## 4. Szczegóły odpowiedzi

- __Sukces__:

    - Status: 201 Created

    - Body:

      ```json
      {
        "id": "uuid",
        "email": "user@example.com",
        "created_at": "2025-10-12T09:45:03Z"
      }
      ```

- __Błąd - Nieprawidłowe dane wejściowe__:

    - Status: 400 Bad Request

    - Body:

      ```json
      {
        "error": "Nieprawidłowy adres email lub hasło"
      }
      ```

- __Błąd - Email już istnieje__:

    - Status: 409 Conflict

    - Body:

      ```json
      {
        "error": "Adres email jest już używany"
      }
      ```

## 5. Przepływ danych

1. Endpoint otrzymuje żądanie POST z danymi rejestracyjnymi użytkownika

2. Dane wejściowe są walidowane za pomocą Zod (format email, siła hasła, poprawność strefy czasowej)

3. Endpoint wywołuje serwis autentykacji, który:

    - Sprawdza, czy użytkownik o podanym adresie email już istnieje
    - Jeśli nie, tworzy nowego użytkownika w Supabase Auth
    - Tworzy odpowiadający rekord w tabeli `users` z podanymi danymi
    - Zwraca dane utworzonego użytkownika

4. Endpoint zwraca odpowiedź z danymi użytkownika i statusem 201 Created

## 6. Względy bezpieczeństwa

1. __Walidacja danych wejściowych__:

    - Email musi być w poprawnym formacie
    - Hasło powinno spełniać minimalne wymagania bezpieczeństwa (min. 8 znaków, zawierać wielkie i małe litery, cyfry)
    - Strefa czasowa musi być poprawnym identyfikatorem strefy czasowej

2. __Przechowywanie haseł__:

    - Hasła są bezpiecznie przechowywane przez Supabase Auth (używa bcrypt do hashowania)

3. __Ochrona przed atakami__:

    - Implementacja rate limiting na poziomie endpointu lub poprzez konfigurację Supabase
    - Sanityzacja danych wejściowych dla zapobiegania atakom XSS
    - Poprawna obsługa błędów bez ujawniania wrażliwych informacji

## 7. Obsługa błędów

1. __Nieprawidłowy format danych wejściowych__:

    - Status: 400 Bad Request
    - Komunikat: Szczegółowa informacja o problemie (np. "Nieprawidłowy format adresu email")

2. __Email już istnieje__:

    - Status: 409 Conflict
    - Komunikat: "Adres email jest już używany"

3. __Błąd usługi autentykacji__:

    - Status: 500 Internal Server Error
    - Komunikat: Ogólny komunikat błędu bez ujawniania szczegółów technicznych
    - Logowanie szczegółów błędu do systemu logowania

4. __Błąd bazy danych__:

    - Status: 500 Internal Server Error
    - Komunikat: Ogólny komunikat błędu
    - Logowanie szczegółów błędu do systemu logowania

## 8. Rozważania dotyczące wydajności

1. __Optymalizacja zapytań__:

    - Użycie indeksów na kolumnie email w tabeli users
    - Minimalizacja liczby zapytań do bazy danych

2. __Walidacja danych__:

    - Walidacja po stronie klienta przed wysłaniem żądania
    - Kompleksowa walidacja po stronie serwera

## 9. Etapy wdrożenia

1. __Utworzenie schematu walidacji__:

    - Implementacja schematu Zod dla walidacji danych wejściowych w `src/lib/schemas/auth.schema.ts`

2. __Implementacja serwisu autentykacji__:

    - Utworzenie/rozszerzenie `src/lib/services/auth.service.ts` z funkcją `registerUser`

3. __Implementacja handlera endpointu__:

    - Utworzenie pliku `src/pages/api/auth/register.ts`
    - Implementacja logiki endpointu korzystającej z serwisu autentykacji
    - Obsługa odpowiedzi i błędów

4. __Testowanie__:

    - Testy jednostkowe dla walidacji i logiki biznesowej
    - Testy integracyjne dla całego przepływu rejestracji
    - Testy bezpieczeństwa (próby rejestracji z nieprawidłowymi danymi, próby wstrzyknięcia kodu)

5. __Dokumentacja__:

    - Aktualizacja dokumentacji API
    - Dokumentacja wewnętrzna (komentarze, README)
