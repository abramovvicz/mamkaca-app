# API Endpoint Implementation Plan: User Settings

## 1. Przegląd punktu końcowego

Ten endpoint umożliwia pobieranie i aktualizowanie ustawień użytkownika aplikacji. Ustawienia mogą obejmować preferencje dotyczące powiadomień, wydatków na alkohol, poziomu humoru w aplikacji i inne konfiguracje. Endpoint obsługuje zarówno uwierzytelnionych użytkowników (przez token JWT) jak i urządzenia identyfikowane przez X-Device-ID.

## 2. Szczegóły żądania

### GET /api/settings
- Metoda HTTP: GET
- Struktura URL: `/api/settings`
- Nagłówki:
  - Wymagane (jeden z): 
    - `Authorization: Bearer {token}` - token JWT dla uwierzytelnionych użytkowników
    - `X-Device-ID: {device_id}` - identyfikator urządzenia dla uwierzytelniania po urządzeniu
- Request Body: Brak (nie wymagane)

### PATCH /api/settings
- Metoda HTTP: PATCH
- Struktura URL: `/api/settings`
- Nagłówki:
  - Wymagane (jeden z): 
    - `Authorization: Bearer {token}` - token JWT dla uwierzytelnionych użytkowników
    - `X-Device-ID: {device_id}` - identyfikator urządzenia dla uwierzytelniania po urządzeniu
- Request Body: Obiekt JSON zawierający pola do aktualizacji
  ```json
  {
    "notification_time": "08:00:00",
    "notifications_enabled": true,
    "notification_format": "standard",
    "alcohol_expense_amount": 50.00,
    "alcohol_expense_frequency": "weekly",
    "alcohol_expense_currency": "PLN",
    "alcohol_expense_option": "custom",
    "humor_level": "standard",
    "hide_app_icon": false
  }
  ```
  Wszystkie pola są opcjonalne, aktualizowane są tylko podane pola.

## 3. Wykorzystywane typy

### DTOs
- `UserSettingsDto` - reprezentuje ustawienia użytkownika, zawiera wszystkie możliwe pola ustawień
- `UpdateUserSettingsDto` - typ używany do częściowej aktualizacji ustawień użytkownika (Partial)

### Modele komend
- `UpdateUserSettingsCommand` - model komendy używany w serwisie do aktualizacji ustawień

### Schemat walidacji
- `updateUserSettingsSchema` - schemat Zod do walidacji danych wejściowych podczas aktualizacji ustawień

## 4. Szczegóły odpowiedzi

### GET /api/settings
- Sukces: 200 OK
  ```json
  {
    "id": "uuid",
    "notification_time": "09:00:00",
    "notifications_enabled": true,
    "notification_format": "standard",
    "alcohol_expense_amount": 50.00,
    "alcohol_expense_frequency": "weekly",
    "alcohol_expense_currency": "PLN",
    "alcohol_expense_option": "custom",
    "humor_level": "standard",
    "hide_app_icon": false
  }
  ```
- Błędy:
  - 401 Unauthorized - brak uwierzytelnienia
  - 404 Not Found - ustawienia nie znalezione

### PATCH /api/settings
- Sukces: 200 OK - zwraca pełny obiekt ustawień po aktualizacji
  ```json
  {
    "id": "uuid",
    "notification_time": "08:00:00",
    "notifications_enabled": true,
    "notification_format": "standard",
    "alcohol_expense_amount": 50.00,
    "alcohol_expense_frequency": "weekly",
    "alcohol_expense_currency": "PLN",
    "alcohol_expense_option": "custom",
    "humor_level": "high",
    "hide_app_icon": false
  }
  ```
- Błędy:
  - 400 Bad Request - nieprawidłowe dane wejściowe
  - 401 Unauthorized - brak uwierzytelnienia
  - 404 Not Found - ustawienia lub urządzenie nie znalezione

## 5. Przepływ danych

### GET /api/settings
1. Endpoint odbiera żądanie HTTP GET.
2. Sprawdza uwierzytelnienie użytkownika (token JWT) lub identyfikator urządzenia (X-Device-ID).
3. Jeśli użytkownik jest uwierzytelniony:
   - Wywołuje serwis `getUserSettings` z ID użytkownika.
4. Jeśli podano identyfikator urządzenia:
   - Wywołuje serwis `getDeviceUserSettings` z ID urządzenia.
5. Serwis pobiera dane z bazy Supabase, tabela `user_settings`.
6. Dane są przekształcane do formatu `UserSettingsDto`.
7. Odpowiedź jest zwracana klientowi.

### PATCH /api/settings
1. Endpoint odbiera żądanie HTTP PATCH z danymi do aktualizacji.
2. Sprawdza uwierzytelnienie użytkownika (token JWT) lub identyfikator urządzenia (X-Device-ID).
3. Waliduje dane wejściowe za pomocą schematu Zod (`updateUserSettingsSchema`).
4. Jeśli użytkownik jest uwierzytelniony:
   - Wywołuje serwis `updateUserSettings` z ID użytkownika i danymi do aktualizacji.
5. Jeśli podano identyfikator urządzenia:
   - Wywołuje serwis `updateDeviceUserSettings` z ID urządzenia i danymi do aktualizacji.
6. Serwis sprawdza, czy ustawienia dla danego użytkownika już istnieją:
   - Jeśli tak, aktualizuje istniejący rekord.
   - Jeśli nie, tworzy nowy rekord.
7. Zaktualizowane dane są przekształcane do formatu `UserSettingsDto`.
8. Odpowiedź jest zwracana klientowi.

## 6. Względy bezpieczeństwa

1. **Uwierzytelnianie**:
   - Endpoint obsługuje uwierzytelnianie poprzez token JWT.
   - Alternatywnie, używa identyfikatora urządzenia dla urządzeń bez zalogowanego użytkownika.
   - Brak uwierzytelnienia skutkuje błędem 401.

2. **Autoryzacja**:
   - Użytkownicy mają dostęp tylko do swoich własnych ustawień.
   - Urządzenia mają dostęp tylko do ustawień powiązanego użytkownika.
   - Supabase zapewnia Row Level Security (RLS), gdy jest włączone.

3. **Walidacja danych**:
   - Dane wejściowe są walidowane przez schemat Zod.
   - Zweryfikowane typy danych zapewniają integralność danych.
   - Odpowiednio obsługiwane są wartości opcjonalne i null.

## 7. Obsługa błędów

1. **Błędy walidacji (400 Bad Request)**:
   - Nieprawidłowy format danych wejściowych.
   - Niedozwolone wartości pól.

2. **Błędy uwierzytelnienia (401 Unauthorized)**:
   - Brak nagłówka uwierzytelniania.
   - Nieprawidłowy token JWT.
   - Nieprawidłowy identyfikator urządzenia.

3. **Błędy zasobu (404 Not Found)**:
   - Ustawienia użytkownika nie istnieją.
   - Urządzenie nie istnieje lub nie jest powiązane z użytkownikiem.

4. **Błędy serwera (500 Internal Server Error)**:
   - Problemy z bazą danych.
   - Nieoczekiwane wyjątki podczas przetwarzania żądania.

Dla wszystkich błędów zwracana jest ustandaryzowana struktura odpowiedzi zawierająca komunikat błędu i opcjonalnie dodatkowe szczegóły.

## 8. Rozważania dotyczące wydajności

1. **Indeksowanie bazy danych**:
   - Tabela `user_settings` ma kolumnę `user_id` z ograniczeniem UNIQUE, co przyspiesza wyszukiwanie.
   
2. **Lazy Loading**:
   - Ustawienia są pobierane tylko wtedy, gdy są potrzebne.
   - Pobierane są tylko niezbędne pola.

3. **Optymalizacja zapytań**:
   - Przy aktualizacji używane jest sprawdzenie, czy ustawienia istnieją, aby zminimalizować liczbę zapytań.

4. **Keszowanie**:
   - W przyszłych rozwinięciach można rozważyć keszowanie często używanych ustawień.

## 9. Etapy wdrożenia

1. **Analiza specyfikacji API i bazy danych**:
   - Zrozumienie struktury danych ustawień użytkownika.
   - Identyfikacja wymaganych typów i walidacji.

2. **Utworzenie schematu walidacji (settings.schema.ts)**:
   - Implementacja schematu Zod do walidacji danych wejściowych.
   - Definicja reguł walidacji dla każdego pola.

3. **Implementacja serwisu ustawień (settings.service.ts)**:
   - Utworzenie funkcji do pobierania ustawień użytkownika.
   - Implementacja funkcji do aktualizacji ustawień.
   - Obsługa przypadków brzegowych (np. brak istniejących ustawień).

4. **Implementacja endpointu GET /api/settings**:
   - Obsługa uwierzytelniania przez token JWT lub identyfikator urządzenia.
   - Wywołanie odpowiedniej funkcji serwisu w zależności od typu uwierzytelnienia.
   - Odpowiednia obsługa błędów i formatowanie odpowiedzi.

5. **Implementacja endpointu PATCH /api/settings**:
   - Walidacja danych wejściowych za pomocą schematu Zod.
   - Wywołanie odpowiedniej funkcji serwisu w zależności od typu uwierzytelnienia.
   - Obsługa błędów walidacji i innych przypadków brzegowych.

6. **Testowanie**:
   - Testowanie endpointu GET z różnymi scenariuszami uwierzytelnienia.
   - Testowanie endpointu PATCH z różnymi zestawami danych.
   - Weryfikacja obsługi błędów.

7. **Dokumentacja**:
   - Utworzenie planu implementacji dla przyszłych prac nad API.

## 10. Podsumowanie

Implementacja endpointów ustawień użytkownika zapewnia elastyczny i bezpieczny sposób zarządzania preferencjami użytkowników aplikacji. Obsługuje zarówno uwierzytelnionych użytkowników jak i urządzenia bez zalogowanych użytkowników. Wykorzystuje schemat Zod do walidacji danych i odpowiednio obsługuje różne przypadki błędów, zapewniając niezawodną pracę API.
