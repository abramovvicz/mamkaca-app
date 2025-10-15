Jako starszy programista frontendu Twoim zadaniem jest stworzenie szczegółowego planu wdrożenia nowego widoku w aplikacji internetowej. Plan ten powinien być kompleksowy i wystarczająco jasny dla innego programisty frontendowego, aby mógł poprawnie i wydajnie wdrożyć widok.

Najpierw przejrzyj następujące informacje:

1. Product Requirements Document (PRD):
   <prd>
   {{prd}} <- zamień na referencję do pliku @prd.md
   </prd>

2. Opis widoku:
   <view_description>
   ## 1. Przegląd struktury UI

Architektura UI dla aplikacji mamkaca.pl jest zaprojektowana zgodnie z zasadą "progressive enhancement", gdzie podstawowa funkcjonalność jest wspólna dla wszystkich platform, ale doświadczenie jest zoptymalizowane dla specyfiki urządzeń mobilnych (podejście mobile-first).

Struktura aplikacji opiera się na trójpoziomowej hierarchii nawigacji:
- **Główny poziom**: Ekran główny z pytaniem, Statystyki, Osiągnięcia, Pomoc, Ustawienia
- **Drugi poziom**: Szczegółowe widoki w ramach głównych kategorii (np. różne typy statystyk)
- **Trzeci poziom**: Akcje szczegółowe i formularze (np. reset streaku, zgłaszanie błędów)

Centralnym elementem interfejsu jest codzienne pytanie "Masz dziś kaca?", które determinuje główny przepływ użytkownika. Aplikacja implementuje zasadę "progressive disclosure" - pokazując dodatkowe funkcje dostępne dla zalogowanych użytkowników jako rozszerzenia podstawowych funkcji, a nie jako całkowicie oddzielne sekcje.

Architektura UI jest ściśle zintegrowana z endpointami API, zapewniając spójne doświadczenie użytkownika zarówno w trybie online, jak i offline.

## 2. Lista widoków

### Ekran Onboardingowy
- **Ścieżka widoku**: `/onboarding`
- **Główny cel**: Wprowadzenie nowego użytkownika do aplikacji
- **Kluczowe informacje**: Wyjaśnienie głównej funkcjonalności, polityki prywatności, opcjonalnej rejestracji
- **Kluczowe komponenty**:
    - Karuzela wprowadzająca (max 3 ekrany)
    - Przyciski "Dalej" i "Pomiń"
    - Przycisk "Rozpocznij" na ostatnim ekranie
    - Formularz podstawowej konfiguracji (opcjonalny)
- **UX, dostępność i bezpieczeństwo**:
    - Jasne, proste komunikaty z dużymi przyciskami
    - Możliwość pominięcia onboardingu
    - Informacja o opcjonalności zbierania danych
    - Krótkie ekrany bez przeciążenia informacjami

### Ekran Główny z Codziennym Pytaniem
- **Ścieżka widoku**: `/`
- **Główny cel**: Umożliwienie użytkownikowi odpowiedzi na pytanie "Masz dziś kaca?"
- **Kluczowe informacje**: Pytanie, licznik aktualnego okresu trzeźwości
- **Kluczowe komponenty**:
    - DailyQuestion - wyraźne pytanie "Masz dziś kaca?"
    - Przyciski odpowiedzi "Tak" i "Nie"
    - SobrietyCounter - aktualny licznik dni trzeźwości
    - StatusIndicator - dyskretny wskaźnik synchronizacji (dla zalogowanych)
- **UX, dostępność i bezpieczeństwo**:
    - Duże, łatwe do kliknięcia przyciski
    - Wysoki kontrast dla lepszej czytelności
    - Brak możliwości zmiany odpowiedzi po jej udzieleniu
    - Dostęp do pytania nawet w trybie offline

### Ekran Odpowiedzi Pozytywnej ("Tak")
- **Ścieżka widoku**: `/response/yes`
- **Główny cel**: Zapewnienie wsparcia i zasobów pomocy po odpowiedzi "Tak"
- **Kluczowe informacje**: Wspierający komunikat, informacje o dostępnych zasobach pomocy
- **Kluczowe komponenty**:
    - MotivationalGraphic - wspierająca grafika
    - SupportMessage - komunikat wspierający
    - HelpResourcesList - lista dostępnych form wsparcia
    - EmergencyCallButton - przycisk szybkiego połączenia z infolinią
    - CopingTechniquesList - lista technik radzenia sobie z głodem alkoholowym
- **UX, dostępność i bezpieczeństwo**:
    - Spokojne, wspierające kolory i grafiki
    - Wyraźne przyciski pomocy
    - Możliwość bezpośredniego połączenia telefonicznego
    - Treści dostępne offline

### Ekran Odpowiedzi Negatywnej ("Nie")
- **Ścieżka widoku**: `/response/no`
- **Główny cel**: Dostarczenie gratulacji i motywacji po odpowiedzi "Nie"
- **Kluczowe informacje**: Gratulacje, cytat motywacyjny, informacja edukacyjna
- **Kluczowe komponenty**:
    - CongratulationsMessage - komunikat gratulacyjny
    - MotivationalQuote - motywujący cytat
    - SobrietyCounter - zaktualizowany licznik dni trzeźwości
    - EducationalContent - rotacyjna informacja edukacyjna
    - ProgressVisualization - graficzna reprezentacja postępu
- **UX, dostępność i bezpieczeństwo**:
    - Żywe, pozytywne kolory i grafiki
    - Animacja aktualizacji licznika
    - Przycisk "Pokaż więcej" dla rozszerzonej informacji edukacyjnej
    - Treści dostępne offline
   </view_description>

3. User Stories:
   <user_stories>
#### US-001 Pierwszy uruchomienie aplikacji
Jako nowy użytkownik, chcę przejść przez prosty proces wprowadzający, abym mógł szybko zrozumieć, jak działa aplikacja.

Kryteria akceptacji:
- Po pierwszym uruchomieniu aplikacji, użytkownik widzi maksymalnie 3 ekrany wprowadzające
- Na ekranach wyświetlane są kluczowe informacje o funkcjonalności aplikacji
- Użytkownik może przejść dalej dotykając przycisk "Dalej" lub przewijając ekrany
- Na ostatnim ekranie znajduje się przycisk "Rozpocznij"
- Proces onboardingu można pominąć poprzez przycisk "Pomiń" (widoczny na każdym ekranie)
#### US-006 Otrzymywanie codziennego powiadomienia
Jako użytkownik aplikacji, chcę otrzymywać codzienne powiadomienie z pytaniem "Masz dziś kaca?", aby pamiętać o odpowiedzi i śledzeniu mojej trzeźwości.

Kryteria akceptacji:
- Powiadomienie jest wysyłane codziennie o ustawionej godzinie
- Powiadomienie zawiera pytanie "Masz dziś kaca?"
- Dotknięcie powiadomienia otwiera aplikację do ekranu odpowiedzi
- Powiadomienie znika po udzieleniu odpowiedzi na pytanie
- Powiadomienie pozostaje w centrum powiadomień do końca dnia

#### US-007 Udzielanie odpowiedzi na pytanie
Jako użytkownik aplikacji, chcę odpowiedzieć na pytanie "Masz dziś kaca?" jednym z dwóch przycisków (tak/nie), aby śledzić moją trzeźwość.

Kryteria akceptacji:
- Ekran z pytaniem zawiera dwa wyraźne przyciski: "Tak" i "Nie"
- Po naciśnięciu przycisku odpowiedź jest zapisywana
- System nie pozwala na zmianę odpowiedzi po jej udzieleniu
- Użytkownik może odpowiedzieć tylko raz dziennie
- Jeśli użytkownik nie odpowie jednego dnia, system oznacza ten dzień jako "brak odpowiedzi"

#### US-008 Otrzymywanie informacji po odpowiedzi "Tak"
Jako użytkownik, który odpowiedział "Tak" na pytanie o kaca, chcę otrzymać wspierające informacje i zasoby, które pomogą mi w trudnym momencie.

Kryteria akceptacji:
- Po odpowiedzi "Tak" system wyświetla wspierający komunikat
- Ekran zawiera motywującą grafikę
- Wyświetlana jest lista dostępnych zasobów pomocy (telefony zaufania, adresy grup wsparcia)
- Dostępna jest opcja bezpośredniego połączenia z infolinią pomocy
- Wyświetlane są techniki radzenia sobie z głodem alkoholowym
- Informacja o resetowaniu licznika dni trzeźwości

#### US-009 Otrzymywanie pochwały po odpowiedzi "Nie"
Jako użytkownik, który odpowiedział "Nie" na pytanie o kaca, chcę otrzymać pochwałę i motywację do kontynuowania trzeźwości.

Kryteria akceptacji:
- Po odpowiedzi "Nie" system wyświetla gratulacyjny komunikat
- Pokazywany jest motywujący cytat
- Aktualizowany i wyświetlany jest licznik dni trzeźwości
- Prezentowany jest rotacyjny fakt edukacyjny o alkoholu
- System pokazuje graficzną reprezentację postępu
</user_stories>

4. Endpoint Description:
   <endpoint_description>
#### Submit Answer
- Method: POST
- Path: `/api/answers`
- Description: Submit answer to daily "Masz dziś kaca?" question
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Request Body:
  ```json
  {
    "answer_date": "2025-10-12",
    "answer_time": "09:15:00",
    "answer_type": "yes",
    "answer_source": "notification",
    "note": "Optional note about the answer"
  }
  ```
- Response:
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
- Success: 201 Created
- Errors:
    - 400 Bad Request - Invalid answer data
    - 401 Unauthorized - Not authenticated
    - 409 Conflict - Already answered today

#### Get Answers
- Method: GET
- Path: `/api/answers`
- Description: Get user's answer history
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Query Parameters:
    - start_date: Filter by start date (YYYY-MM-DD)
    - end_date: Filter by end date (YYYY-MM-DD)
    - answer_type: Filter by answer type (yes/no/none)
    - limit: Limit results (default: 30)
    - offset: Offset for pagination
- Response:
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
- Success: 200 OK
- Errors:
    - 400 Bad Request - Invalid query parameters
    - 401 Unauthorized - Not authenticated

### Sobriety Streaks

#### Get Current Streak
- Method: GET
- Path: `/api/streaks/current`
- Description: Get current sobriety streak
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Response:
  ```json
  {
    "days_sober": 30,
    "since_date": "2025-09-12"
  }
  ```
- Success: 200 OK
- Errors:
    - 401 Unauthorized - Not authenticated

#### Get Longest Streak
- Method: GET
- Path: `/api/streaks/longest`
- Description: Get longest sobriety streak
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Response:
  ```json
  {
    "longest_streak_days": 45,
    "start_date": "2025-05-01",
    "end_date": "2025-06-15"
  }
  ```
- Success: 200 OK
- Errors:
    - 401 Unauthorized - Not authenticated

#### Reset Streak
- Method: POST
- Path: `/api/resets`
- Description: Create a "Reset bez wstydu" record
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Request Body:
  ```json
  {
    "reset_date": "2025-10-12",
    "note": "Optional note about reset"
  }
  ```
- Response:
  ```json
  {
    "id": "uuid",
    "reset_date": "2025-10-12",
    "note": "Optional note about reset",
    "created_at": "2025-10-12T09:45:03Z"
  }
  ```
- Success: 201 Created
- Errors:
    - 400 Bad Request - Invalid reset data
    - 401 Unauthorized - Not authenticated

### Statistics

#### Get Statistics
- Method: GET
- Path: `/api/statistics`
- Description: Get user's sobriety statistics
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Query Parameters:
    - period_type: day, week, month, year (default: month)
    - start_date: Filter by start date (YYYY-MM-DD)
    - limit: Limit results (default: 12)
- Response:
  ```json
  [
    {
      "period_type": "month",
      "period_start": "2025-09-01",
      "sober_days_count": 25,
      "sober_days_percentage": 83.33
    }
  ]
  ```
- Success: 200 OK
- Errors:
    - 400 Bad Request - Invalid query parameters
    - 401 Unauthorized - Not authenticated
   </endpoint_description>

5. Endpoint Implementation:
   <endpoint_implementation>
   {{endpoint-implementation}} <- zamień na referencję do implementacji endpointów, z których będzie korzystał widok (np. @generations.ts, @flashcards.ts)
   </endpoint_implementation>

6. Type Definitions:
   <type_definitions>
   {{types}} <- zamień na referencję do pliku z definicjami DTOsów (np. @types.ts)
   </type_definitions>

7. Tech Stack:
   <tech_stack>
   {{tech-stack}} <- zamień na referencję do pliku @tech-stack.md
   </tech_stack>

Przed utworzeniem ostatecznego planu wdrożenia przeprowadź analizę i planowanie wewnątrz tagów <implementation_breakdown> w swoim bloku myślenia. Ta sekcja może być dość długa, ponieważ ważne jest, aby być dokładnym.

W swoim podziale implementacji wykonaj następujące kroki:
1. Dla każdej sekcji wejściowej (PRD, User Stories, Endpoint Description, Endpoint Implementation, Type Definitions, Tech Stack):
- Podsumuj kluczowe punkty
- Wymień wszelkie wymagania lub ograniczenia
- Zwróć uwagę na wszelkie potencjalne wyzwania lub ważne kwestie
2. Wyodrębnienie i wypisanie kluczowych wymagań z PRD
3. Wypisanie wszystkich potrzebnych głównych komponentów, wraz z krótkim opisem ich opisu, potrzebnych typów, obsługiwanych zdarzeń i warunków walidacji
4. Stworzenie wysokopoziomowego diagramu drzewa komponentów
5. Zidentyfikuj wymagane DTO i niestandardowe typy ViewModel dla każdego komponentu widoku. Szczegółowo wyjaśnij te nowe typy, dzieląc ich pola i powiązane typy.
6. Zidentyfikuj potencjalne zmienne stanu i niestandardowe hooki, wyjaśniając ich cel i sposób ich użycia
7. Wymień wymagane wywołania API i odpowiadające im akcje frontendowe
8. Zmapuj każdej historii użytkownika do konkretnych szczegółów implementacji, komponentów lub funkcji
9. Wymień interakcje użytkownika i ich oczekiwane wyniki
10. Wymień warunki wymagane przez API i jak je weryfikować na poziomie komponentów
11. Zidentyfikuj potencjalne scenariusze błędów i zasugeruj, jak sobie z nimi poradzić
12. Wymień potencjalne wyzwania związane z wdrożeniem tego widoku i zasugeruj możliwe rozwiązania

Po przeprowadzeniu analizy dostarcz plan wdrożenia w formacie Markdown z następującymi sekcjami:

1. Przegląd: Krótkie podsumowanie widoku i jego celu.
2. Routing widoku: Określenie ścieżki, na której widok powinien być dostępny.
3. Struktura komponentów: Zarys głównych komponentów i ich hierarchii.
4. Szczegóły komponentu: Dla każdego komponentu należy opisać:
- Opis komponentu, jego przeznaczenie i z czego się składa
- Główne elementy HTML i komponenty dzieci, które budują komponent
- Obsługiwane zdarzenia
- Warunki walidacji (szczegółowe warunki, zgodnie z API)
- Typy (DTO i ViewModel) wymagane przez komponent
- Propsy, które komponent przyjmuje od rodzica (interfejs komponentu)
5. Typy: Szczegółowy opis typów wymaganych do implementacji widoku, w tym dokładny podział wszelkich nowych typów lub modeli widoku według pól i typów.
6. Zarządzanie stanem: Szczegółowy opis sposobu zarządzania stanem w widoku, określenie, czy wymagany jest customowy hook.
7. Integracja API: Wyjaśnienie sposobu integracji z dostarczonym punktem końcowym. Precyzyjnie wskazuje typy żądania i odpowiedzi.
8. Interakcje użytkownika: Szczegółowy opis interakcji użytkownika i sposobu ich obsługi.
9. Warunki i walidacja: Opisz jakie warunki są weryfikowane przez interfejs, których komponentów dotyczą i jak wpływają one na stan interfejsu
10. Obsługa błędów: Opis sposobu obsługi potencjalnych błędów lub przypadków brzegowych.
11. Kroki implementacji: Przewodnik krok po kroku dotyczący implementacji widoku.

Upewnij się, że Twój plan jest zgodny z PRD, historyjkami użytkownika i uwzględnia dostarczony stack technologiczny.

Ostateczne wyniki powinny być w języku polskim i zapisane w pliku o nazwie .ai/{view-name}-view-implementation-plan.md. Nie uwzględniaj żadnej analizy i planowania w końcowym wyniku.

Oto przykład tego, jak powinien wyglądać plik wyjściowy (treść jest do zastąpienia):

```markdown
# Plan implementacji widoku [Nazwa widoku]

## 1. Przegląd
[Krótki opis widoku i jego celu]

## 2. Routing widoku
[Ścieżka, na której widok powinien być dostępny]

## 3. Struktura komponentów
[Zarys głównych komponentów i ich hierarchii]

## 4. Szczegóły komponentów
### [Nazwa komponentu 1]
- Opis komponentu [opis]
- Główne elementy: [opis]
- Obsługiwane interakcje: [lista]
- Obsługiwana walidacja: [lista, szczegółowa]
- Typy: [lista]
- Propsy: [lista]

### [Nazwa komponentu 2]
[...]

## 5. Typy
[Szczegółowy opis wymaganych typów]

## 6. Zarządzanie stanem
[Opis zarządzania stanem w widoku]

## 7. Integracja API
[Wyjaśnienie integracji z dostarczonym endpointem, wskazanie typów żądania i odpowiedzi]

## 8. Interakcje użytkownika
[Szczegółowy opis interakcji użytkownika]

## 9. Warunki i walidacja
[Szczegółowy opis warunków i ich walidacji]

## 10. Obsługa błędów
[Opis obsługi potencjalnych błędów]

## 11. Kroki implementacji
1. [Krok 1]
2. [Krok 2]
3. [...]
```

Rozpocznij analizę i planowanie już teraz. Twój ostateczny wynik powinien składać się wyłącznie z planu wdrożenia w języku polskim w formacie markdown, który zapiszesz w pliku .ai/{view-name}-view-implementation-plan.md i nie powinien powielać ani powtarzać żadnej pracy wykonanej w podziale implementacji.