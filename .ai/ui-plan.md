# Architektura UI dla mamkaca.pl

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

### Ekran Statystyk Podstawowych
- **Ścieżka widoku**: `/statistics/basic`
- **Główny cel**: Wyświetlanie podstawowych statystyk trzeźwości
- **Kluczowe informacje**: Aktualny licznik, najdłuższy okres, procent dni trzeźwych
- **Kluczowe komponenty**:
  - CurrentStreakCounter - aktualny licznik dni trzeźwości
  - LongestStreakInfo - informacja o najdłuższym okresie trzeźwości
  - MonthlyPercentage - procent dni trzeźwych w bieżącym miesiącu
  - LoginPrompt - zachęta do zalogowania dla rozszerzonych statystyk (dla niezalogowanych)
- **UX, dostępność i bezpieczeństwo**:
  - Przejrzyste wizualizacje danych
  - Czytelne liczniki i procenty
  - Podstawowe statystyki dostępne offline
  - Nienatarczywe zachęty do rejestracji

### Ekran Statystyk Rozszerzonych
- **Ścieżka widoku**: `/statistics/advanced`
- **Główny cel**: Dostarczanie rozbudowanych statystyk dla zalogowanych użytkowników
- **Kluczowe informacje**: Historia odpowiedzi, oszczędności finansowe, poprawa zdrowia, trendy
- **Kluczowe komponenty**:
  - AnswerHistoryCalendar - historia odpowiedzi w formie kalendarza
  - FinancialSavingsChart - wizualizacja oszczędności finansowych
  - HealthImprovementInfo - informacje o poprawie zdrowia
  - TrendChart - wykres trendów trzeźwości
  - StatisticsFilter - filtry okresów (tydzień, miesiąc, rok)
- **UX, dostępność i bezpieczeństwo**:
  - Interaktywne wykresy i wizualizacje
  - Opcje eksportu danych
  - Wyraźne wyjaśnienia metryk
  - Synchronizacja z serwerem dla aktualności danych

### Ekran Osiągnięć
- **Ścieżka widoku**: `/achievements`
- **Główny cel**: Prezentowanie osiągniętych kamieni milowych trzeźwości
- **Kluczowe informacje**: Zdobyte osiągnięcia, nadchodzące kamienie milowe
- **Kluczowe komponenty**:
  - AchievementsList - lista zdobytych osiągnięć
  - UpcomingAchievements - informacja o zbliżających się kamieniach milowych
  - AchievementDetail - szczegóły i opis każdego osiągnięcia
  - ShareAchievement - opcja udostępnienia osiągnięcia (opcjonalna)
- **UX, dostępność i bezpieczeństwo**:
  - Atrakcyjne, gamifikacyjne elementy wizualne
  - Celebracja nowych osiągnięć
  - Motywujące opisy i grafiki
  - Synchronizacja osiągnięć z kontem użytkownika

### Ekran Zasobów Pomocy
- **Ścieżka widoku**: `/help`
- **Główny cel**: Dostarczanie zasobów wsparcia w walce z uzależnieniem
- **Kluczowe informacje**: Telefony zaufania, grupy wsparcia, techniki radzenia sobie
- **Kluczowe komponenty**:
  - EmergencyHelpline - lista telefonów zaufania z możliwością połączenia
  - SupportGroups - lista lokalnych grup wsparcia
  - CopingTechniques - techniki radzenia sobie z głodem alkoholowym
  - ResourcesFilter - możliwość filtrowania zasobów
- **UX, dostępność i bezpieczeństwo**:
  - Wyraźne, łatwo dostępne przyciski połączeń
  - Grupowanie zasobów według kategorii
  - Aktualizacje zasobów przy połączeniu z internetem
  - Zapamiętywanie lokalnych zasobów dla trybu offline

### Ekran Ustawień
- **Ścieżka widoku**: `/settings`
- **Główny cel**: Umożliwienie personalizacji aplikacji
- **Kluczowe informacje**: Ustawienia powiadomień, kosztów, prywatności, konta
- **Kluczowe komponenty**:
  - NotificationSettings - ustawienia czasu i formatu powiadomień
  - ExpenseSettings - personalizacja kosztów alkoholu
  - PrivacySettings - ustawienia prywatności
  - AccountManagement - zarządzanie kontem (dla zalogowanych)
  - AppSecurity - opcje zabezpieczenia dostępu do aplikacji
- **UX, dostępność i bezpieczeństwo**:
  - Jasne etykiety i opisy ustawień
  - Potwierdzenia zmian ustawień
  - Kontrola danych osobowych
  - Natychmiastowe zapisywanie zmian

### Ekran Logowania/Rejestracji
- **Ścieżka widoku**: `/auth`
- **Główny cel**: Umożliwienie utworzenia konta lub zalogowania się
- **Kluczowe informacje**: Formularze logowania i rejestracji, informacje o korzyściach
- **Kluczowe komponenty**:
  - LoginForm - formularz logowania
  - RegisterForm - formularz rejestracji
  - BenefitsInfo - informacja o korzyściach z posiadania konta
  - PasswordRecovery - opcja odzyskiwania hasła
- **UX, dostępność i bezpieczeństwo**:
  - Minimalistyczne formularze z podstawowymi danymi
  - Wyraźna informacja o opcjonalności rejestracji
  - Bezpieczne przechowywanie danych uwierzytelniających
  - Walidacja danych wejściowych

### Ekran Profilu
- **Ścieżka widoku**: `/profile`
- **Główny cel**: Zarządzanie danymi użytkownika i urządzeniami
- **Kluczowe informacje**: Dane użytkownika, lista urządzeń, opcje eksportu i usunięcia danych
- **Kluczowe komponenty**:
  - UserInfo - podstawowe dane użytkownika
  - DevicesList - lista powiązanych urządzeń
  - DataExport - opcja eksportu danych
  - AccountDeletion - opcja usunięcia konta
- **UX, dostępność i bezpieczeństwo**:
  - Przejrzysty układ informacji
  - Wyraźne ostrzeżenia przed nieodwracalnymi akcjami
  - Potwierdzenia dla krytycznych operacji
  - Transparentność dotycząca przechowywanych danych

### Ekran Ankiet/Feedbacku
- **Ścieżka widoku**: `/feedback`
- **Główny cel**: Zbieranie opinii i doświadczeń użytkowników
- **Kluczowe informacje**: Krótka ankieta, formularz zgłaszania błędów
- **Kluczowe komponenty**:
  - SurveyForm - formularz ankiety (max 2-3 pytania)
  - SkipOption - możliwość pominięcia ankiety
  - BugReportForm - formularz zgłaszania błędów
  - ThankYouMessage - podziękowanie za udział
- **UX, dostępność i bezpieczeństwo**:
  - Nienatarczywe prezentowanie ankiet
  - Proste formularze z minimalnymi polami
  - Opcja dołączenia zrzutu ekranu do zgłoszenia błędu
  - Anonimowe przesyłanie danych

### Ekran FAQ
- **Ścieżka widoku**: `/faq`
- **Główny cel**: Dostarczanie odpowiedzi na najczęściej zadawane pytania
- **Kluczowe informacje**: Pogrupowane tematycznie pytania i odpowiedzi
- **Kluczowe komponenty**:
  - CategoryList - lista kategorii pytań
  - SearchBar - wyszukiwarka w FAQ
  - ExpandableQuestions - rozwijane pytania z odpowiedziami
  - FeedbackQuestion - pytanie o pomocność odpowiedzi
- **UX, dostępność i bezpieczeństwo**:
  - Łatwa nawigacja między kategoriami
  - Szybkie wyszukiwanie
  - Dostępność offline po pierwszym załadowaniu
  - Aktualizacje treści przy połączeniu z internetem

### Ekran "Reset bez wstydu"
- **Ścieżka widoku**: `/reset`
- **Główny cel**: Umożliwienie świadomego zresetowania licznika trzeźwości
- **Kluczowe informacje**: Wspierający komunikat, analiza wyzwalaczy, opcja dodania notatki
- **Kluczowe komponenty**:
  - SupportiveMessage - wspierający komunikat bez stygmatyzacji
  - TriggersAnalysis - pomoc w analizie wyzwalaczy nawrotu
  - OptionalNote - pole do dodania notatki o przyczynach
  - ResetConfirmation - potwierdzenie resetu
- **UX, dostępność i bezpieczeństwo**:
  - Wspierający, nieoceniający ton komunikatów
  - Wyraźna informacja o konsekwencjach resetu
  - Opcjonalność dodatkowych informacji
  - Zwiększone wsparcie po resecie

### Widget Ekranu Głównego
- **Ścieżka widoku**: Nie dotyczy (element systemowy)
- **Główny cel**: Wyświetlanie licznika dni trzeźwości na ekranie głównym telefonu
- **Kluczowe informacje**: Aktualny licznik dni trzeźwości
- **Kluczowe komponenty**:
  - CounterDisplay - wyświetlacz liczby dni
  - WidgetStyles - różne style widgetu (minimalistyczny/standardowy)
  - TouchArea - obszar dotykowy otwierający aplikację
- **UX, dostępność i bezpieczeństwo**:
  - Różne rozmiary widgetu
  - Wysoki kontrast dla czytelności
  - Dyskretna prezentacja (bez wyraźnych odniesień do uzależnienia)
  - Aktualizacja licznika nawet bez otwierania aplikacji

## 3. Mapa podróży użytkownika

### Główny przepływ codzienny

1. Użytkownik otrzymuje powiadomienie z pytaniem "Masz dziś kaca?"
2. Po kliknięciu powiadomienia, otwiera się aplikacja na ekranie głównym
3. Użytkownik odpowiada "Tak" lub "Nie" na pytanie
4. W zależności od odpowiedzi:
   - Odpowiedź "Tak":
     - Użytkownik widzi ekran odpowiedzi pozytywnej z wspierającym komunikatem
     - Licznik dni trzeźwości resetuje się
     - Użytkownik może przejrzeć dostępne zasoby pomocy
     - Opcjonalnie, użytkownik może wykonać połączenie telefoniczne z infolinią pomocy
   - Odpowiedź "Nie":
     - Użytkownik widzi ekran odpowiedzi negatywnej z gratulacjami
     - Licznik dni trzeźwości aktualizuje się
     - Użytkownik otrzymuje rotacyjną informację edukacyjną
5. Użytkownik może przejść do innych sekcji aplikacji (statystyki, osiągnięcia, pomoc, ustawienia)

### Przepływ onboardingu (pierwsze uruchomienie)

1. Użytkownik pobiera i uruchamia aplikację po raz pierwszy
2. Wyświetlane są ekrany wprowadzające (max 3)
   - Ekran 1: Wprowadzenie do aplikacji i jej głównego celu
   - Ekran 2: Informacja o prywatności i anonimowości
   - Ekran 3: Podstawowa konfiguracja (czas powiadomień, opcjonalne dane)
3. Użytkownik może pominąć onboarding w dowolnym momencie
4. Na ostatnim ekranie użytkownik klika "Rozpocznij"
5. Użytkownik trafia na ekran główny z pytaniem "Masz dziś kaca?"

### Przepływ rejestracji i logowania (opcjonalny)

1. Użytkownik wybiera opcję utworzenia konta z ekranu głównego lub ustawień
2. Użytkownik wprowadza podstawowe dane (email, hasło)
3. System weryfikuje dane i tworzy konto
4. Użytkownik otrzymuje potwierdzenie na email
5. Po zalogowaniu, dane z urządzenia są synchronizowane z kontem
6. Użytkownik zyskuje dostęp do rozszerzonych funkcji (historia, zaawansowane statystyki)

### Przepływ przy wykryciu kryzysu

1. System wykrywa wzorzec sugerujący kryzys (np. kilka dni z rzędu odpowiedzi "tak" po dłuższym okresie abstynencji)
2. Przy następnym uruchomieniu aplikacji, użytkownik widzi specjalny komunikat wspierający
3. Prezentowane są dodatkowe zasoby pomocy dostosowane do sytuacji
4. Użytkownik może:
   - Odrzucić dodatkowe wsparcie
   - Przejrzeć zasoby pomocy
   - Skorzystać z funkcji "Reset bez wstydu"
   - Połączyć się bezpośrednio z infolinią pomocy

### Przepływ synchronizacji (dla zalogowanych użytkowników)

1. Użytkownik korzysta z aplikacji offline
2. Gdy urządzenie uzyskuje dostęp do internetu, system inicjuje automatyczną synchronizację
3. Dane lokalne są porównywane z danymi na serwerze
4. W przypadku konfliktów, stosowana jest ustalona strategia rozwiązywania
5. Użytkownik jest dyskretnie informowany o statusie synchronizacji
6. Po zakończeniu synchronizacji, dane lokalne i na serwerze są spójne

## 4. Układ i struktura nawigacji

### Nawigacja główna

Aplikacja wykorzystuje nawigację typu "tab bar" na dole ekranu, umożliwiającą szybki dostęp do głównych sekcji:

1. **Główna** - Ekran główny z codziennym pytaniem i odpowiedziami
2. **Statystyki** - Podstawowe i rozszerzone statystyki trzeźwości
3. **Osiągnięcia** - Lista zdobytych i nadchodzących kamieni milowych
4. **Pomoc** - Zasoby wsparcia, telefony zaufania, grupy wsparcia
5. **Więcej** - Menu rozwijane z dodatkowymi opcjami (Ustawienia, FAQ, Profil, Feedback)

### Nawigacja kontekstowa

Wewnątrz głównych sekcji, nawigacja opiera się na:

1. **Przyciski powrotu** - Standardowy przycisk "Wstecz" w lewym górnym rogu
2. **Karty/Zakładki** - W sekcjach z wieloma podwidokami (np. Statystyki podstawowe/rozszerzone)
3. **Listy rozwijane** - Dla grupowania podobnych treści (np. kategorie pytań w FAQ)
4. **Przyciski akcji** - Wyraźne przyciski dla głównych działań na każdym ekranie

### Nawigacja globalna

1. **Przycisk "Pomoc"** - Zawsze dostępny w sytuacjach kryzysowych
2. **Przycisk "Profil"** - Szybki dostęp do konta (dla zalogowanych)
3. **Przycisk "Powiadomienia"** - Informacje systemowe i przypomnienia (jeśli są)

### Przepływ między widokami

```
Ekran Główny
│
├── Odpowiedź "Tak" ──────┬─── Zasoby Pomocy ───── Telefony Zaufania
│                         └─── Reset Licznika
│
├── Odpowiedź "Nie" ──────┬─── Aktualizacja Licznika
│                         └─── Treść Edukacyjna ─── Rozszerzona Treść
│
├── Statystyki ───────────┬─── Podstawowe
│                         └─── Rozszerzone (dla zalogowanych)
│
├── Osiągnięcia ──────────┬─── Lista Osiągnięć
│                         └─── Szczegóły Osiągnięcia
│
├── Pomoc ─────────────────── Lista Zasobów ───── Szczegóły Zasobu
│
└── Więcej ────────────────┬─── Ustawienia
                          ├─── FAQ
                          ├─── Profil (dla zalogowanych)
                          └─── Feedback
```

## 5. Kluczowe komponenty

### DailyQuestion
- **Opis**: Centralny komponent wyświetlający pytanie "Masz dziś kaca?" z przyciskami odpowiedzi.
- **Użycie**: Ekran główny.
- **Właściwości**: Tekst pytania, opcje odpowiedzi, stan odpowiedzi (udzielona/nieudzielona).
- **Integracja API**: `/api/answers` do zapisywania odpowiedzi.

### SobrietyCounter
- **Opis**: Komponent wyświetlający liczbę dni trzeźwości w formie licznika.
- **Użycie**: Ekran główny, ekran odpowiedzi negatywnej, statystyki, widget.
- **Właściwości**: Liczba dni, data rozpoczęcia, animacja aktualizacji.
- **Integracja API**: `/api/streaks/current` do pobierania aktualnego okresu trzeźwości.

### AnswerResponse
- **Opis**: Komponent wyświetlający odpowiednią reakcję na odpowiedź użytkownika.
- **Użycie**: Ekrany odpowiedzi pozytywnej i negatywnej.
- **Właściwości**: Typ odpowiedzi, komunikat, grafika, dodatkowe akcje.
- **Integracja API**: `/api/content` do pobierania rotacyjnych treści.

### EducationalContentCard
- **Opis**: Karta wyświetlająca treści edukacyjne o alkoholu.
- **Użycie**: Ekran odpowiedzi negatywnej, sekcja edukacyjna.
- **Właściwości**: Treść, typ treści (cytat/fakt/grafika), opcja "Pokaż więcej".
- **Integracja API**: `/api/content` z parametrami filtrowania.

### HelpResourcesList
- **Opis**: Lista dostępnych zasobów pomocy w walce z uzależnieniem.
- **Użycie**: Ekran pomocy, ekran odpowiedzi pozytywnej.
- **Właściwości**: Filtry typów zasobów, opcja bezpośredniego połączenia, aktualizacja.
- **Integracja API**: `/api/help` z parametrami filtrowania.

### StatisticsDisplay
- **Opis**: Zestaw komponentów do wizualizacji statystyk trzeźwości.
- **Użycie**: Ekrany statystyk podstawowych i rozszerzonych.
- **Właściwości**: Typ statystyki, okres, opcje filtrowania, typy wykresów.
- **Integracja API**: `/api/statistics` z parametrami okresu.

### AchievementCard
- **Opis**: Karta wyświetlająca osiągnięcie (kamień milowy trzeźwości).
- **Użycie**: Ekran osiągnięć, powiadomienia o nowych osiągnięciach.
- **Właściwości**: Nazwa osiągnięcia, opis, ikona, data zdobycia, postęp do następnego.
- **Integracja API**: `/api/achievements` i `/api/milestones`.

### SettingsForm
- **Opis**: Formularz do zarządzania ustawieniami aplikacji.
- **Użycie**: Ekran ustawień.
- **Właściwości**: Sekcje ustawień, kontrolki (przełączniki, pola wyboru, pola tekstowe).
- **Integracja API**: `/api/settings` do pobierania i aktualizacji ustawień.

### AuthForm
- **Opis**: Formularz do logowania i rejestracji.
- **Użycie**: Ekran logowania/rejestracji.
- **Właściwości**: Pola formularza, walidacja, przyciski akcji, komunikaty błędów.
- **Integracja API**: `/api/auth/register` i `/api/auth/login`.

### SyncIndicator
- **Opis**: Wskaźnik statusu synchronizacji danych.
- **Użycie**: Globalnie w aplikacji (np. w nagłówku).
- **Właściwości**: Stan synchronizacji, ostatnia synchronizacja, animacja.
- **Integracja API**: `/api/sync` i `/api/sync/{sync_id}`.

### ResetConfirmation
- **Opis**: Komponent do potwierdzenia resetu licznika trzeźwości.
- **Użycie**: Ekran "Reset bez wstydu".
- **Właściwości**: Komunikat wspierający, pole notatki, przyciski potwierdzenia/anulowania.
- **Integracja API**: `/api/resets` do tworzenia rekordu resetu.

### SurveyPopup
- **Opis**: Wyskakujące okienko z krótką ankietą.
- **Użycie**: Wyświetlane okresowo lub po osiągnięciu kamieni milowych.
- **Właściwości**: Pytania ankiety, opcje odpowiedzi, przyciski pominięcia/wysłania.
- **Integracja API**: `/api/surveys` i `/api/surveys/{survey_id}/responses`.

### OfflineIndicator
- **Opis**: Wskaźnik pracy w trybie offline z informacją o synchronizacji.
- **Użycie**: Globalnie w aplikacji, gdy brak połączenia.
- **Właściwości**: Stan połączenia, komunikat, opcja ręcznej synchronizacji po połączeniu.
- **Integracja API**: `/api/sync` po przywróceniu połączenia.

### CrisisDetectionAlert
- **Opis**: Alert wyświetlany przy wykryciu wzorca sugerującego kryzys.
- **Użycie**: Wyświetlany w odpowiednim momencie, bazując na historii odpowiedzi.
- **Właściwości**: Komunikat wspierający, dodatkowe zasoby, opcje akcji.
- **Integracja API**: `/api/crisis/detections` i `/api/help`.
