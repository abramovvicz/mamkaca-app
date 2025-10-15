# API Endpoint Implementation Plan: Login & Logout

## 1. Przegląd punktów końcowych

### Login Endpoint

Endpoint `/api/auth/login` umożliwia uwierzytelnienie użytkownika w aplikacji. Przyjmuje dane logowania (email, hasło), weryfikuje je w systemie autentykacji Supabase, a w przypadku poprawnej weryfikacji zwraca tokeny dostępowe oraz podstawowe informacje o użytkowniku.

### Logout Endpoint

Endpoint `/api/auth/logout` umożliwia wylogowanie użytkownika poprzez unieważnienie jego aktualnej sesji. Wymaga przekazania tokenu uwierzytelniającego w nagłówku i nie zwraca treści odpowiedzi w przypadku sukcesu.

## 2. Szczegóły żądań

### Login

- __Metoda HTTP__: POST
- __Struktura URL__: `/api/auth/login`
- __Parametry__:
  - __Wymagane__:
    - email (string): Adres email użytkownika
    - password (string): Hasło użytkownika
  - __Opcjonalne__: Brak

- __Request Body__:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```

### Logout

- __Metoda HTTP__: POST
- __Struktura URL__: `/api/auth/logout`
- __Parametry__: Brak
- __Nagłówki__:
  - __Wymagane__:
    - Authorization: Bearer {access_token}
  - __Opcjonalne__: Brak
- __Request Body__: Brak

## 3. Wykorzystywane typy

### DTOs:

- __LoginDto__ (już istnieje w `src/types.ts`):
  ```typescript
  export interface LoginDto {
    email: string;
    password: string;
  }
  ```

- __AuthResponseDto__ (już istnieje w `src/types.ts`):
  ```typescript
  export interface AuthResponseDto {
    access_token: string;
    refresh_token: string;
    user: UserDto;
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

## 4. Szczegóły odpowiedzi

### Login

- __Sukces__:
  - Status: 200 OK
  - Body:
    ```json
    {
      "access_token": "jwt_token",
      "refresh_token": "refresh_token",
      "user": {
        "id": "uuid",
        "email": "user@example.com"
      }
    }
    ```

- __Błąd - Brak wymaganych danych__:
  - Status: 400 Bad Request
  - Body:
    ```json
    {
      "error": "Brak wymaganych danych",
      "details": { /* szczegóły błędów walidacji */ }
    }
    ```

- __Błąd - Nieprawidłowe dane logowania__:
  - Status: 401 Unauthorized
  - Body:
    ```json
    {
      "error": "Nieprawidłowy email lub hasło"
    }
    ```

### Logout

- __Sukces__:
  - Status: 204 No Content
  - Body: Brak

- __Błąd - Nieprawidłowy token__:
  - Status: 401 Unauthorized
  - Body:
    ```json
    {
      "error": "Nieprawidłowy token uwierzytelniający"
    }
    ```

## 5. Przepływ danych

### Login

1. Endpoint otrzymuje żądanie POST z danymi logowania użytkownika
2. Dane wejściowe są walidowane za pomocą Zod
3. Endpoint wywołuje serwis autentykacji, który:
   - Weryfikuje dane logowania w Supabase Auth
   - Pobiera dane użytkownika z bazy danych
   - Generuje i zwraca tokeny dostępowe wraz z danymi użytkownika
4. Endpoint zwraca odpowiedź z tokenami i danymi użytkownika

### Logout

1. Endpoint otrzymuje żądanie POST z tokenem uwierzytelniającym w nagłówku
2. Token jest weryfikowany i parsowany
3. Endpoint wywołuje serwis autentykacji, który unieważnia sesję użytkownika
4. Endpoint zwraca pustą odpowiedź ze statusem 204

## 6. Względy bezpieczeństwa

1. __Walidacja danych wejściowych__:
   - Email musi być w poprawnym formacie
   - Hasło powinno spełniać minimalne wymagania (walidacja po stronie klienta)

2. __Ochrona tokenów__:
   - Tokeny są przekazywane tylko przez HTTPS
   - Token refresh jest przechowywany w bezpieczny sposób (HttpOnly cookie)
   - Token access ma krótki czas życia

3. __Ochrona przed atakami__:
   - Implementacja rate limiting na poziomie endpointu
   - Walidacja i sanityzacja danych wejściowych
   - Bezpieczne przechowywanie i weryfikacja tokenów

4. __Obsługa sesji__:
   - Poprawne unieważnianie sesji przy wylogowaniu
   - Weryfikacja ważności tokenu przy każdym żądaniu

## 7. Obsługa błędów

### Login

1. __Nieprawidłowy format danych wejściowych__:
   - Status: 400 Bad Request
   - Komunikat: Szczegółowe informacje o błędach walidacji

2. __Nieprawidłowe dane logowania__:
   - Status: 401 Unauthorized
   - Komunikat: "Nieprawidłowy email lub hasło"

3. __Błąd usługi autentykacji__:
   - Status: 500 Internal Server Error
   - Komunikat: Ogólny komunikat błędu
   - Logowanie szczegółów błędu do systemu logowania

### Logout

1. __Brak tokenu uwierzytelniającego__:
   - Status: 401 Unauthorized
   - Komunikat: "Wymagane uwierzytelnienie"

2. __Nieprawidłowy token__:
   - Status: 401 Unauthorized
   - Komunikat: "Nieprawidłowy token uwierzytelniający"

3. __Wygasły token__:
   - Status: 401 Unauthorized
   - Komunikat: "Token uwierzytelniający wygasł"

4. __Błąd usługi autentykacji__:
   - Status: 500 Internal Server Error
   - Komunikat: Ogólny komunikat błędu
   - Logowanie szczegółów błędu do systemu logowania

## 8. Rozważania dotyczące wydajności

1. __Optymalizacja zapytań__:
   - Minimalizacja liczby zapytań do bazy danych
   - Indeksy na kolumnach używanych do wyszukiwania użytkowników

2. __Obsługa tokenów__:
   - Efektywne generowanie i weryfikacja tokenów JWT
   - Odpowiedni czas życia tokenów dla zrównoważenia bezpieczeństwa i doświadczenia użytkownika

## 9. Etapy wdrożenia

### Etap 1: Uzupełnienie schematów walidacji

1. Utwórz schemat loginSchema w `src/lib/schemas/auth.schema.ts`:
   ```typescript
   // Dodaj do istniejącego pliku auth.schema.ts
   
   /**
    * Schema for validating user login input
    */
   export const loginSchema = z.object({
     email: z
       .string({ required_error: 'Email jest wymagany' })
       .email('Nieprawidłowy format adresu email'),
     password: z
       .string({ required_error: 'Hasło jest wymagane' })
       .min(1, 'Hasło jest wymagane'),
   });
   
   export type LoginInput = z.infer<typeof loginSchema>;
   ```

### Etap 2: Rozszerzenie serwisu autentykacji

1. Dodaj funkcje loginUser i logoutUser w `src/lib/services/auth.service.ts`:
   ```typescript
   /**
    * Authenticates a user and returns access tokens
    * @param supabase SupabaseClient instance from context.locals
    * @param email User's email
    * @param password User's password
    * @returns Authentication data including tokens and user info
    * @throws Error if authentication fails
    */
   export async function loginUser(
     supabase: SupabaseClient<Database>,
     email: string,
     password: string
   ): Promise<AuthResponseDto> {
     // Sign in with email and password
     const { data: authData, error: authError } = await supabase.auth.signInWithPassword({
       email,
       password,
     });
     
     if (authError) {
       const unauthorizedError = new Error('Nieprawidłowy email lub hasło');
       // Add a status property to the error for HTTP status code identification
       (unauthorizedError as any).status = 401;
       throw unauthorizedError;
     }
     
     if (!authData?.user || !authData?.session) {
       const unauthorizedError = new Error('Nieprawidłowy email lub hasło');
       (unauthorizedError as any).status = 401;
       throw unauthorizedError;
     }
     
     // Get user details from the users table
     const { data: userData, error: userError } = await supabase
       .from('users')
       .select('id, email, created_at')
       .eq('id', authData.user.id)
       .single();
     
     if (userError) {
       throw new Error(`Błąd podczas pobierania danych użytkownika: ${userError.message}`);
     }
     
     // Return authentication response
     return {
       access_token: authData.session.access_token,
       refresh_token: authData.session.refresh_token,
       user: userData,
     };
   }
   
   /**
    * Logs out a user by invalidating their session
    * @param supabase SupabaseClient instance from context.locals
    * @returns Promise that resolves when logout is successful
    * @throws Error if logout fails
    */
   export async function logoutUser(
     supabase: SupabaseClient<Database>
   ): Promise<void> {
     const { error } = await supabase.auth.signOut();
     
     if (error) {
       throw new Error(`Błąd podczas wylogowywania: ${error.message}`);
     }
   }
   ```

### Etap 3: Implementacja endpointów

1. Utwórz endpoint logowania w `src/pages/api/auth/login.ts`:
   ```typescript
   import type { APIRoute } from 'astro';
   import { loginSchema } from '../../../lib/schemas/auth.schema';
   import { loginUser } from '../../../lib/services/auth.service';
   import type { LoginDto, AuthResponseDto } from '../../../types';

   // Disable static pre-rendering as this endpoint requires dynamic processing
   export const prerender = false;

   /**
    * POST handler for user login
    * 
    * This endpoint allows clients to authenticate using email and password,
    * and returns access tokens along with basic user information.
    */
   export const POST: APIRoute = async ({ request, locals }) => {
     try {
       // Parse and validate request body
       const body = await request.json() as LoginDto;
       
       const validationResult = loginSchema.safeParse(body);
       
       // Return 400 Bad Request if validation fails
       if (!validationResult.success) {
         return new Response(
           JSON.stringify({
             error: 'Brak wymaganych danych',
             details: validationResult.error.format(),
           }),
           {
             status: 400,
             headers: { 'Content-Type': 'application/json' },
           }
         );
       }
       
       // Get validated data
       const loginData = validationResult.data;
       
       // Authenticate user using auth service
       const authData: AuthResponseDto = await loginUser(
         locals.supabase, 
         loginData.email, 
         loginData.password
       );
       
       // Return 200 OK with authentication data
       return new Response(
         JSON.stringify(authData),
         {
           status: 200,
           headers: { 'Content-Type': 'application/json' },
         }
       );
     } catch (error: unknown) {
       console.error('Error during user authentication:', error);
       
       // Handle known error types
       if (error && typeof error === 'object' && 'status' in error && error.status === 401) {
         // Unauthorized - Invalid credentials
         const errorMessage = 'message' in error && typeof error.message === 'string' 
           ? error.message 
           : 'Nieprawidłowy email lub hasło';
           
         return new Response(
           JSON.stringify({ error: errorMessage }),
           {
             status: 401,
             headers: { 'Content-Type': 'application/json' },
           }
         );
       }
       
       // Handle general errors
       return new Response(
         JSON.stringify({ error: 'Wystąpił błąd podczas logowania' }),
         {
           status: 500,
           headers: { 'Content-Type': 'application/json' },
         }
       );
     }
   };
   ```

2. Utwórz endpoint wylogowania w `src/pages/api/auth/logout.ts`:
   ```typescript
   import type { APIRoute } from 'astro';
   import { logoutUser } from '../../../lib/services/auth.service';

   // Disable static pre-rendering as this endpoint requires dynamic processing
   export const prerender = false;

   /**
    * POST handler for user logout
    * 
    * This endpoint invalidates the current user session.
    * It requires an Authorization header with a valid Bearer token.
    */
   export const POST: APIRoute = async ({ request, locals }) => {
     try {
       // Check for Authorization header
       const authHeader = request.headers.get('Authorization');
       
       if (!authHeader || !authHeader.startsWith('Bearer ')) {
         return new Response(
           JSON.stringify({ error: 'Wymagane uwierzytelnienie' }),
           {
             status: 401,
             headers: { 'Content-Type': 'application/json' },
           }
         );
       }
       
       // Logout user using auth service
       await logoutUser(locals.supabase);
       
       // Return 204 No Content for successful logout
       return new Response(null, { status: 204 });
     } catch (error: unknown) {
       console.error('Error during user logout:', error);
       
       // Handle unauthorized errors
       if (error && typeof error === 'object' && 'status' in error && error.status === 401) {
         const errorMessage = 'message' in error && typeof error.message === 'string' 
           ? error.message 
           : 'Nieprawidłowy token uwierzytelniający';
           
         return new Response(
           JSON.stringify({ error: errorMessage }),
           {
             status: 401,
             headers: { 'Content-Type': 'application/json' },
           }
         );
       }
       
       // Handle general errors
       return new Response(
         JSON.stringify({ error: 'Wystąpił błąd podczas wylogowywania' }),
         {
           status: 500,
           headers: { 'Content-Type': 'application/json' },
         }
       );
     }
   };
   ```

### Etap 4: Testowanie

1. Utwórz skrypt testowy dla logowania w `test-auth-login.sh`:
   ```bash
   #!/bin/bash
   
   echo "Testowanie endpointu logowania..."
   
   curl -X POST \
     -H "Content-Type: application/json" \
     -d '{"email": "user@example.com", "password": "Password123"}' \
     http://localhost:4321/api/auth/login
   
   echo -e "\n"
   ```

2. Utwórz skrypt testowy dla wylogowania w `test-auth-logout.sh`:
   ```bash
   #!/bin/bash
   
   # Pobierz token z parametru lub przekaż swój token
   TOKEN=${1:-"twój_token_testowy"}
   
   echo "Testowanie endpointu wylogowania..."
   
   curl -X POST \
     -H "Authorization: Bearer $TOKEN" \
     http://localhost:4321/api/auth/logout -v
   
   echo -e "\n"
   ```

3. Nadaj uprawnienia wykonania dla skryptów:
   ```bash
   chmod +x test-auth-login.sh test-auth-logout.sh
   ```

### Etap 5: Dokumentacja

1. Zaktualizuj README.md lub dokumentację API, dodając informacje o nowych endpointach
2. Dodaj przykłady użycia endpointów w dokumentacji
