# API Endpoint Implementation Plan: Statistics Endpoints

## 1. Przegląd punktów końcowych

### 1.1 Statystyki trzeźwości (/api/statistics)

Endpoint umożliwiający użytkownikom pobieranie zagregowanych statystyk trzeźwości w określonych okresach czasu (dzień, tydzień, miesiąc, rok). Statystyki zawierają informacje o liczbie dni trzeźwości oraz procentowym udziale dni trzeźwości w danym okresie.

### 1.2 Szacunkowe oszczędności (/api/statistics/savings)

Endpoint umożliwiający użytkownikom pobieranie szacunkowych oszczędności finansowych wynikających z utrzymywania trzeźwości, z podziałem na oszczędności dzienne, tygodniowe, miesięczne oraz całkowite.

## 2. Szczegóły żądania

### 2.1 GET /api/statistics

- __Metoda HTTP:__ GET

- __Struktura URL:__ `/api/statistics`

- __Nagłówki:__
    - `Authorization: Bearer {token}` LUB `X-Device-ID: {device_id}`

- __Parametry zapytania:__

    - __Opcjonalne:__

        - `period_type`: dzień, tydzień, miesiąc, rok (domyślnie: miesiąc)
        - `start_date`: Data początkowa w formacie YYYY-MM-DD
        - `limit`: Limit wyników (domyślnie: 12)

### 2.2 GET /api/statistics/savings

- __Metoda HTTP:__ GET
- __Struktura URL:__ `/api/statistics/savings`
- __Nagłówki:__
    - `Authorization: Bearer {token}` LUB `X-Device-ID: {device_id}`
- __Parametry:__ Brak

## 3. Wykorzystywane typy

### 3.1 Data Transfer Objects (DTO)

#### StatisticsResponseDTO

```kotlin
data class StatisticsResponseDTO(
    val period_type: String,
    val period_start: String,
    val sober_days_count: Int,
    val sober_days_percentage: BigDecimal
)
```

#### SavingsResponseDTO

```kotlin
data class SavingsResponseDTO(
    val currency: String,
    val daily_saving: BigDecimal,
    val weekly_saving: BigDecimal,
    val monthly_saving: BigDecimal,
    val total_saving: BigDecimal,
    val calculation_method: String
)
```

#### StatisticsQueryDTO

```kotlin
data class StatisticsQueryDTO(
    val period_type: String?,
    val start_date: LocalDate?,
    val limit: Int?
)
```

### 3.2 Modele domeny

#### StatisticsModel

```kotlin
data class StatisticsModel(
    val periodType: PeriodType,
    val periodStart: LocalDate,
    val soberDaysCount: Int,
    val soberDaysPercentage: BigDecimal
)
```

#### SavingsModel

```kotlin
data class SavingsModel(
    val currency: String,
    val dailySaving: BigDecimal,
    val weeklySaving: BigDecimal,
    val monthlySaving: BigDecimal,
    val totalSaving: BigDecimal,
    val calculationMethod: String
)
```

#### PeriodType

```kotlin
enum class PeriodType {
    DAY, WEEK, MONTH, YEAR;
    
    companion object {
        fun fromString(value: String?): PeriodType {
            return when(value?.lowercase()) {
                "day" -> DAY
                "week" -> WEEK
                "year" -> YEAR
                else -> MONTH // Domyślna wartość
            }
        }
    }
}
```

## 4. Szczegóły odpowiedzi

### 4.1 GET /api/statistics

- __Sukces (200 OK):__

  ```json
  [
    {
      "period_type": "month",
      "period_start": "2025-09-01",
      "sober_days_count": 25,
      "sober_days_percentage": 83.33
    },
    {
      "period_type": "month",
      "period_start": "2025-08-01",
      "sober_days_count": 31,
      "sober_days_percentage": 100.00
    }
  ]
  ```

- __Błędne dane wejściowe (400 Bad Request):__

  ```json
  {
    "error": "Invalid query parameters",
    "details": "Invalid period_type. Must be one of: day, week, month, year"
  }
  ```

- __Brak autoryzacji (401 Unauthorized):__

  ```json
  {
    "error": "Unauthorized",
    "details": "Authentication required"
  }
  ```

### 4.2 GET /api/statistics/savings

- __Sukces (200 OK):__

  ```json
  {
    "currency": "PLN",
    "daily_saving": 15.00,
    "weekly_saving": 105.00,
    "monthly_saving": 450.00,
    "total_saving": 1350.00,
    "calculation_method": "custom"
  }
  ```

- __Brak autoryzacji (401 Unauthorized):__

  ```json
  {
    "error": "Unauthorized",
    "details": "Authentication required"
  }
  ```

## 5. Przepływ danych

### 5.1 GET /api/statistics

1. __Kontroler__ odbiera żądanie HTTP i pobiera dane uwierzytelniające użytkownika (token JWT lub identyfikator urządzenia)
2. __Kontroler__ waliduje parametry zapytania i konwertuje je na obiekt DTO
3. __Serwis__ identyfikuje użytkownika na podstawie danych uwierzytelniających
4. __Serwis__ odpytuje repozytorium o dane statystyczne użytkownika
5. __Repozytorium__ wykonuje zapytanie do tabeli `stat_aggregations`
6. __Repozytorium__ mapuje wyniki zapytania na obiekty domeny
7. __Serwis__ filtruje i limituje wyniki
8. __Kontroler__ mapuje obiekty domeny na obiekty DTO
9. __Kontroler__ zwraca odpowiedź HTTP z danymi w formacie JSON

### 5.2 GET /api/statistics/savings

1. __Kontroler__ odbiera żądanie HTTP i pobiera dane uwierzytelniające użytkownika
2. __Serwis__ identyfikuje użytkownika na podstawie danych uwierzytelniających
3. __Serwis__ pobiera ustawienia użytkownika (w tym dzienną kwotę wydatków na alkohol)
4. __Serwis__ pobiera dane o trzeźwości użytkownika
5. __Serwis__ oblicza szacunkowe oszczędności
6. __Kontroler__ mapuje model domeny na DTO
7. __Kontroler__ zwraca odpowiedź HTTP z danymi w formacie JSON

## 6. Względy bezpieczeństwa

### 6.1 Uwierzytelnianie i autoryzacja

- Wymagane uwierzytelnienie za pomocą tokena JWT lub identyfikatora urządzenia
- Kontrola dostępu: użytkownicy mogą przeglądać tylko własne statystyki
- Walidacja tokenów JWT pod kątem ważności i podpisu
- Weryfikacja powiązania identyfikatora urządzenia z użytkownikiem

### 6.2 Walidacja danych wejściowych

- Dokładna walidacja typu okresu (period_type) - musi być jednym z dopuszczalnych wartości
- Walidacja formatu daty początkowej (start_date) - zgodność z formatem YYYY-MM-DD
- Walidacja limitu - wartość dodatnia

### 6.3 Ochrona przed atakami

- Ograniczenie częstotliwości żądań (rate limiting)
- Stosowanie preparowanych zapytań (prepared statements) w dostępie do bazy danych
- Odpowiednie kodowanie danych wyjściowych JSON

## 7. Obsługa błędów

### 7.1 Błędy walidacji

- 400 Bad Request - nieprawidłowy format daty
- 400 Bad Request - nieprawidłowa wartość period_type
- 400 Bad Request - nieprawidłowa wartość limitu

### 7.2 Błędy uwierzytelniania

- 401 Unauthorized - brak tokena JWT lub identyfikatora urządzenia
- 401 Unauthorized - wygasły token JWT
- 401 Unauthorized - nieznany identyfikator urządzenia

### 7.3 Błędy bazy danych

- 500 Internal Server Error - błąd połączenia z bazą danych
- 500 Internal Server Error - błąd wykonania zapytania

### 7.4 Rejestrowanie błędów

- Rejestrowanie błędów uwierzytelniania
- Rejestrowanie błędów walidacji danych wejściowych
- Rejestrowanie błędów bazy danych
- Maskowanie wrażliwych informacji w logach

## 8. Rozważania dotyczące wydajności

### 8.1 Optymalizacja bazy danych

- Wykorzystanie indeksu `idx_stat_aggregations_user_id` dla szybkiego wyszukiwania statystyk użytkownika
- Stosowanie ograniczenia wyników (LIMIT) w zapytaniach do bazy danych
- Możliwe dodanie indeksu dla kolumn `period_type` i `period_start` w tabeli `stat_aggregations`

### 8.2 Buforowanie

- Rozważenie buforowania wyników dla często używanych parametrów
- Wykorzystanie nagłówków HTTP cache-control dla odpowiedzi klienta
- Implementacja mechanizmu unieważniania bufora po dodaniu nowych danych

## 9. Etapy wdrożenia

### 9.1 Implementacja modeli i DTO

1. Utworzenie modeli domeny (StatisticsModel, SavingsModel, PeriodType)
2. Utworzenie DTO (StatisticsResponseDTO, SavingsResponseDTO, StatisticsQueryDTO)
3. Implementacja funkcji mapujących między modelami domeny i DTO

### 9.2 Implementacja repozytorium

1. Utworzenie interfejsu StatisticsRepository
2. Implementacja StatisticsRepositoryImpl
3. Dodanie metod do pobierania statystyk użytkownika
4. Dodanie metod pomocniczych do obliczania oszczędności

### 9.3 Implementacja serwisu

1. Utworzenie interfejsu StatisticsService
2. Implementacja StatisticsServiceImpl
3. Implementacja metody pobierania statystyk z filtrowaniem i limitowaniem
4. Implementacja metody obliczania oszczędności

### 9.4 Implementacja kontrolera

1. Utworzenie StatisticsController
2. Implementacja endpointu GET /api/statistics
3. Implementacja endpointu GET /api/statistics/savings
4. Dodanie obsługi wyjątków

### 9.5 Testowanie

1. Utworzenie testów jednostkowych dla modeli i mapperów
2. Utworzenie testów jednostkowych dla serwisu
3. Utworzenie testów integracyjnych dla repozytorium
4. Utworzenie testów end-to-end dla API

### 9.6 Dokumentacja

1. Dokumentacja API (Swagger/OpenAPI)
2. Dokumentacja kodu
3. Dodanie przykładowych wywołań API
