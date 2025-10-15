1. Implementacja tymczasowego identyfikatora urządzenia dla użytkowników niezalogowanych z mechanizmem migracji danych po rejestracji.
2. Struktura tabeli odpowiedzi zawierająca: ID użytkownika/urządzenia, datę, typ odpowiedzi (tak/nie/brak), godzinę, źródło odpowiedzi i opcjonalną notatkę.
3. Wymuszenie ograniczenia jednorazowej odpowiedzi dziennie na poziomie bazy danych.
4. Przechowywanie ustawień użytkownika dotyczących wydatków na alkohol (dzienna/tygodniowa/miesięczna kwota i waluta).
5. Utworzenie tabeli kamieni milowych trzeźwości (1 dzień, 7 dni, 30 dni, 100 dni) oraz relacyjnej tabeli osiągnięć użytkowników.
6. Przechowywanie treści edukacyjnych w bazie danych z kategoryzacją według typu, treści, kategorii i wymaganego stażu trzeźwości, z możliwością uzupełniania z zewnętrznego API.
7. Implementacja mechanizmu rotacji treści poprzez śledzenie wyświetlonych użytkownikowi treści.
8. Utworzenie tabeli zasobów pomocy z typem, opisem, danymi kontaktowymi i lokalizacją geograficzną.
9. Przechowywanie historii odpowiedzi dla analizy wzorców kryzysowych.
10. Minimalizacja zbieranych danych osobowych (tylko email) i implementacja Row Level Security w Supabase.
11. Przechowywanie dat w formacie UTC z uwzględnieniem strefy czasowej użytkownika.
12. Przechowywanie agregacji statystycznych dla optymalizacji zapytań.
13. Implementacja funkcji "reset bez wstydu" z zachowaniem historycznych danych.
14. Przechowywanie ustawień powiadomień użytkownika (czas, włączone/wyłączone, format).
15. Struktura dla ankiet feedback'u i odpowiedzi użytkowników z kontrolą częstotliwości wyświetlania.
16. Zabezpieczenie aplikacji kodem PIN przechowanym jako hash.
17. Implementacja mechanizmu synchronizacji między urządzeniami z timestampami i identyfikatorami urządzeń.
18. Dodanie pola języka do treści edukacyjnych dla przyszłej internacjonalizacji.
19. Dynamiczne obliczanie licznika dni trzeźwości zamiast przechowywania go jako osobnego pola.

<matched_recommendations>

1. Tymczasowe ID urządzenia i mechanizm migracji danych dla użytkowników niezalogowanych.
2. Struktura danych dla dziennych odpowiedzi z dodatkowymi metadanymi.
3. Wymuszenie ograniczeń na poziomie bazy danych dla jednokrotnej odpowiedzi dziennie.
4. Struktura przechowywania informacji o wydatkach na alkohol dla obliczania oszczędności.
5. System kamieni milowych trzeźwości z relacyjnymi powiązaniami osiągnięć.
6. Elastyczna struktura dla treści edukacyjnych z kategoryzacją i filtrowaniem.
7. Mechanizm rotacji treści oparty na historii wyświetleń.
8. Organizacja zasobów pomocy z możliwością filtrowania geograficznego.
9. System wykrywania wzorców kryzysowych bazujący na historii odpowiedzi.
10. Minimalizacja danych osobowych i implementacja Row Level Security.
11. Obsługa stref czasowych dla właściwego zarządzania powiadomieniami.
12. Agregacja statystyk dla wydajnego raportowania.
13. Funkcjonalność "reset bez wstydu" zachowująca historyczne dane.
14. Struktura ustawień powiadomień użytkownika.
15. System ankiet z kontrolą częstotliwości.
16. Mechanizm zabezpieczenia PIN-em.
17. System synchronizacji między urządzeniami.
18. Wsparcie dla wielojęzyczności.
19. Wydajna metoda obliczania statystyk trzeźwości. </matched_recommendations>

<database_planning_summary>

# Planowanie schematu bazy danych dla MamKaca.pl

## Główne wymagania

Na podstawie analizy dokumentu wymagań produktu (PRD) i dyskusji, główne wymagania dla schematu bazy danych obejmują:

1. __Obsługa użytkowników zalogowanych i niezalogowanych__ - z mechanizmem migracji danych
2. __Rejestrowanie codziennych odpowiedzi__ - z ograniczeniem jednej odpowiedzi dziennie
3. __Śledzenie okresów trzeźwości__ - z dynamicznym obliczaniem statystyk
4. __Zarządzanie treściami edukacyjnymi__ - z rotacją i personalizacją
5. __System kamieni milowych__ - predefiniowane osiągnięcia trzeźwościowe
6. __Zasoby pomocy__ - dostosowane do lokalizacji użytkownika
7. __Wykrywanie wzorców kryzysowych__ - analiza historii odpowiedzi
8. __Ochrona prywatności__ - minimalizacja danych i Row Level Security
9. __Ustawienia użytkownika__ - powiadomienia, preferencje, zabezpieczenia
10. __System ankiet i feedbacku__ - z kontrolą częstotliwości

## Kluczowe encje i relacje

### 1. System użytkowników

- __Użytkownicy (users)__ - podstawowa encja z minimalnym zestawem danych osobowych (email)
- __Urządzenia (devices)__ - identyfikatory urządzeń dla użytkowników niezalogowanych i synchronizacji
    - Relacja: Użytkownik 1:N Urządzenia

### 2. System odpowiedzi

- __Odpowiedzi (answers)__ - rejestr odpowiedzi na pytanie "Masz dziś kaca?"

    - ID użytkownika/urządzenia (klucz obcy)
    - Data i godzina odpowiedzi
    - Typ odpowiedzi (tak/nie/brak)
    - Źródło odpowiedzi (powiadomienie/ręczne)
    - Opcjonalna notatka (dla odpowiedzi "tak")
    - Timestamp modyfikacji
    - Relacja: Użytkownik/Urządzenie 1:N Odpowiedzi

- __Resety (resets)__ - rejestr "resetów bez wstydu"

    - ID użytkownika (klucz obcy)
    - Data resetu
    - Opcjonalna notatka o przyczynach
    - Relacja: Użytkownik 1:N Resety

### 3. System treści edukacyjnych

- __Treści edukacyjne (educational_content)__

    - Typ (cytat/fakt/grafika)
    - Treść
    - Kategoria
    - Minimalny staż trzeźwości do wyświetlenia
    - Język

- __Wyświetlone treści (displayed_content)__

    - ID użytkownika (klucz obcy)

    - ID treści (klucz obcy)

    - Data wyświetlenia

    - Relacje:

        - Użytkownik 1:N Wyświetlone treści
        - Treść edukacyjna 1:N Wyświetlone treści

### 4. System kamieni milowych

- __Kamienie milowe (milestones)__

    - Wartość (dni)
    - Nazwa
    - Opis
    - Grafika (opcjonalnie)

- __Osiągnięcia użytkownika (user_achievements)__

    - ID użytkownika (klucz obcy)

    - ID kamienia milowego (klucz obcy)

    - Data osiągnięcia

    - Relacje:

        - Użytkownik 1:N Osiągnięcia
        - Kamień milowy 1:N Osiągnięcia

### 5. System wsparcia

- __Zasoby pomocy (help_resources)__

    - Typ (telefon/grupa/technika)
    - Nazwa
    - Opis
    - Dane kontaktowe
    - Region/kraj

- __Wzorce kryzysowe (crisis_patterns)__

    - Definicja wzorca
    - Opis
    - Sugerowane działania

### 6. Preferencje i ustawienia

- __Ustawienia użytkownika (user_settings)__

    - ID użytkownika (klucz obcy)
    - Preferencje powiadomień (czas, włączone/wyłączone)
    - Format powiadomień (standardowy/dyskretny)
    - Strefa czasowa
    - Zabezpieczenie PIN-em (hash)
    - Wydatki na alkohol (kwota, częstotliwość, waluta)
    - Relacja: Użytkownik 1:1 Ustawienia

### 7. System feedback'u

- __Ankiety (surveys)__

    - Tytuł
    - Opis
    - Okres wyświetlania
    - Aktywna (tak/nie)

- __Pytania ankiet (survey_questions)__

    - ID ankiety (klucz obcy)
    - Treść pytania
    - Typ odpowiedzi
    - Kolejność
    - Relacja: Ankieta 1:N Pytania

- __Odpowiedzi na ankiety (survey_responses)__

    - ID użytkownika (klucz obcy)

    - ID pytania (klucz obcy)

    - Odpowiedź

    - Data wypełnienia

    - Relacje:

        - Użytkownik 1:N Odpowiedzi na ankiety
        - Pytanie 1:N Odpowiedzi na ankiety

- __Wyświetlenia ankiet (survey_displays)__

    - ID użytkownika (klucz obcy)

    - ID ankiety (klucz obcy)

    - Ostatnia data wyświetlenia

    - Relacje:

        - Użytkownik 1:N Wyświetlenia ankiet
        - Ankieta 1:N Wyświetlenia ankiet

### 8. Statystyki i agregacje

- __Agregacje statystyk (stat_aggregations)__

    - ID użytkownika (klucz obcy)
    - Typ okresu (dzień, tydzień, miesiąc, rok)
    - Data początku okresu
    - Liczba dni trzeźwych
    - Procent dni trzeźwych
    - Timestamp aktualizacji
    - Relacja: Użytkownik 1:N Agregacje statystyk

## Bezpieczeństwo i prywatność

1. __Row Level Security (RLS)__ - implementacja polityk w Supabase zapewniających, że użytkownik ma dostęp tylko do swoich danych
2. __Minimalizacja danych osobowych__ - zbieranie tylko niezbędnego minimum (email)
3. __Szyfrowanie wrażliwych danych__ - dodatkowa warstwa ochrony dla szczególnie wrażliwych informacji
4. __Bezpieczne przechowywanie PIN-ów i haseł__ - stosowanie odpowiednich algorytmów haszujących
5. __Automatyczne usuwanie nieaktywnych kont__ - zgodnie z polityką prywatności

## Optymalizacja i skalowalność

1. __Indeksowanie__ - kluczowych pól wykorzystywanych w zapytaniach (daty, ID użytkownika)
2. __Materialized views__ - dla często wykonywanych zapytań statystycznych
3. __Przechowywanie agregacji__ - aktualizowanych po każdej odpowiedzi lub okresowo
4. __Zarządzanie strefami czasowymi__ - przechowywanie dat w UTC z konwersją na poziomie aplikacji
5. __Mechanizm synchronizacji__ - timestampy i identyfikatory urządzeń dla wykrywania konfliktów
6. __Przyszła rozszerzalność__ - pola językowe, struktury pozwalające na dodawanie nowych funkcji </database_planning_summary>

<unresolved_issues>

1. __Metoda obliczania oszczędności finansowych__ - wątpliwości czy użytkownicy będą chcieli podawać swoje wydatki na alkohol. Rozważyć alternatywy: uśrednione statystyki konsumpcji alkoholu w Polsce lub opcjonalne podawanie wydatków.

2. __Integracja z zewnętrznym API dla treści edukacyjnych__ - potrzeba doprecyzowania czy i w jaki sposób treści edukacyjne mają być uzupełniane z zewnętrznych źródeł.

3. __Funkcja eksportu danych użytkownika__ - brak pewności czy ta funkcja jest potrzebna w MVP.

4. __Optymalna metoda obliczania licznika dni trzeźwości__ - potrzeba rozważenia różnych metod obliczania statystyk trzeźwości poza dynamicznym obliczaniem z wykorzystaniem materialized views. </unresolved_issues> </conversation_summary>

Na podstawie powyższego podsumowania, mamy solidne podstawy do zaprojektowania schematu bazy danych dla MVP aplikacji MamKaca.pl. Struktura obejmuje wszystkie kluczowe funkcjonalności opisane w PRD, z uwzględnieniem specyficznych wymagań dotyczących prywatności i bezpieczeństwa danych.


Skupmy się na rozwiązaniu nierozstrzygniętych kwestii, zanim przejdziemy do implementacji schematu bazy danych:

## 1. Metoda obliczania oszczędności finansowych

Proponuję trójwarstwowe podejście do tego problemu:

__Opcja A: Wartości domyślne z oficjalnych statystyk__

- Zaimplementować domyślne wartości oparte na oficjalnych statystykach (np. GUS, WHO)
- Przykład: średni Polak wydaje X zł tygodniowo na alkohol
- Korzyść: Nie wymaga podawania danych przez użytkownika, wszyscy otrzymują podobne statystyki

__Opcja B: Zgrubne szacunki__

- Dać użytkownikowi możliwość wyboru z predefiniowanych grup konsumpcji:

    - Lekka konsumpcja: ~30zł/tydzień (np. "kilka piw weekendowo")
    - Średnia konsumpcja: ~100zł/tydzień (np. "regularne picie, głównie weekendowe")
    - Intensywna konsumpcja: ~300zł/tydzień (np. "codzienne spożycie")

- Korzyść: Bardziej spersonalizowane niż wartości domyślne, ale nie wymaga podawania dokładnych kwot

__Opcja C: Dokładne dane (opcjonalne)__

- Pozwolić użytkownikowi na wprowadzenie dokładnych wartości, jeśli tego chce
- Można dodać prosty kalkulator: "Ile pijesz tygodniowo? Ile kosztuje Cię jedna sesja?"
- Korzyść: Najdokładniejsze obliczenia dla zainteresowanych użytkowników

__Rekomendacja__: Zaimplementować wszystkie trzy opcje, z Opcją A jako domyślną. Użytkownicy, którzy chcą bardziej spersonalizowanych statystyk, mogą wybrać Opcję B lub C.

## 2. Integracja z zewnętrznym API dla treści edukacyjnych

Proponuję następujące rozwiązanie:

__Warstwa 1: Statyczne treści w bazie danych__

- Przygotować początkowy zestaw ~100 treści edukacyjnych w bazie danych
- Te treści będą zawsze dostępne, nawet bez połączenia z internetem
- Zapewni to podstawową funkcjonalność aplikacji

__Warstwa 2: Okresowe aktualizacje treści__

- Stworzyć prosty system administracyjny do dodawania nowych treści
- Aktualizacje byłyby pobierane przez aplikację przy połączeniu z internetem
- Nie wymaga integracji z zewnętrznym API, tylko z własnym backendem

__Warstwa 3: Integracja API (przyszłe rozszerzenie)__

- W przyszłych wersjach można rozważyć integrację z API dostarczającymi:

    - Motywacyjne cytaty (np. API z cytatami motywacyjnymi)
    - Aktualne informacje medyczne o alkoholu (np. API z artykułami naukowymi)
    - Lokalne zasoby pomocy (np. API organizacji wspierających)

__Rekomendacja__: Rozpocząć od Warstwy 1 (statyczne treści) i Warstwy 2 (okresowe aktualizacje) w MVP. Zaprojektować schemat bazy danych tak, aby umożliwiał łatwą integrację Warstwy 3 w przyszłości, ale nie implementować jej na tym etapie.

## 3. Funkcja eksportu danych użytkownika

Funkcja eksportu danych jest istotna z kilku powodów:

__Zgodność z RODO/GDPR__:

- Użytkownicy mają prawo dostępu do swoich danych osobowych
- Eksport danych to standardowy sposób realizacji tego prawa

__Portabilność danych__:

- Użytkownik może chcieć przenieść swoje dane do innego narzędzia
- Może chcieć udostępnić historię swojemu terapeucie

__Backup__:

- Możliwość wykonania lokalnej kopii danych na wypadek problemów z kontem

__Rekomendacja__: Implementacja prostego eksportu danych w formacie JSON zawierającego:

- Historię odpowiedzi (daty i odpowiedzi)
- Osiągnięte kamienie milowe
- Podstawowe statystyki

Ta funkcja nie musi być priorytetem w pierwszej wersji MVP, ale warto uwzględnić ją w schemacie bazy danych (dodając np. tabelę `data_exports` śledzącą historię eksportów).

## 4. Optymalna metoda obliczania licznika dni trzeźwości

Istnieje kilka podejść do tego problemu:

__Opcja A: Obliczanie w locie__

- Dynamiczne obliczanie na podstawie zapytań do tabeli `answers`
- Zalety: Zawsze aktualne, nie wymaga dodatkowych pól
- Wady: Może być mniej wydajne przy dużej liczbie rekordów

__Opcja B: Materialized views__

- Utworzenie zmaterializowanej perspektywy, która jest aktualizowana po każdej odpowiedzi
- Zalety: Szybszy dostęp do danych, mniej obciążające dla bazy danych
- Wady: Wymaga mechanizmu regularnej aktualizacji

__Opcja C: Kolumna w tabeli użytkownika + triggery__

- Przechowywanie aktualnego licznika dni trzeźwości jako kolumny w tabeli `users`
- Wykorzystanie triggerów PostgreSQL do automatycznej aktualizacji po każdej odpowiedzi
- Zalety: Bardzo szybki dostęp, zautomatyzowana aktualizacja
- Wady: Dodatkowa złożoność, ryzyko niespójności danych

__Opcja D: Hybrydowe podejście__

- Przechowywanie "ostatniego dnia picia" jako kolumny w tabeli `users`
- Obliczanie licznika na podstawie różnicy między dzisiejszą datą a ostatnim dniem picia
- Zalety: Dobry balans między wydajnością a prostotą
- Wady: Wymaga aktualizacji przy każdej odpowiedzi "tak"

__Rekomendacja__: Implementacja Opcji D jako najbardziej zrównoważonego podejścia. Ta metoda jest:

- Prosta w implementacji
- Wydajna w kwestii zapytań (jedno proste obliczenie)
- Odporna na błędy (zawsze można zrekonstruować dane z historii odpowiedzi)

Dla bardziej zaawansowanych statystyk (jak najdłuższy okres trzeźwości) można wykorzystać materialized views aktualizowane raz dziennie.