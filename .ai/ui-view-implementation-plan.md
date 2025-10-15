# Plan implementacji widoków codziennego pytania

## 1. Przegląd
Głównym celem implementacji jest stworzenie kluczowych widoków aplikacji mamkaca.pl związanych z codziennym pytaniem "Masz dziś kaca?". Obejmuje to widok główny z pytaniem oraz dwa widoki odpowiedzi (pozytywnej i negatywnej). Jest to kluczowa funkcjonalność aplikacji, umożliwiająca użytkownikom śledzenie okresów trzeźwości i otrzymywanie wsparcia w procesie zdrowienia.

## 2. Routing widoku
Implementacja obejmuje trzy powiązane ścieżki:
- `/` - Ekran główny z codziennym pytaniem
- `/response/yes` - Ekran po odpowiedzi twierdzącej
- `/response/no` - Ekran po odpowiedzi przeczącej

## 3. Struktura komponentów
```
src/
├── pages/
│   ├── index.astro             # Główna strona z pytaniem
│   ├── response/
│   │   ├── yes.astro           # Widok odpowiedzi "Tak"
│   │   └── no.astro            # Widok odpowiedzi "Nie"
├── components/
│   ├── DailyQuestion/
│   │   ├── DailyQuestion.astro # Komponent z pytaniem
│   │   ├── AnswerButtons.tsx   # Interaktywne przyciski odpowiedzi
│   │   └── StatusIndicator.tsx # Wskaźnik synchronizacji
│   ├── SobrietyCounter.astro   # Licznik dni trzeźwości
│   ├── MotivationalContent/
│   │   ├── MotivationalQuote.astro     # Motywujący cytat
│   │   ├── EducationalContent.astro    # Treść edukacyjna
│   │   └── ProgressVisualization.tsx   # Wizualizacja postępu
│   ├── SupportContent/
│   │   ├── SupportMessage.astro        # Komunikat wspierający
│   │   ├── HelpResourcesList.astro     # Lista zasobów pomocy
│   │   ├── EmergencyCallButton.tsx     # Przycisk połączenia z infolinią
│   │   └── CopingTechniquesList.astro  # Techniki radzenia sobie
│   └── shared/
│       ├── MotivationalGraphic.astro   # Grafika motywacyjna
│       └── MessageDisplay.astro        # Komponent wyświetlający komunikaty
```

## 4. Szczegóły komponentów

### Strona index.astro (Ekran główny)
- Opis komponentu: Główny widok aplikacji, prezentujący pytanie "Masz dziś kaca?" i umożliwiający użytkownikowi udzielenie odpowiedzi.
- Główne elementy: 
  - DailyQuestion (pytanie i przyciski odpowiedzi)
  - SobrietyCounter (aktualny licznik dni trzeźwości)
  - StatusIndicator (wskaźnik synchronizacji dla zalogowanych)
- Obsługiwane interakcje:
  - Kliknięcie w przycisk "Tak" (przekierowanie na `/response/yes`)
  - Kliknięcie w przycisk "Nie" (przekierowanie na `/response/no`)
- Obsługiwana walidacja:
  - Sprawdzenie czy użytkownik już odpowiedział dzisiaj
  - Obsługa stanu ładowania podczas sprawdzania
- Typy: `AnswerDto`, `CurrentStreakDto`
- Propsy: Brak (strona główna)

### DailyQuestion.astro
- Opis komponentu: Komponent wyświetlający pytanie "Masz dziś kaca?" i przyciski odpowiedzi.
- Główne elementy: 
  - Nagłówek z pytaniem
  - AnswerButtons (przyciski "Tak"/"Nie")
- Obsługiwane interakcje: Brak bezpośrednich (przekazuje do AnswerButtons)
- Obsługiwana walidacja: Brak
- Typy: Brak
- Propsy: `hasAnsweredToday` (boolean), `isLoading` (boolean)

### AnswerButtons.tsx
- Opis komponentu: Interaktywne przyciski umożliwiające odpowiedź na codzienne pytanie.
- Główne elementy: 
  - Przycisk "Tak" (czerwony/ostrzegawczy)
  - Przycisk "Nie" (zielony/pozytywny)
- Obsługiwane interakcje:
  - Kliknięcie na przycisk (wywołuje funkcję onAnswerSubmit)
  - Obsługa stanu ładowania (disabled podczas submitu)
- Obsługiwana walidacja: 
  - Przyciski są wyłączone jeśli użytkownik już odpowiedział dzisiaj
  - Przyciski są wyłączone podczas przetwarzania odpowiedzi
- Typy: `SubmitAnswerCommand`
- Propsy: 
  ```typescript
  interface AnswerButtonsProps {
    onAnswerSubmit: (answerType: 'yes' | 'no') => Promise<void>;
    isDisabled: boolean;
    isLoading: boolean;
  }
  ```

### SobrietyCounter.astro
- Opis komponentu: Wyświetla aktualną liczbę dni trzeźwości.
- Główne elementy: 
  - Licznik dni
  - Etykieta "dni trzeźwości"
  - Opcjonalna informacja o dacie rozpoczęcia
- Obsługiwane interakcje: Brak
- Obsługiwana walidacja: Brak
- Typy: `CurrentStreakDto`
- Propsy: 
  ```typescript
  interface SobrietyCounterProps {
    daysSober: number;
    sinceDate?: string;
    showDetailedInfo?: boolean;
  }
  ```

### StatusIndicator.tsx
- Opis komponentu: Dyskretny wskaźnik pokazujący stan synchronizacji danych (tylko dla zalogowanych).
- Główne elementy:
  - Ikona stanu synchronizacji
  - Ewentualna wiadomość o ostatniej synchronizacji
- Obsługiwane interakcje: Brak
- Obsługiwana walidacja: Brak
- Typy: Brak
- Propsy: 
  ```typescript
  interface StatusIndicatorProps {
    isLoggedIn: boolean;
    isSynced: boolean;
    lastSyncTime?: string;
  }
  ```

### Strona yes.astro (Odpowiedź "Tak")
- Opis komponentu: Widok wyświetlany po udzieleniu odpowiedzi "Tak", zapewniający wsparcie i zasoby pomocy.
- Główne elementy:
  - MotivationalGraphic (wspierająca grafika)
  - SupportMessage (komunikat wspierający)
  - HelpResourcesList (lista dostępnych form wsparcia)
  - EmergencyCallButton (przycisk szybkiego połączenia z infolinią)
  - CopingTechniquesList (lista technik radzenia sobie z głodem alkoholowym)
- Obsługiwane interakcje:
  - Kliknięcie przycisku połączenia z infolinią
  - Nawigacja do szczegółowych informacji o technikach radzenia sobie
- Obsługiwana walidacja: Brak
- Typy: `HelpResourceDto`
- Propsy: Brak (strona)

### SupportMessage.astro
- Opis komponentu: Wyświetla wspierający komunikat dla użytkownika, który odpowiedział "Tak".
- Główne elementy:
  - Nagłówek wiadomości
  - Treść wspierająca
- Obsługiwane interakcje: Brak
- Obsługiwana walidacja: Brak
- Typy: Brak
- Propsy: 
  ```typescript
  interface SupportMessageProps {
    heading: string;
    message: string;
  }
  ```

### HelpResourcesList.astro
- Opis komponentu: Lista dostępnych form wsparcia i zasobów pomocy.
- Główne elementy:
  - Nagłówek sekcji
  - Lista zasobów (telefony, adresy)
  - Linki do szczegółów
- Obsługiwane interakcje: Kliknięcie w link/zasób
- Obsługiwana walidacja: Brak
- Typy: `HelpResourceDto[]`
- Propsy: 
  ```typescript
  interface HelpResourcesListProps {
    resources: Array<{
      name: string;
      description?: string;
      contactInfo: any;
      resourceType: string;
    }>;
  }
  ```

### EmergencyCallButton.tsx
- Opis komponentu: Przycisk umożliwiający szybkie połączenie telefoniczne z infolinią pomocy.
- Główne elementy:
  - Przycisk z ikoną telefonu
  - Numer telefonu
- Obsługiwane interakcje:
  - Kliknięcie (inicjuje połączenie telefoniczne)
- Obsługiwana walidacja:
  - Sprawdzenie czy urządzenie obsługuje tel: protokół
- Typy: Brak
- Propsy: 
  ```typescript
  interface EmergencyCallButtonProps {
    phoneNumber: string;
    label: string;
  }
  ```

### CopingTechniquesList.astro
- Opis komponentu: Lista technik radzenia sobie z głodem alkoholowym.
- Główne elementy:
  - Nagłówek sekcji
  - Lista technik z opisami
- Obsługiwane interakcje: Brak
- Obsługiwana walidacja: Brak
- Typy: Brak
- Propsy: 
  ```typescript
  interface CopingTechniquesListProps {
    techniques: Array<{
      title: string;
      description: string;
    }>;
  }
  ```

### Strona no.astro (Odpowiedź "Nie")
- Opis komponentu: Widok wyświetlany po udzieleniu odpowiedzi "Nie", oferujący gratulacje i motywację.
- Główne elementy:
  - MessageDisplay (komunikat gratulacyjny)
  - MotivationalQuote (motywujący cytat)
  - SobrietyCounter (zaktualizowany licznik dni trzeźwości)
  - EducationalContent (rotacyjna informacja edukacyjna)
  - ProgressVisualization (graficzna reprezentacja postępu)
- Obsługiwane interakcje:
  - Przycisk "Pokaż więcej" dla rozszerzonej informacji edukacyjnej
- Obsługiwana walidacja: Brak
- Typy: `CurrentStreakDto`, `EducationalContentDto`
- Propsy: Brak (strona)

### MessageDisplay.astro
- Opis komponentu: Uniwersalny komponent do wyświetlania komunikatów.
- Główne elementy:
  - Nagłówek
  - Treść wiadomości
  - Opcjonalna ikona
- Obsługiwane interakcje: Brak
- Obsługiwana walidacja: Brak
- Typy: Brak
- Propsy: 
  ```typescript
  interface MessageDisplayProps {
    title: string;
    message: string;
    icon?: string;
    variant?: 'success' | 'info' | 'warning' | 'error';
  }
  ```

### MotivationalQuote.astro
- Opis komponentu: Wyświetla motywujący cytat.
- Główne elementy:
  - Treść cytatu
  - Autor cytatu
- Obsługiwane interakcje: Brak
- Obsługiwana walidacja: Brak
- Typy: Brak
- Propsy: 
  ```typescript
  interface MotivationalQuoteProps {
    quote: string;
    author?: string;
  }
  ```

### EducationalContent.astro
- Opis komponentu: Wyświetla rotacyjną informację edukacyjną o alkoholu.
- Główne elementy:
  - Główna treść informacji
  - Przycisk "Pokaż więcej" (opcjonalnie)
  - Rozszerzona treść (ukryta do kliknięcia)
- Obsługiwane interakcje:
  - Kliknięcie w "Pokaż więcej"
- Obsługiwana walidacja: Brak
- Typy: `EducationalContentDto`
- Propsy: 
  ```typescript
  interface EducationalContentProps {
    content: string;
    extendedContent?: string;
    showMoreLabel?: string;
  }
  ```

### ProgressVisualization.tsx
- Opis komponentu: Graficzna reprezentacja postępu trzeźwości.
- Główne elementy:
  - Wykres/wizualizacja postępu
  - Legenda
  - Etykiety z danymi
- Obsługiwane interakcje:
  - Najechanie myszką na elementy wykresu (tooltip)
- Obsługiwana walidacja: Brak
- Typy: Zależne od implementacji wizualizacji
- Propsy: 
  ```typescript
  interface ProgressVisualizationProps {
    daysSober: number;
    streakData?: {
      date: string;
      wasAnswer: boolean;
      answerType: 'yes' | 'no' | 'none';
    }[];
    longestStreak?: number;
  }
  ```

## 5. Typy

Poza typami już zdefiniowanymi w `src/types.ts`, implementacja będzie wymagać następujących typów specyficznych dla widoku:

```typescript
// Typy specificzne dla Daily Question View
interface DailyQuestionViewModel {
  currentDate: string;
  hasAnsweredToday: boolean;
  lastAnswer?: {
    date: string;
    type: 'yes' | 'no' | 'none';
  };
}

// Typy dla odpowiedzi "Tak"
interface SupportViewModel {
  supportMessage: {
    heading: string;
    message: string;
  };
  helpResources: Array<HelpResourceDto>;
  emergencyContact: {
    phoneNumber: string;
    label: string;
  };
  copingTechniques: Array<{
    title: string;
    description: string;
  }>;
}

// Typy dla odpowiedzi "Nie"
interface CongratulationsViewModel {
  message: {
    title: string;
    content: string;
  };
  motivationalQuote: {
    quote: string;
    author?: string;
  };
  educationalContent: EducationalContentDto;
  sobrietyData: {
    daysSober: number;
    sinceDate: string;
    streakData?: Array<{
      date: string;
      wasAnswer: boolean;
      answerType: 'yes' | 'no' | 'none';
    }>;
    longestStreak?: number;
  };
}
```

## 6. Zarządzanie stanem

Implementacja wymaga zarządzania stanem dla kilku kluczowych funkcjonalności:

1. **Hook useAnswerSubmission**
   ```typescript
   function useAnswerSubmission() {
     const [isSubmitting, setIsSubmitting] = useState(false);
     const [hasAnsweredToday, setHasAnsweredToday] = useState(false);
     const [error, setError] = useState<string | null>(null);

     // Sprawdza czy użytkownik już odpowiedział dzisiaj
     const checkTodaysAnswer = useCallback(async () => {
       // Implementacja logiki sprawdzającej dzisiejszą odpowiedź
     }, []);

     // Przesyła odpowiedź użytkownika
     const submitAnswer = useCallback(async (answerType: 'yes' | 'no') => {
       // Implementacja logiki wysyłania odpowiedzi
     }, []);

     useEffect(() => {
       checkTodaysAnswer();
     }, [checkTodaysAnswer]);

     return {
       isSubmitting,
       hasAnsweredToday,
       error,
       submitAnswer
     };
   }
   ```

2. **Hook useSobrietyData**
   ```typescript
   function useSobrietyData() {
     const [currentStreak, setCurrentStreak] = useState<CurrentStreakDto | null>(null);
     const [longestStreak, setLongestStreak] = useState<LongestStreakDto | null>(null);
     const [isLoading, setIsLoading] = useState(false);
     const [error, setError] = useState<string | null>(null);

     // Pobiera dane o trzeźwości
     const fetchSobrietyData = useCallback(async () => {
       // Implementacja pobierania danych o aktualnym i najdłuższym okresie trzeźwości
     }, []);

     useEffect(() => {
       fetchSobrietyData();
     }, [fetchSobrietyData]);

     return {
       currentStreak,
       longestStreak,
       isLoading,
       error,
       refreshData: fetchSobrietyData
     };
   }
   ```

3. **Hook useEducationalContent**
   ```typescript
   function useEducationalContent() {
     const [content, setContent] = useState<EducationalContentDto | null>(null);
     const [isLoading, setIsLoading] = useState(false);
     const [error, setError] = useState<string | null>(null);
     const [expandedContent, setExpandedContent] = useState(false);

     // Pobiera treść edukacyjną
     const fetchContent = useCallback(async () => {
       // Implementacja pobierania treści
     }, []);

     const toggleExpandedContent = () => setExpandedContent(!expandedContent);

     useEffect(() => {
       fetchContent();
     }, [fetchContent]);

     return {
       content,
       isLoading,
       error,
       expandedContent,
       toggleExpandedContent
     };
   }
   ```

## 7. Integracja API

Implementacja będzie korzystać z następujących endpointów:

1. **Sprawdzanie dzisiejszej odpowiedzi**
   - Metoda: `GET /api/answers`
   - Parametry: 
     ```typescript
     {
       start_date: todayISODate,
       end_date: todayISODate,
     }
     ```
   - Odpowiedź: `PaginatedAnswersResponseDto`
   - Logika: Jeśli `total > 0`, użytkownik już odpowiedział dzisiaj

2. **Przesyłanie odpowiedzi**
   - Metoda: `POST /api/answers`
   - Dane: 
     ```typescript
     {
       answer_date: todayISODate,
       answer_time: currentISOTime,
       answer_type: 'yes' | 'no',
       answer_source: 'manual',
     }
     ```
   - Odpowiedź: `AnswerDto`
   - Obsługa błędów: Szczególnie kod 409 (Conflict) gdy użytkownik już odpowiedział

3. **Pobieranie danych o okresach trzeźwości**
   - Metoda: `GET /api/streaks`
   - Odpowiedź: 
     ```typescript
     {
       current_streak: CurrentStreakDto,
       longest_streak: LongestStreakDto
     }
     ```

4. **Pobieranie osiągnięć (opcjonalne w widoku odpowiedzi "Nie")**
   - Metoda: `GET /api/achievements`
   - Odpowiedź: `Array<UserAchievementDto>`

## 8. Interakcje użytkownika

1. **Ekran główny**
   - Kliknięcie przycisku "Tak":
     1. Wywołanie `submitAnswer('yes')`
     2. Jeśli sukces, przekierowanie na `/response/yes`
     3. Jeśli błąd, wyświetlenie komunikatu błędu
   
   - Kliknięcie przycisku "Nie":
     1. Wywołanie `submitAnswer('no')`
     2. Jeśli sukces, przekierowanie na `/response/no`
     3. Jeśli błąd, wyświetlenie komunikatu błędu

2. **Ekran odpowiedzi "Tak"**
   - Kliknięcie przycisku połączenia z infolinią:
     1. Wywołanie natywnego mechanizmu dzwonienia telefonu

   - Kliknięcie na zasób pomocy:
     1. Wyświetlenie szczegółowych informacji o zasobie lub przekierowanie

3. **Ekran odpowiedzi "Nie"**
   - Kliknięcie przycisku "Pokaż więcej" przy treści edukacyjnej:
     1. Rozwinięcie pełnej treści informacji
     2. Zmiana etykiety przycisku na "Pokaż mniej"

## 9. Warunki i walidacja

1. **Walidacja odpowiedzi**
   - Użytkownik może odpowiedzieć tylko raz dziennie
   - Sprawdzenie: Na wczytaniu ekranu głównego, aplikacja sprawdza czy jest już odpowiedź z dzisiejszą datą
   - Efekt: Jeśli użytkownik już odpowiedział, przyciski odpowiedzi są wyłączone (disabled)

2. **Autentykacja**
   - Zarówno zalogowani jak i niezalogowani użytkownicy mogą korzystać z funkcji
   - Dla zalogowanych: Używany nagłówek `Authorization: Bearer {token}`
   - Dla niezalogowanych: Używany nagłówek `X-Device-ID: {device_id}`

3. **Walidacja po stronie serwera**
   - API zwróci błąd 409 jeśli próbowano wysłać drugą odpowiedź tego samego dnia
   - Obsługa: Wyświetlenie komunikatu o błędzie i odświeżenie stanu interfejsu

## 10. Obsługa błędów

1. **Błędy sieciowe**
   - Problem: Brak połączenia internetowego
   - Obsługa: 
     - Wyświetlenie komunikatu o braku połączenia
     - Oferowanie opcji zapisu lokalnego i późniejszej synchronizacji (dla zarejestrowanych)

2. **Błędy API**
   - Problem: Serwer zwraca błąd 4xx/5xx
   - Obsługa:
     - Dla 401: Przekierowanie do logowania (jeśli token wygasł)
     - Dla 409: Informacja o niemożności zmiany już udzielonej odpowiedzi
     - Dla innych błędów: Ogólny komunikat z informacją o problemie

3. **Stan ładowania**
   - Problem: Długi czas odpowiedzi API
   - Obsługa:
     - Wyświetlenie wskaźnika ładowania (spinner/progress)
     - Wyłączenie przycisków podczas oczekiwania na odpowiedź

4. **Tryb offline**
   - Problem: Aplikacja używana bez połączenia z internetem
   - Obsługa:
     - Zapisanie odpowiedzi lokalnie
     - Wyświetlenie wskaźnika "niesynchronizowane"
     - Automatyczna synchronizacja po przywróceniu połączenia

## 11. Kroki implementacji

1. **Przygotowanie struktury plików**
   - Utworzenie plików stron w `src/pages/`
   - Utworzenie struktury katalogów dla komponentów

2. **Implementacja komponentów współdzielonych**
   - Stworzenie SobrietyCounter
   - Stworzenie MessageDisplay
   - Stworzenie MotivationalGraphic

3. **Implementacja customowych hooków**
   - Utworzenie useAnswerSubmission
   - Utworzenie useSobrietyData
   - Utworzenie useEducationalContent

4. **Implementacja ekranu głównego**
   - Stworzenie DailyQuestion.astro
   - Stworzenie AnswerButtons.tsx
   - Integracja z hookiem useAnswerSubmission
   - Implementacja sprawdzania czy użytkownik już odpowiedział

5. **Implementacja widoku odpowiedzi "Tak"**
   - Stworzenie komponentów wsparcia
   - Implementacja przycisku połączenia telefonicznego
   - Implementacja wyświetlania zasobów pomocy

6. **Implementacja widoku odpowiedzi "Nie"**
   - Stworzenie komponentów gratulacji i motywacji
   - Implementacja wyświetlania treści edukacyjnej
   - Integracja z SobrietyCounter
   - Stworzenie wizualizacji postępu

7. **Integracja z API**
   - Implementacja logiki wysyłania odpowiedzi
   - Implementacja pobierania danych o trzeźwości
   - Obsługa przypadków błędów

8. **Testowanie**
   - Weryfikacja poprawności obsługi wszystkich przypadków użycia
   - Weryfikacja responsywności widoków
   - Testowanie różnych stanów (zalogowany/niezalogowany, z/bez połączenia)

9. **Finalizacja i optymalizacja**
   - Dodanie dokumentacji komponentów
   - Optymalizacja wydajności (np. memo dla kosztownych renderowań)
   - Implementacja lazy loading dla mniej krytycznych komponentów
