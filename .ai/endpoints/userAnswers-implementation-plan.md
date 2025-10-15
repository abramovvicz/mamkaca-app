# API Endpoint Implementation Plan: Daily Answers

## 1. Przegląd punktu końcowego

Te endpointy umożliwiają zarządzanie odpowiedziami użytkowników na codzienne pytanie "Masz dziś kaca?". Aplikacja codziennie pyta użytkowników o ich stan i te odpowiedzi są rejestrowane w systemie. Endpointy pozwalają na:
1. Przesyłanie nowych odpowiedzi (POST /api/answers)
2. Pobieranie historii odpowiedzi użytkownika (GET /api/answers)

Odpowiedzi mogą być powiązane zarówno z uwierzytelnionym użytkownikiem (przez token JWT) jak i z konkretnym urządzeniem (przez X-Device-ID), co umożliwia gromadzenie danych nawet bez zalogowania.

## 2. Szczegóły żądania

### POST /api/answers
- Metoda HTTP: POST
- Struktura URL: `/api/answers`
- Nagłówki:
  - Wymagane (jeden z): 
    - `Authorization: Bearer {token}` - token JWT dla uwierzytelnionych użytkowników
    - `X-Device-ID: {device_id}` - identyfikator urządzenia dla uwierzytelniania po urządzeniu
- Request Body: Obiekt JSON zawierający informacje o odpowiedzi
  ```json
  {
    "answer_date": "2025-10-12",
    "answer_time": "09:15:00",
    "answer_type": "yes",
    "answer_source": "notification",
    "note": "Optional note about the answer"
  }
  ```
  - Wymagane pola: `answer_date`, `answer_time`, `answer_type`, `answer_source`
  - Opcjonalne pola: `note`

### GET /api/answers
- Metoda HTTP: GET
- Struktura URL: `/api/answers`
- Nagłówki:
  - Wymagane (jeden z): 
    - `Authorization: Bearer {token}` - token JWT dla uwierzytelnionych użytkowników
    - `X-Device-ID: {device_id}` - identyfikator urządzenia dla uwierzytelniania po urządzeniu
- Query Parameters:
  - `start_date` (opcjonalny): Data początkowa filtrowania (format: YYYY-MM-DD)
  - `end_date` (opcjonalny): Data końcowa filtrowania (format: YYYY-MM-DD)
  - `answer_type` (opcjonalny): Filtrowanie po typie odpowiedzi (yes/no/none)
  - `limit` (opcjonalny): Limit wyników (domyślnie: 30)
  - `offset` (opcjonalny): Przesunięcie dla paginacji

## 3. Wykorzystywane typy

### DTOs
- `SubmitAnswerDto` - reprezentuje dane wejściowe dla tworzenia nowej odpowiedzi
- `AnswerDto` - reprezentuje pojedynczą odpowiedź w odpowiedzi API
- `GetAnswersQueryDto` - reprezentuje parametry zapytania dla pobierania odpowiedzi
- `PaginatedAnswersResponseDto` - reprezentuje odpowiedź paginowaną dla listy odpowiedzi

### Modele komend
- `SubmitAnswerCommand` - model komendy używany w serwisie do zapisywania nowej odpowiedzi

### Schemat walidacji
- `submitAnswerSchema` - schemat Zod do walidacji danych wejściowych podczas tworzenia odpowiedzi
- `getAnswersQuerySchema` - schemat Zod do walidacji parametrów zapytania GET

## 4. Szczegóły odpowiedzi

### POST /api/answers
- Sukces: 201 Created
  ```json
  {
    "id": "uuid",
    "answer_date": "2025-10-12",
    "answer_time": "09:15:00",
    "answer_type": "yes",
    "answer_source": "notification",
    "note": "Optional note about the answer",
    "created_at": "2025-10-12T09:15:03Z"
  }
  ```
- Błędy:
  - 400 Bad Request - nieprawidłowe dane wejściowe
  - 401 Unauthorized - brak uwierzytelnienia
  - 409 Conflict - odpowiedź na dany dzień już istnieje

### GET /api/answers
- Sukces: 200 OK
  ```json
  {
    "total": 60,
    "items": [
      {
        "id": "uuid",
        "answer_date": "2025-10-12",
        "answer_time": "09:15:00",
        "answer_type": "no",
        "answer_source": "notification",
        "note": null,
        "created_at": "2025-10-12T09:15:03Z"
      }
    ]
  }
  ```
- Błędy:
  - 400 Bad Request - nieprawidłowe parametry zapytania
  - 401 Unauthorized - brak uwierzytelnienia

## 5. Przepływ danych

### POST /api/answers
1. Endpoint odbiera żądanie HTTP POST z danymi odpowiedzi.
2. Sprawdza uwierzytelnienie użytkownika (token JWT) lub identyfikator urządzenia (X-Device-ID).
3. Waliduje dane wejściowe za pomocą schematu Zod (`submitAnswerSchema`).
4. Sprawdza, czy istnieje już odpowiedź na podany dzień dla danego użytkownika/urządzenia.
   - Jeśli tak, zwraca błąd 409 Conflict.
5. Tworzy obiekt `SubmitAnswerCommand` z danymi odpowiedzi i identyfikatorami użytkownika/urządzenia.
6. Wywołuje serwis `submitAnswer` z utworzoną komendą.
7. Serwis zapisuje odpowiedź w bazie danych Supabase, tabela `answers`.
8. Zapisana odpowiedź jest przekształcana do formatu `AnswerDto`.
9. Odpowiedź jest zwracana klientowi ze statusem 201 Created.

### GET /api/answers
1. Endpoint odbiera żądanie HTTP GET.
2. Sprawdza uwierzytelnienie użytkownika (token JWT) lub identyfikator urządzenia (X-Device-ID).
3. Waliduje parametry zapytania za pomocą schematu Zod (`getAnswersQuerySchema`).
4. Konstruuje zapytanie do bazy danych z odpowiednimi filtrami:
   - Filtruje po user_id lub device_id w zależności od typu uwierzytelnienia.
   - Dodaje filtry start_date, end_date i answer_type, jeśli są podane.
   - Stosuje limit i offset dla paginacji.
5. Wykonuje dwa zapytania do bazy danych:
   - Jedno do pobrania odpowiedzi z paginacją.
   - Drugie do obliczenia całkowitej liczby odpowiedzi (dla paginacji).
6. Dane są przekształcane do formatu `PaginatedAnswersResponseDto`.
7. Odpowiedź jest zwracana klientowi ze statusem 200 OK.

## 6. Względy bezpieczeństwa

1. **Uwierzytelnianie**:
   - Endpoint obsługuje uwierzytelnianie poprzez token JWT.
   - Alternatywnie, używa identyfikatora urządzenia dla urządzeń bez zalogowanego użytkownika.
   - Brak uwierzytelnienia skutkuje błędem 401.

2. **Autoryzacja**:
   - Użytkownicy mają dostęp tylko do swoich własnych odpowiedzi.
   - Urządzenia mają dostęp tylko do odpowiedzi powiązanego użytkownika lub własnych odpowiedzi.
   - Supabase zapewnia Row Level Security (RLS) dla tabeli `answers`.

3. **Walidacja danych**:
   - Dane wejściowe są walidowane przez schemat Zod.
   - Sprawdzana jest poprawność formatów dat i czasu.
   - Weryfikowane są wartości enum dla answer_type i answer_source.
   - Odpowiednio obsługiwane są wartości opcjonalne i null.

4. **Ochrona przed duplikatami**:
   - System zapobiega dodawaniu więcej niż jednej odpowiedzi dziennie dla danego użytkownika/urządzenia.
   - Zastosowanie ograniczenia unique w bazie danych i dodatkowe sprawdzanie w kodzie.

## 7. Obsługa błędów

1. **Błędy walidacji (400 Bad Request)**:
   - Nieprawidłowy format daty lub czasu.
   - Niedozwolone wartości dla answer_type lub answer_source.
   - Nieprawidłowe parametry zapytania w endpoincie GET.

2. **Błędy uwierzytelnienia (401 Unauthorized)**:
   - Brak nagłówka uwierzytelniania.
   - Nieprawidłowy token JWT.
   - Nieprawidłowy identyfikator urządzenia.

3. **Konflikt (409 Conflict)**:
   - Próba dodania więcej niż jednej odpowiedzi na ten sam dzień.

4. **Błędy serwera (500 Internal Server Error)**:
   - Problemy z bazą danych.
   - Nieoczekiwane wyjątki podczas przetwarzania żądania.

Dla wszystkich błędów zwracana jest ustandaryzowana struktura odpowiedzi zawierająca komunikat błędu i opcjonalnie dodatkowe szczegóły.

## 8. Rozważania dotyczące wydajności

1. **Indeksowanie bazy danych**:
   - Tabela `answers` ma indeksy na kolumnach `user_id` i `device_id`, co przyspiesza wyszukiwanie.
   - Indeks na `answer_date` dla szybkiego filtrowania według dat.
   - Indeks na `answer_type` dla filtrowania według typu odpowiedzi.
   
2. **Paginacja**:
   - Implementacja paginacji w endpoincie GET ogranicza rozmiar odpowiedzi.
   - Domyślny limit 30 elementów zapobiega przeciążeniu.
   - Parametry limit i offset umożliwiają klientowi kontrolę nad paginacją.

3. **Optymalizacja zapytań**:
   - Pobieranie tylko niezbędnych pól z bazy danych.
   - Używanie count() z warunkami WHERE zamiast filtrowania po stronie aplikacji.
   - Optymalizacja filtrowania przez budowanie zapytań z warunkami tylko dla podanych parametrów.

4. **Keszowanie**:
   - W przyszłych wersjach można rozważyć keszowanie historycznych odpowiedzi.
   - Strategie invalidacji keszu dla nowych odpowiedzi.

## 9. Etapy wdrożenia

1. **Analiza specyfikacji API i bazy danych**:
   - Zrozumienie struktury danych odpowiedzi użytkownika.
   - Identyfikacja wymaganych typów i walidacji.

2. **Utworzenie schematu walidacji (answers.schema.ts)**:
   - Implementacja schematu Zod dla walidacji danych wejściowych POST.
   - Implementacja schematu Zod dla parametrów zapytania GET.
   - Definicja reguł walidacji dla każdego pola.

3. **Implementacja serwisu odpowiedzi (answers.service.ts)**:
   - Utworzenie funkcji submitAnswer do zapisywania odpowiedzi.
   - Implementacja funkcji getAnswers do pobierania odpowiedzi z paginacją.
   - Implementacja logiki sprawdzania duplikatów.
   - Mapowanie danych między DTO a modelami bazy danych.

4. **Implementacja endpointu POST /api/answers**:
   - Obsługa uwierzytelniania przez token JWT lub identyfikator urządzenia.
   - Walidacja danych wejściowych za pomocą schematu Zod.
   - Wywołanie serwisu submitAnswer.
   - Obsługa błędów i formatowanie odpowiedzi.

5. **Implementacja endpointu GET /api/answers**:
   - Obsługa uwierzytelniania przez token JWT lub identyfikator urządzenia.
   - Walidacja parametrów zapytania.
   - Wywołanie serwisu getAnswers z odpowiednimi parametrami.
   - Implementacja paginacji i formatowanie odpowiedzi.

6. **Testowanie**:
   - Testowanie endpointu POST z różnymi zestawami danych.
   - Testowanie endpointu GET z różnymi parametrami filtrowania i paginacji.
   - Weryfikacja obsługi błędów i przypadków brzegowych.
   - Sprawdzenie zgodności z regułami biznesowymi (np. jedna odpowiedź dziennie).

7. **Dokumentacja**:
   - Aktualizacja dokumentacji API.
   - Utworzenie przykładowych żądań i odpowiedzi dla klientów.

## 10. Podsumowanie

Implementacja endpointów odpowiedzi użytkownika zapewnia solidny fundament dla kluczowej funkcjonalności aplikacji - śledzenia trzeźwości poprzez codzienne odpowiedzi. Endpointy są zaprojektowane z uwzględnieniem bezpieczeństwa, wydajności i łatwości użycia. Obsługują zarówno uwierzytelnionych użytkowników, jak i urządzenia bez zalogowanego użytkownika, co zwiększa elastyczność aplikacji. Kompletna walidacja danych i odpowiednia obsługa błędów zapewniają niezawodne działanie API.
