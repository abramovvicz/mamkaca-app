# API Endpoint Implementation Plan: Sobriety Streaks

## 1. Przegląd punktu końcowego

Plan implementacji trzech powiązanych ze sobą endpointów REST API obsługujących zarządzanie okresami trzeźwości użytkownika (streaks):

1. **GET /api/streaks/current** - Pobieranie informacji o aktualnym okresie trzeźwości użytkownika
2. **GET /api/streaks/longest** - Pobieranie informacji o najdłuższym okresie trzeźwości użytkownika
3. **POST /api/resets** - Tworzenie rekordu resetującego okres trzeźwości

Endpointy te wspólnie tworzą funkcjonalność śledzenia okresów trzeźwości, umożliwiając użytkownikom monitorowanie ich postępów oraz rejestrowanie przypadków przerwania abstynencji w sposób niewywołujący poczucia wstydu ("Reset bez wstydu").

## 2. Szczegóły żądania

### GET /api/streaks/current
- **Metoda HTTP**: GET
- **Struktura URL**: `/api/streaks/current`
- **Nagłówki**:
  - Wymagane: Jeden z:
    - `Authorization: Bearer {token}` - Token JWT użytkownika
    - `X-Device-ID: {device_id}` - ID urządzenia niezalogowanego
- **Parametry URL**: Brak

### GET /api/streaks/longest
- **Metoda HTTP**: GET
- **Struktura URL**: `/api/streaks/longest`
- **Nagłówki**:
  - Wymagane: Jeden z:
    - `Authorization: Bearer {token}` - Token JWT użytkownika
    - `X-Device-ID: {device_id}` - ID urządzenia niezalogowanego
- **Parametry URL**: Brak

### POST /api/resets
- **Metoda HTTP**: POST
- **Struktura URL**: `/api/resets`
- **Nagłówki**:
  - Wymagane: Jeden z:
    - `Authorization: Bearer {token}` - Token JWT użytkownika
    - `X-Device-ID: {device_id}` - ID urządzenia niezalogowanego
- **Request Body**:
  ```json
  {
    "reset_date": "2025-10-12",
    "note": "Optional note about reset"
  }
  ```

## 3. Wykorzystywane typy

Implementacja będzie korzystać z istniejących typów zdefiniowanych w `src/types.ts`:

```typescript
// DTOs dla obsługi okresów trzeźwości
export interface CurrentStreakDto {
  days_sober: number;
  since_date: string;
}

export interface LongestStreakDto {
  longest_streak_days: number;
  start_date: string;
  end_date: string;
}

// Request DTO dla resetowania okresu trzeźwości
export interface ResetStreakDto {
  reset_date: string;
  note?: string | null;
}

// Response DTO dla resetowania okresu trzeźwości
export interface ResetDto {
  id: string;
  reset_date: string;
  note: string | null;
  created_at: string;
}

// Command Model dla resetowania okresu trzeźwości
export interface ResetStreakCommand {
  reset_date: string;
  note?: string | null;
  user_id: string;
}
```

Dodatkowo, należy zaimplementować schematy walidacji Zod dla tych typów w nowym pliku `src/lib/schemas/streak.schema.ts`.

## 4. Szczegóły odpowiedzi

### GET /api/streaks/current
- **Sukces (200 OK)**:
  ```json
  {
    "days_sober": 30,
    "since_date": "2025-09-12"
  }
  ```
- **Błędy**:
  - 401 Unauthorized - Brak autoryzacji lub nieprawidłowe dane uwierzytelniające
  - 500 Internal Server Error - Błąd wewnętrzny serwera

### GET /api/streaks/longest
- **Sukces (200 OK)**:
  ```json
  {
    "longest_streak_days": 45,
    "start_date": "2025-05-01",
    "end_date": "2025-06-15"
  }
  ```
- **Błędy**:
  - 401 Unauthorized - Brak autoryzacji lub nieprawidłowe dane uwierzytelniające
  - 500 Internal Server Error - Błąd wewnętrzny serwera

### POST /api/resets
- **Sukces (201 Created)**:
  ```json
  {
    "id": "uuid",
    "reset_date": "2025-10-12",
    "note": "Optional note about reset",
    "created_at": "2025-10-12T09:45:03Z"
  }
  ```
- **Błędy**:
  - 400 Bad Request - Nieprawidłowe dane resetowania (np. nieprawidłowy format daty)
  - 401 Unauthorized - Brak autoryzacji lub nieprawidłowe dane uwierzytelniające
  - 500 Internal Server Error - Błąd wewnętrzny serwera

## 5. Przepływ danych

### GET /api/streaks/current
1. Endpoint odbiera żądanie z nagłówkiem uwierzytelniającym (JWT lub X-Device-ID)
2. Uwierzytelnianie użytkownika/urządzenia przez middleware Supabase
3. Pobranie historii resetów i odpowiedzi z tabeli `resets` i `answers` dla danego użytkownika/urządzenia
4. Obliczenie aktualnego okresu trzeźwości na podstawie ostatniego resetu lub odpowiedzi "no"
5. Zwrócenie struktury CurrentStreakDto w odpowiedzi

### GET /api/streaks/longest
1. Endpoint odbiera żądanie z nagłówkiem uwierzytelniającym (JWT lub X-Device-ID)
2. Uwierzytelnianie użytkownika/urządzenia przez middleware Supabase
3. Pobranie historii resetów i odpowiedzi z tabeli `resets` i `answers` dla danego użytkownika/urządzenia
4. Analiza historycznych danych i obliczenie najdłuższego okresu trzeźwości
5. Zwrócenie struktury LongestStreakDto w odpowiedzi

### POST /api/resets
1. Endpoint odbiera żądanie z nagłówkiem uwierzytelniającym (JWT lub X-Device-ID) i danymi resetowania
2. Walidacja danych wejściowych przez schemat Zod
3. Uwierzytelnianie użytkownika/urządzenia przez middleware Supabase
4. Zapisanie rekordu resetowania w tabeli `resets`
5. Zwrócenie struktury ResetDto w odpowiedzi

## 6. Względy bezpieczeństwa

1. **Uwierzytelnianie** - Każdy endpoint będzie wymagał autoryzacji poprzez token JWT lub identyfikator urządzenia, zabezpieczając dane przed nieautoryzowanym dostępem.

2. **Walidacja danych wejściowych** - Wszystkie dane wejściowe będą walidowane za pomocą schematów Zod, zapobiegając wstrzyknięciom i innym atakom związanym z nieprawidłowymi danymi.

3. **Uprawnienia dostępu** - Użytkownicy będą mieli dostęp tylko do swoich własnych danych okresów trzeźwości i resetów.

4. **Bezpieczne zapytania do bazy danych** - Zapytania będą parametryzowane za pomocą API Supabase, zapobiegając atakom SQL injection.

5. **Obsługa błędów** - Błędy będą logowane na serwerze, ale informacje o błędach zwracane klientom będą ograniczone, aby nie ujawniać potencjalnie wrażliwych informacji o systemie.

## 7. Obsługa błędów

1. **Błędy walidacji** (400 Bad Request):
   - Nieprawidłowy format daty
   - Brakujące wymagane pola
   - Nieprawidłowy format danych

2. **Błędy autoryzacji** (401 Unauthorized):
   - Brak tokenu JWT lub identyfikatora urządzenia
   - Nieprawidłowy token JWT
   - Wygasły token JWT

3. **Błędy wewnętrzne** (500 Internal Server Error):
   - Problemy z połączeniem do bazy danych
   - Nieoczekiwane wyjątki podczas przetwarzania danych

Każdy błąd będzie zawierał jednolity format odpowiedzi:
```json
{
  "error": "Error Type",
  "message": "Human-readable error message",
  "details": [optional detailed error information]
}
```

## 8. Rozważania dotyczące wydajności

1. **Indeksowanie bazy danych** - Zapewnienie odpowiednich indeksów na tabelach `answers` i `resets` dla szybkiego filtrowania według user_id, device_id i daty.

2. **Cachowanie** - Rozważenie cachowania wyników zapytań dla często używanych endpointów, szczególnie dla danych, które nie zmieniają się często (jak najdłuższy okres).

3. **Optymalizacja zapytań** - Zapytania do bazy danych będą zoptymalizowane, aby pobierać tylko niezbędne kolumny i ograniczać liczbę rekordów.

4. **Przetwarzanie asynchroniczne** - Wykorzystanie asynchronicznego przetwarzania do obsługi równoległych zapytań i zwiększenia przepustowości serwera.

## 9. Etapy wdrożenia

### 1. Stworzenie schematu walidacji (`streak.schema.ts`)
1. Zaimplementowanie schematu ResetStreakSchema do walidacji danych wejściowych dla endpointu POST /api/resets

### 2. Implementacja serwisu streaks (`streak.service.ts`)
1. Implementacja funkcji getCurrentStreak
2. Implementacja funkcji getLongestStreak
3. Implementacja funkcji resetStreak

### 3. Implementacja endpointów API
1. Implementacja endpointu GET /api/streaks/current
2. Implementacja endpointu GET /api/streaks/longest
3. Implementacja endpointu POST /api/resets

### 4. Testowanie
1. Testy jednostkowe dla logiki biznesowej w serwisie
2. Testy integracyjne dla endpointów API
3. Testy wydajnościowe dla krytycznych ścieżek

### 5. Dokumentacja
1. Aktualizacja dokumentacji API
2. Dokumentacja wewnętrzna kodu

### 6. Wdrożenie
1. Przegląd kodu
2. Wdrożenie na środowisko testowe
3. Wdrożenie na produkcję
