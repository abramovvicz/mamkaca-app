# API Endpoint Implementation Plan: Device Management

## 1. Przegląd punktu końcowego

Punkty końcowe API zarządzania urządzeniami umożliwiają:
1. Rejestrację nowych urządzeń w systemie
2. Powiązanie anonimowych urządzeń z kontami użytkowników
3. Pobieranie listy urządzeń powiązanych z zalogowanym użytkownikiem

Te endpointy są kluczowe dla obsługi wielu urządzeń, synchronizacji danych i zapewnienia spójnego doświadczenia użytkownika na różnych platformach.

## 2. Szczegóły żądania

### Endpoint 1: Register Device

- Metoda HTTP: POST
- Struktura URL: `/api/devices`
- Request Body:
  ```json
  {
    "device_identifier": "unique-device-id",
    "device_name": "iPhone 13", 
    "device_type": "ios"
  }
  ```
- Kody odpowiedzi:
  - 201 Created - Urządzenie zarejestrowane pomyślnie
  - 400 Bad Request - Nieprawidłowe dane urządzenia
  - 409 Conflict - Urządzenie jest już zarejestrowane

### Endpoint 2: Link Device to User

- Metoda HTTP: POST
- Struktura URL: `/api/devices/link`
- Nagłówki: `Authorization: Bearer {token}`
- Request Body:
  ```json
  {
    "device_id": "device-uuid"
  }
  ```
- Kody odpowiedzi:
  - 204 No Content - Urządzenie zostało pomyślnie powiązane
  - 400 Bad Request - Nieprawidłowy identyfikator urządzenia
  - 401 Unauthorized - Brak autentykacji
  - 404 Not Found - Urządzenie nie zostało znalezione

### Endpoint 3: Get User Devices

- Metoda HTTP: GET
- Struktura URL: `/api/devices`
- Nagłówki: `Authorization: Bearer {token}`
- Kody odpowiedzi:
  - 200 OK - Pomyślnie pobrano urządzenia
  - 401 Unauthorized - Brak autentykacji

## 3. Wykorzystywane typy

Wymagane typy DTO i modele poleceń są już zdefiniowane w `src/types.ts`:

```typescript
// Request DTOs
export interface RegisterDeviceDto {
  device_identifier: string;
  device_name?: string | null;
  device_type?: string | null;
}

export interface LinkDeviceDto {
  device_id: string;
}

// Response DTOs
export interface DeviceDto {
  id: string;
  device_identifier: string;
  device_name: string | null;
  device_type: string | null;
  last_sync_at: string;
  created_at?: string;
}

// Command Models
export interface RegisterDeviceCommand {
  device_identifier: string;
  device_name?: string | null;
  device_type?: string | null;
}

export interface LinkDeviceToUserCommand {
  device_id: string;
  user_id: string;
}
```

## 4. Szczegóły odpowiedzi

### Endpoint 1: Register Device

Sukces (201 Created):
```json
{
  "id": "uuid",
  "device_identifier": "unique-device-id",
  "device_name": "iPhone 13",
  "device_type": "ios",
  "created_at": "2025-10-12T09:45:03Z"
}
```

### Endpoint 2: Link Device to User

Sukces (204 No Content): Brak treści odpowiedzi

### Endpoint 3: Get User Devices

Sukces (200 OK):
```json
[
  {
    "id": "uuid",
    "device_identifier": "unique-device-id",
    "device_name": "iPhone 13",
    "device_type": "ios",
    "last_sync_at": "2025-10-12T09:45:03Z"
  }
]
```

## 5. Przepływ danych

### Register Device
1. Klient wysyła dane urządzenia (identyfikator, nazwa, typ) do endpointu `/api/devices`
2. Endpoint sprawdza, czy urządzenie o podanym identyfikatorze już istnieje
3. Jeśli nie istnieje, tworzy nowy rekord w tabeli `devices` z podanymi danymi
4. Zwraca dane nowo utworzonego urządzenia

### Link Device to User
1. Klient wysyła ID urządzenia do endpointu `/api/devices/link` z tokenem JWT w nagłówku
2. Endpoint weryfikuje token, aby ustalić ID bieżącego użytkownika
3. Sprawdza, czy urządzenie o podanym ID istnieje
4. Aktualizuje rekord urządzenia, ustawiając pole `user_id` na ID uwierzytelnionego użytkownika
5. Zwraca odpowiedź 204 No Content

### Get User Devices
1. Klient wysyła żądanie GET do endpointu `/api/devices` z tokenem JWT w nagłówku
2. Endpoint weryfikuje token, aby ustalić ID bieżącego użytkownika
3. Pobiera wszystkie rekordy urządzeń z tabeli `devices`, gdzie `user_id` jest równy ID bieżącego użytkownika
4. Zwraca listę urządzeń w odpowiedzi

## 6. Względy bezpieczeństwa

### Uwierzytelnianie
- Endpointy `/api/devices/link` i `/api/devices` (GET) wymagają tokenu uwierzytelniania w nagłówku
- Token JWT będzie weryfikowany w celu ustalenia ID użytkownika
- Dostęp do urządzeń jest dozwolony tylko dla zalogowanych użytkowników

### Autoryzacja
- Użytkownicy mogą uzyskać dostęp tylko do swoich własnych urządzeń
- Mechanizm sprawdzania, czy urządzenie należy do użytkownika, zapobiega nieautoryzowanemu dostępowi

### Walidacja danych
- Wszystkie dane wejściowe będą walidowane za pomocą schematów Zod
- Sprawdzanie unikalności identyfikatorów urządzeń zapobiega duplikacji

## 7. Obsługa błędów

### Register Device
- 400 Bad Request - Nieprawidłowe dane urządzenia (np. brak wymaganego pola)
- 409 Conflict - Urządzenie o podanym identyfikatorze jest już zarejestrowane
- 500 Internal Server Error - Nieoczekiwane błędy serwera

### Link Device to User
- 400 Bad Request - Nieprawidłowy identyfikator urządzenia
- 401 Unauthorized - Brak tokenu uwierzytelniania lub nieprawidłowy token
- 404 Not Found - Urządzenie o podanym ID nie istnieje
- 500 Internal Server Error - Nieoczekiwane błędy serwera

### Get User Devices
- 401 Unauthorized - Brak tokenu uwierzytelniania lub nieprawidłowy token
- 500 Internal Server Error - Nieoczekiwane błędy serwera

## 8. Rozważania dotyczące wydajności

- Indeksy bazy danych: Zapewnić, że pole `device_identifier` w tabeli `devices` ma indeks UNIQUE, a pole `user_id` ma indeks do szybkiego wyszukiwania urządzeń użytkownika
- Odpowiedzi paginacyjne: W przyszłości, gdy użytkownicy mogą mieć wiele urządzeń, warto rozważyć dodanie paginacji do endpointu `/api/devices`

## 9. Etapy wdrożenia

### Utworzenie serwisu zarządzania urządzeniami

1. Utworzyć plik `src/lib/schemas/device.schema.ts`:
   - Zdefiniować schemat walidacji `registerDeviceSchema` dla rejestracji urządzenia
   - Zdefiniować schemat walidacji `linkDeviceSchema` dla powiązania urządzenia

2. Utworzyć plik `src/lib/services/device.service.ts`:
   - Implementować funkcję `registerDevice` do tworzenia nowego urządzenia
   - Implementować funkcję `linkDeviceToUser` do powiązania urządzenia z użytkownikiem
   - Implementować funkcję `getUserDevices` do pobierania urządzeń użytkownika

### Implementacja endpointów

3. Utworzyć plik `src/pages/api/devices/index.ts`:
   - Implementować handler POST dla rejestracji urządzenia
   - Implementować handler GET dla pobierania urządzeń użytkownika
   - Dodać walidację wejścia
   - Dodać obsługę błędów

4. Utworzyć plik `src/pages/api/devices/link.ts`:
   - Implementować handler POST dla powiązania urządzenia z użytkownikiem
   - Dodać walidację wejścia
   - Dodać obsługę błędów

### Testowanie

5. Utworzyć skrypty testowe dla testowania endpointów:
   - `test-device-register.sh` dla testowania rejestracji urządzenia
   - `test-device-link.sh` dla testowania powiązania urządzenia
   - `test-device-list.sh` dla testowania pobierania urządzeń
