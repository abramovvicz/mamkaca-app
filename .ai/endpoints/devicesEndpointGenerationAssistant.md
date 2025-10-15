Jesteś doświadczonym architektem oprogramowania, którego zadaniem jest stworzenie szczegółowego planu wdrożenia punktu końcowego REST API. Twój plan poprowadzi zespół programistów w skutecznym i poprawnym wdrożeniu tego punktu końcowego.

Zanim zaczniemy, zapoznaj się z poniższymi informacjami:
### Device Management

#### Register Device
- Method: POST
- Path: `/api/devices`
- Description: Register a new device
- Request Body:
  ```json
  {
    "device_identifier": "unique-device-id",
    "device_name": "iPhone 13",
    "device_type": "ios"
  }
  ```
- Response:
  ```json
  {
    "id": "uuid",
    "device_identifier": "unique-device-id",
    "device_name": "iPhone 13",
    "device_type": "ios",
    "created_at": "2025-10-12T09:45:03Z"
  }
  ```
- Success: 201 Created
- Errors:
    - 400 Bad Request - Invalid device data
    - 409 Conflict - Device already registered

#### Link Device to User
- Method: POST
- Path: `/api/devices/link`
- Description: Link an anonymous device to a user account
- Request Headers: Authorization: Bearer {token}
- Request Body:
  ```json
  {
    "device_id": "device-uuid"
  }
  ```
- Response: 204 No Content
- Errors:
    - 400 Bad Request - Invalid device ID
    - 401 Unauthorized - Not authenticated
    - 404 Not Found - Device not found

#### Get User Devices
- Method: GET
- Path: `/api/devices`
- Description: Get all devices linked to the authenticated user
- Request Headers: Authorization: Bearer {token}
- Response:
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
- Success: 200 OK
- Errors:
    - 401 Unauthorized - Not authenticated
</route_api_specification>

2. Related database resources:
<related_db_resources>
#### `devices` - Urządzenia użytkowników
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator urządzenia |
| user_id | uuid | REFERENCES users(id) | Identyfikator użytkownika (opcjonalny dla niezalogowanych) |
| device_identifier | text | NOT NULL, UNIQUE | Unikalny identyfikator urządzenia |
| device_name | text | | Nazwa urządzenia |
| device_type | text | | Typ urządzenia |
| last_sync_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej synchronizacji |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |


## 2. Relacje między tabelami

### Relacje jeden-do-wielu (1:N)

- `users` 1:N `devices` - Użytkownik może mieć wiele urządzeń
- `users` 1:N `user_settings` - Użytkownik ma jedno ustawienie (relacja 1:1 przez ograniczenie UNIQUE)
- `users` 1:N `answers` - Użytkownik może mieć wiele odpowiedzi
- `devices` 1:N `answers` - Urządzenie może mieć wiele odpowiedzi
- `users` 1:N `resets` - Użytkownik może mieć wiele resetów
- `users` 1:N `stat_aggregations` - Użytkownik może mieć wiele agregacji statystyk
- `users` 1:N `user_achievements` - Użytkownik może mieć wiele osiągnięć
- `milestones` 1:N `user_achievements` - Kamień milowy może być osiągnięty przez wielu użytkowników
- `users` 1:N `displayed_content` - Użytkownik może mieć wiele wyświetlonych treści
- `devices` 1:N `displayed_content` - Urządzenie może mieć wiele wyświetlonych treści
- `educational_content` 1:N `displayed_content` - Treść może być wyświetlona wielu użytkownikom/urządzeniom
- `users` 1:N `data_exports` - Użytkownik może mieć wiele eksportów danych
- `users` 1:N `sync_events` - Użytkownik może mieć wiele wydarzeń synchronizacji
- `devices` 1:N `sync_events` - Urządzenie może mieć wiele wydarzeń synchronizacji
- `sync_events` 1:N `sync_conflicts` - Wydarzenie synchronizacji może zawierać wiele konfliktów
- `surveys` 1:N `survey_questions` - Ankieta może zawierać wiele pytań
- `survey_questions` 1:N `survey_responses` - Pytanie może mieć wiele odpowiedzi
- `users` 1:N `survey_responses` - Użytkownik może udzielić wielu odpowiedzi
- `devices` 1:N `survey_responses` - Urządzenie może być źródłem wielu odpowiedzi
- `users` 1:N `survey_displays` - Użytkownik może mieć wiele wyświetleń ankiet
- `devices` 1:N `survey_displays` - Urządzenie może mieć wiele wyświetleń ankiet
- `surveys` 1:N `survey_displays` - Ankieta może być wyświetlona wielu użytkownikom/urządzeniom

## 3. Indeksy

### Indeksy dla tabeli `answers`
- `idx_answers_user_id` na `user_id` - Przyspiesza wyszukiwanie odpowiedzi dla konkretnego użytkownika
- `idx_answers_device_id` na `device_id` - Przyspiesza wyszukiwanie odpowiedzi dla konkretnego urządzenia
- `idx_answers_date` na `answer_date` - Przyspiesza wyszukiwanie odpowiedzi według daty
- `idx_answers_type` na `answer_type` - Przyspiesza filtrowanie według typu odpowiedzi

### Indeksy dla tabeli `user_achievements`
- `idx_user_achievements_user_id` na `user_id` - Przyspiesza wyszukiwanie osiągnięć dla konkretnego użytkownika
- `idx_user_achievements_milestone_id` na `milestone_id` - Przyspiesza wyszukiwanie użytkowników z konkretnym osiągnięciem

### Indeksy dla tabeli `displayed_content`
- `idx_displayed_content_user_id` na `user_id` - Przyspiesza wyszukiwanie wyświetlonych treści dla konkretnego użytkownika
- `idx_displayed_content_device_id` na `device_id` - Przyspiesza wyszukiwanie wyświetlonych treści dla konkretnego urządzenia
- `idx_displayed_content_content_id` na `content_id` - Przyspiesza wyszukiwanie użytkowników, którym wyświetlono konkretną treść

### Indeksy dla tabeli `educational_content`
- `idx_educational_content_type` na `content_type` - Przyspiesza filtrowanie treści według typu
- `idx_educational_content_language` na `language` - Przyspiesza filtrowanie treści według języka
- `idx_educational_content_min_sobriety` na `min_sobriety_days` - Przyspiesza filtrowanie treści według minimalnego stażu trzeźwości

### Indeksy dla tabeli `help_resources`
- `idx_help_resources_type` na `resource_type` - Przyspiesza filtrowanie zasobów pomocy według typu
- `idx_help_resources_region` na `region` - Przyspiesza filtrowanie zasobów pomocy według regionu
- `idx_help_resources_country` na `country` - Przyspiesza filtrowanie zasobów pomocy według kraju

### Indeksy dla tabeli `survey_responses`
- `idx_survey_responses_user_id` na `user_id` - Przyspiesza wyszukiwanie odpowiedzi ankietowych dla konkretnego użytkownika
- `idx_survey_responses_device_id` na `device_id` - Przyspiesza wyszukiwanie odpowiedzi ankietowych dla konkretnego urządzenia
- `idx_survey_responses_question_id` na `question_id` - Przyspiesza wyszukiwanie odpowiedzi na konkretne pytanie
</related_db_resources>
- 
3. Definicje typów:
   <type_definitions>
   @/src/types.ts
   </type_definitions>

3. Tech stack:
   <tech_stack>
   @/.ai/prd/techStack.md
   </tech_stack>

4. Implementation rules:
   <implementation_rules>
   @/.cursor/rules/shared.mdc , @/.cursor/rules/backend.mdc , @/.cursor/rules/astro.mdc )
   </implementation_rules>

Twoim zadaniem jest stworzenie kompleksowego planu wdrożenia endpointu interfejsu API REST. Przed dostarczeniem ostatecznego planu użyj znaczników <analysis>, aby przeanalizować informacje i nakreślić swoje podejście. W tej analizie upewnij się, że:

1. Podsumuj kluczowe punkty specyfikacji API.
2. Wymień wymagane i opcjonalne parametry ze specyfikacji API.
3. Wymień niezbędne typy DTO i Command Modele.
4. Zastanów się, jak wyodrębnić logikę do service (istniejącego lub nowego, jeśli nie istnieje).
5. Zaplanuj walidację danych wejściowych zgodnie ze specyfikacją API endpointa, zasobami bazy danych i regułami implementacji.
6. Określenie sposobu rejestrowania błędów w tabeli błędów (jeśli dotyczy).
7. Identyfikacja potencjalnych zagrożeń bezpieczeństwa w oparciu o specyfikację API i stack technologiczny.
8. Nakreśl potencjalne scenariusze błędów i odpowiadające im kody stanu.

Po przeprowadzeniu analizy utwórz szczegółowy plan wdrożenia w formacie markdown. Plan powinien zawierać następujące sekcje:

1. Przegląd punktu końcowego
2. Szczegóły żądania
3. Szczegóły odpowiedzi
4. Przepływ danych
5. Względy bezpieczeństwa
6. Obsługa błędów
7. Wydajność
8. Kroki implementacji

W całym planie upewnij się, że
- Używać prawidłowych kodów stanu API:
  - 200 dla pomyślnego odczytu
  - 201 dla pomyślnego utworzenia
  - 400 dla nieprawidłowych danych wejściowych
  - 401 dla nieautoryzowanego dostępu
  - 404 dla nie znalezionych zasobów
  - 500 dla błędów po stronie serwera
- Dostosowanie do dostarczonego stacku technologicznego
- Postępuj zgodnie z podanymi zasadami implementacji

Końcowym wynikiem powinien być dobrze zorganizowany plan wdrożenia w formacie markdown. Oto przykład tego, jak powinny wyglądać dane wyjściowe:

``markdown
# API Endpoint Implementation Plan: [Nazwa punktu końcowego]

## 1. Przegląd punktu końcowego
[Krótki opis celu i funkcjonalności punktu końcowego]

## 2. Szczegóły żądania
- Metoda HTTP: [GET/POST/PUT/DELETE]
- Struktura URL: [wzorzec URL]
- Parametry:
  - Wymagane: [Lista wymaganych parametrów]
  - Opcjonalne: [Lista opcjonalnych parametrów]
- Request Body: [Struktura treści żądania, jeśli dotyczy]

## 3. Wykorzystywane typy
[DTOs i Command Modele niezbędne do implementacji]

## 3. Szczegóły odpowiedzi
[Oczekiwana struktura odpowiedzi i kody statusu]

## 4. Przepływ danych
[Opis przepływu danych, w tym interakcji z zewnętrznymi usługami lub bazami danych]

## 5. Względy bezpieczeństwa
[Szczegóły uwierzytelniania, autoryzacji i walidacji danych]

## 6. Obsługa błędów
[Lista potencjalnych błędów i sposób ich obsługi]

## 7. Rozważania dotyczące wydajności
[Potencjalne wąskie gardła i strategie optymalizacji]

## 8. Etapy wdrożenia
1. [Krok 1]
2. [Krok 2]
3. [Krok 3]
...
```

Końcowe wyniki powinny składać się wyłącznie z planu wdrożenia w formacie markdown i nie powinny powielać ani powtarzać żadnej pracy wykonanej w sekcji analizy.

Pamiętaj, aby zapisać swój plan wdrożenia jako .ai/view-implementation-plan.md. Upewnij się, że plan jest szczegółowy, przejrzysty i zapewnia kompleksowe wskazówki dla zespołu programistów.
