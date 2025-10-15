# Podsumowanie architektury UI dla MVP mamkaca.pl

Na podstawie analizy dostarczonej dokumentacji i odpowiedzi na pytania, przygotowałem kompleksowe podsumowanie architektury UI dla MVP aplikacji mamkaca.pl.

<conversation_summary>

1. Przyjęcie zasady "progressive enhancement" - podstawowa funkcjonalność wspólna dla wszystkich platform, z doświadczeniem zoptymalizowanym pod specyfikę każdej platformy.
2. Wdrożenie trójpoziomowej hierarchii nawigacji: główny poziom, drugi poziom (szczegółowe widoki) i trzeci poziom (akcje szczegółowe).
3. Zastosowanie "progressive disclosure" - pokazywanie dodatkowych opcji dostępnych po zalogowaniu jako rozszerzenia podstawowych funkcji.
4. Uczynienie codziennego pytania "Masz dziś kaca?" centralnym elementem interfejsu, z płynnym przejściem do odpowiedniej reakcji po udzieleniu odpowiedzi.
5. Wykorzystanie natywnych funkcji Supabase do uwierzytelniania z prostymi ekranami logowania i rejestracji.
6. Udostępnianie treści edukacyjnych w dwóch formach: krótkiej (przy odpowiedzi) i pełnej (w dedykowanej sekcji).
7. Zdefiniowanie kluczowych komponentów UI, takich jak DailyQuestion, SobrietyCounter, AnswerResponse, itd.
8. Zastosowanie podejścia "mobile-first" z wykorzystaniem wariantów responsywności Tailwind CSS.
9. Implementacja dyskretnego informowania o statusie synchronizacji i magazynu lokalnego na wypadek braku połączenia.
10. Nienatarczywa integracja systemu ankiet, pojawiających się po osiągnięciu określonych kamieni milowych.

<matched_recommendations>

1. Struktura nawigacji z podziałem na Dzienny widok odpowiedzi, Statystyki, Pomoc i Ustawienia idealnie odpowiada endpointom API, szczególnie `/api/answers`, `/api/statistics`, `/api/help` i `/api/settings`.
2. Komponenty UI, takie jak DailyQuestion i AnswerResponse, bezpośrednio współpracują z endpointami `/api/answers`.
3. SobrietyCounter wykorzystuje dane z `/api/streaks/current` do wyświetlania aktualnego okresu trzeźwości.
4. AchievementCard prezentuje osiągnięcia pobierane przez `/api/achievements`.
5. Ekrany odpowiedzi pozytywnej i negatywnej wykorzystują odpowiednio zasoby pomocy (`/api/help`) i treści edukacyjne (`/api/content`).
6. System uwierzytelniania oparty na natywnych funkcjach Supabase współpracuje z endpointami `/api/auth/*`.
7. Architektura z oddzielnymi widokami dla użytkowników zalogowanych i niezalogowanych odpowiada systemowi podwójnego uwierzytelniania (JWT i Device ID).
8. Dyskretne informowanie o statusie synchronizacji wykorzystuje dane z endpointów `/api/sync`.
9. Implementacja lokalnego magazynu danych współpracuje z mechanizmem synchronizacji API.
10. System ankiet zintegrowany z interfejsem wykorzystuje endpointy `/api/surveys` do pobierania i wysyłania danych. </matched_recommendations>

<ui_architecture_planning_summary>

## Główne wymagania architektury UI

Architektura UI dla MVP mamkaca.pl opiera się na następujących kluczowych wymaganiach:

1. __Centralny element__: Codzienne pytanie "Masz dziś kaca?" jako główna funkcjonalność aplikacji.
2. __Dwutorowe uwierzytelnianie__: Obsługa zarówno użytkowników zalogowanych, jak i anonimowych (przez identyfikatory urządzeń).
3. __Responsywność__: Projekt "mobile-first" z optymalizacją dla różnych urządzeń.
4. __Spójność wizualna__: Wykorzystanie biblioteki shadcn/ui i Tailwind CSS.
5. __Działanie offline__: Możliwość podstawowej funkcjonalności bez połączenia internetowego.
6. __Progresywna funkcjonalność__: Stopniowe odkrywanie zaawansowanych funkcji.
7. __Wsparcie i edukacja__: Łatwy dostęp do materiałów pomocowych i edukacyjnych.

## Kluczowe widoki i ekrany

1. __Ekran powitalny / Onboarding__ (pierwsze uruchomienie)

   - Wprowadzenie do aplikacji i jej głównych funkcji
   - Opcjonalna rejestracja/logowanie
   - Podstawowa konfiguracja ustawień

2. __Ekran główny z codziennym pytaniem__

   - Centralne pytanie "Masz dziś kaca?"
   - Przyciski odpowiedzi "Tak"/"Nie"
   - Licznik aktualnego okresu trzeźwości
   - Szybki dostęp do menu głównego

3. __Ekrany odpowiedzi__

   - __Odpowiedź pozytywna ("Tak")__: Wsparcie, zasoby pomocy, zrozumienie
   - __Odpowiedź negatywna ("Nie")__: Gratulacje, statystyki, treści edukacyjne

4. __Ekran statystyk__

   - Podstawowe statystyki (dostępne dla wszystkich)
   - Rozszerzone statystyki (dla zalogowanych użytkowników)
   - Wizualizacje danych (wykresy, liczniki)
   - Szacunkowe oszczędności finansowe

5. __Ekran osiągnięć__

   - Lista zdobytych kamieni milowych
   - Nadchodzące osiągnięcia
   - Szczegóły i opis każdego osiągnięcia

6. __Ekran zasobów pomocy__

   - Telefony zaufania
   - Grupy wsparcia
   - Techniki radzenia sobie
   - Możliwość filtrowania zasobów

7. __Ekran ustawień__

   - Powiadomienia (czas, format)
   - Personalizacja kosztów (kwota, częstotliwość, waluta)
   - Preferencje prywatności
   - Zarządzanie kontem (dla zalogowanych)

8. __Ekran logowania/rejestracji__

   - Prosty formularz z minimalnymi wymaganiami
   - Wyraźne komunikaty o opcjonalności rejestracji
   - Opcje logowania/rejestracji przez Supabase

9. __Ekran profilu__ (dla zalogowanych)

   - Dane użytkownika
   - Eksport danych
   - Zarządzanie urządzeniami
   - Usunięcie konta

10. __Ekran ankiet/feedbacku__

    - Prosty formularz ankiety
    - Możliwość pominięcia
    - Podziękowanie za udział

## Przepływy użytkownika

### Główny przepływ codzienny

1. Użytkownik otwiera aplikację

2. Wyświetlane jest pytanie "Masz dziś kaca?"

3. Użytkownik odpowiada "Tak" lub "Nie"

4. W zależności od odpowiedzi:

   - "Tak": Wyświetlane są zasoby pomocy i wsparcie
   - "Nie": Wyświetlane są gratulacje i aktualizowane statystyki

5. Użytkownik może przejść do innych sekcji aplikacji

### Przepływ onboardingu

1. Użytkownik uruchamia aplikację po raz pierwszy
2. Wyświetlane są ekrany wprowadzające
3. Użytkownik konfiguruje podstawowe ustawienia
4. Opcjonalna rejestracja/logowanie
5. Przejście do ekranu głównego

### Przepływ logowania

1. Użytkownik wybiera opcję logowania
2. Wprowadza dane uwierzytelniające
3. System weryfikuje dane przez API `/api/auth/login`
4. Po zalogowaniu synchronizowane są dane z urządzenia z kontem
5. Użytkownik wraca do ostatniego ekranu

### Przepływ synchronizacji

1. System wykrywa połączenie internetowe
2. Inicjowane jest żądanie synchronizacji przez API `/api/sync`
3. Dane lokalne są porównywane z danymi serwera
4. W przypadku konfliktów, stosowana jest strategia rozwiązywania
5. Status synchronizacji jest dyskretnie wyświetlany użytkownikowi

## Strategia integracji z API i zarządzania stanem

### Integracja z API

- __Uwierzytelnianie__: Wykorzystanie mechanizmów JWT i Device ID zgodnie z endpointami `/api/auth/*`
- __Odpowiedzi dzienne__: Integracja z `/api/answers` do zapisywania i pobierania historii odpowiedzi
- __Statystyki__: Wykorzystanie `/api/statistics` i `/api/streaks` do generowania wizualizacji
- __Osiągnięcia__: Pobieranie z `/api/achievements` i `/api/milestones`
- __Pomoc__: Dynamiczne ładowanie zasobów pomocy z `/api/help`
- __Treści edukacyjne__: Wykorzystanie `/api/content` do wyświetlania odpowiednich materiałów

### Zarządzanie stanem

- __Stan lokalny__: Przechowywanie w localStorage/IndexedDB podstawowych danych
- __Synchronizacja__: Okresowe wysyłanie i pobieranie danych przez API `/api/sync`
- __Obsługa konfliktów__: Implementacja strategii rozwiązywania konfliktów
- __Buforowanie__: Przechowywanie często używanych danych dla lepszej wydajności
- __Tryb offline__: Umożliwienie podstawowej funkcjonalności bez połączenia

## Responsywność, dostępność i bezpieczeństwo

### Responsywność

- Implementacja podejścia "mobile-first"
- Wykorzystanie wariantów responsywności Tailwind CSS (sm:, md:, lg:, itd.)
- Dostosowanie layoutu do różnych rozmiarów ekranów
- Optymalizacja interakcji dotykowych dla urządzeń mobilnych

### Dostępność

- Zapewnienie odpowiednich kontrastów kolorystycznych
- Wykorzystanie semantycznego HTML
- Implementacja atrybutów ARIA dla lepszej nawigacji czytnikami ekranu
- Obsługa klawiatury dla wszystkich interakcji

### Bezpieczeństwo

- Implementacja mechanizmów uwierzytelniania Supabase
- Bezpieczne przechowywanie tokenów JWT
- Walidacja danych wejściowych przed wysłaniem do API
- Zabezpieczenie danych lokalnych </ui_architecture_planning_summary>

<unresolved_issues>

1. Szczegółowe mapowanie komponentów UI do konkretnych endpointów API dla wszystkich funkcji aplikacji.
2. Dokładna strategia buforowania i synchronizacji dla trybu offline.
3. Szczegółowy projekt interfejsu użytkownika dla rozwiązywania konfliktów synchronizacji.
4. Zdefiniowanie dokładnych komunikatów o błędach i ich prezentacji w interfejsie.
5. Pełna specyfikacja wizualna komponentów UI z wykorzystaniem shadcn/ui.
6. Szczegółowe przepływy użytkownika dla mniej typowych scenariuszy (np. resetowanie hasła).
7. Strategia migracji danych między urządzeniami po zalogowaniu.
8. Dokładne określenie mechanizmów walidacji danych wejściowych na poziomie UI. </unresolved_issues> </conversation_summary>

To podsumowanie stanowi kompleksowy przegląd architektury UI dla MVP aplikacji mamkaca.pl, łącząc decyzje projektowe z dostępnymi endpointami API. Dokument ten może służyć jako podstawa do dalszego rozwoju interfejsu użytkownika, wdrażania konkretnych komponentów UI i integracji z backendem.

Następne kroki mogą obejmować:

1. Stworzenie prototypów kluczowych ekranów w oparciu o tę architekturę
2. Rozwinięcie specyfikacji komponentów UI z wykorzystaniem shadcn/ui
3. Rozpoczęcie implementacji podstawowych widoków (ekran główny, odpowiedzi na pytanie)
4. Opracowanie szczegółowej strategii obsługi trybu offline i synchronizacji
