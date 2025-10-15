Jesteś asystentem AI, którego zadaniem jest pomoc w zaplanowaniu schematu bazy danych w PostgreSQL dla MVP (Minimum
Viable Product) na podstawie dostarczonych informacji. Twoim celem jest wygenerowanie listy pytań i zaleceń, które
zostaną wykorzystane w kolejnym promptowaniu do utworzenia schematu bazy danych, relacji i zasad bezpieczeństwa na
poziomie wierszy (RLS).

Prosimy o uważne zapoznanie się z poniższymi informacjami:

<product_requirements>

</product_requirements>

<tech_stack>
{{tech-stack}} <- zamień na referencję do @tech-stack.md
</tech_stack>

Przeanalizuj dostarczone informacje, koncentrując się na aspektach istotnych dla projektowania bazy danych. Rozważ
następujące kwestie:

1. Zidentyfikuj kluczowe encje i ich atrybuty na podstawie wymagań produktu.
2. Określ potencjalne relacje między jednostkami.
3. Rozważ typów danych i ograniczeń, które mogą być konieczne.
4. Pomyśl o skalowalności i wpływie na wydajność.
5. Oceń wymagania bezpieczeństwa i ich wpływ na projekt bazy danych.
6. Rozważ wszelkie konkretne funkcje PostgreSQL, które mogą być korzystne dla projektu.

Na podstawie analizy wygeneruj listę pytań i zaleceń. Powinny one dotyczyć wszelkich niejasności, potencjalnych
problemów lub obszarów, w których potrzeba więcej informacji, aby stworzyć skuteczny schemat bazy danych. Rozważ pytania
dotyczące:

1. Relacje i kardynalność jednostek
2. Typy danych i ograniczenia
3. Strategie indeksowania
4. Partycjonowanie (jeśli dotyczy)
5. Wymagania bezpieczeństwa na poziomie wierszy
6. Rozważania dotyczące wydajności
7. Kwestie skalowalności
8. Integralność i spójność danych

Dane wyjściowe powinny mieć następującą strukturę:

<pytania>
Wymień tutaj swoje pytania i zalecenia, ponumerowane dla przejrzystości:

Na przykład:

1. Czy encja „użytkownicy” powinna mieć powiązanie z „postami”?

Rekomendacja: Tak, encja „użytkownicy” powinna mieć powiązanie z „postami”, ponieważ użytkownicy mogą tworzyć posty.
</pytania>

Pamiętaj, że Twoim celem jest dostarczenie kompleksowej listy pytań i zaleceń, które pomogą w stworzeniu solidnego
schematu bazy danych PostgreSQL dla MVP. Skoncentruj się na jasności, trafności i dokładności swoich wyników. Nie
dołączaj żadnych dodatkowych komentarzy ani wyjaśnień poza określonym formatem wyjściowym.

Kontynuuj ten proces, generując nowe pytania i rekomendacje w oparciu o przekazany kontekst i odpowiedzi użytkownika,
dopóki użytkownik wyraźnie nie poprosi o podsumowanie.

Pamiętaj, aby skupić się na jasności, trafności i dokładności wyników. Nie dołączaj żadnych dodatkowych komentarzy ani
wyjaśnień poza określonym formatem wyjściowym.


## PODSUMOWANIE planowania bazy danyc:

Ad 1 Tak, według PRD rejestracja jest opcjonalna, ale warto rozważyć mechanizm pozwalający na migrację lokalnie
przechowywanych danych do konta po rejestracji. Sugeruję utworzenie tymczasowego identyfikatora urządzenia, który będzie
przechowywał dane przed rejestracją, a następnie umożliwi ich powiązanie z kontem.

AD 2 Minimalne dane to: identyfikator użytkownika/urządzenia, data, odpowiedź (tak/nie/brak), ale warto
rozważyć dodatkowe pola jak: godzina odpowiedzi, czy odpowiedź pochodzi z powiadomienia, ewentualna notatka
użytkownika (w przypadku odpowiedzi "tak").

AD 3. Nie, z PRD wynika, że użytkownik może odpowiedzieć tylko raz dziennie i nie może zmienić odpowiedzi po jej
udzieleniu. Sugeruję wymuszenie tego ograniczenia na poziomie bazy danych.

AD 4 Utworzenie tabeli z ustawieniami użytkownika, gdzie będzie przechowywana dzienna/tygodniowa/miesięczna
kwota wydatków na alkohol oraz waluta. Obliczenia można wykonywać na podstawie liczby dni trzeźwości. 

AD 5 Utworzenie osobnej tabeli z predefiniowanymi kamieniami milowymi (np. 1 dzień, 7 dni, 30 dni, 100 dni)
oraz tabeli łączącej użytkownika z osiągniętymi kamieniami milowymi, zawierającej datę osiągnięcia.

AD 6. Przechowywanie w bazie danych da większą elastyczność (łatwiejsza aktualizacja, kategoryzacja, filtrowanie
według stażu trzeźwości). Sugeruję tabelę treści edukacyjnych z polami: typ (cytat/fakt/inne), treść, kategoria,
minimalny staż trzeźwości do wyświetlenia. Można też uzupełniać danynmi pobranymi z zewnętrznego api ?

AD 7 Utworzenie tabeli łączącej użytkownika z wyświetlonymi treściami, zawierającej datę wyświetlenia.
Zapytanie wybierające treść może wtedy wykluczać te, które były pokazywane w ostatnich X dniach.

AD 8 Rekomendacja: Osobna tabela z zasobami pomocy, zawierająca: typ (telefon/grupa/technika), nazwa, opis, dane kontaktowe,
region/kraj (jeśli dotyczy). Pozwoli to na łatwe aktualizowanie i filtrowanie zasobów według lokalizacji użytkownika.

AD 9 Przechowywanie historii odpowiedzi pozwoli na analizę wzorców. Można dodać tabelę z predefiniowanymi
wzorcami kryzysowymi (np. "3 odpowiedzi tak po 30 dniach trzeźwości") i logiką aplikacji do ich wykrywania.

AD 10 Minimalizacja zbieranych danych osobowych - wymaganie tylko emaila do rejestracji, bez imienia/nazwiska.
Implementacja Row Level Security w Supabase, aby każdy użytkownik miał dostęp tylko do swoich danych. Szyfrowanie
wrażliwych danych.

AD 11 Przechowywanie wszystkich dat/czasów w UTC, z dodatkowym polem strefy czasowej w ustawieniach użytkownika.
Logika aplikacji powinna konwertować czasy przy wyświetlaniu i zapisie.

AD 12 Tak, to ułatwi generowanie statystyk. Warto przechowywać podstawowe agregacje (np. liczba dni trzeźwych w
miesiącu) w osobnej tabeli, aktualizowanej po każdej odpowiedzi lub okresowo.

AD 13 Nie wiem czy ta funkcja jesdt potrzeba.

ad 14 Dodanie flagi "reset" do tabeli odpowiedzi lub osobna tabela resetów z datami i opcjonalnym polem na
notatkę o przyczynach. Ważne, aby reset nie usuwał historycznych danych.

ad 15 Tabela ustawień użytkownika zawierająca preferowaną godzinę powiadomień, czy powiadomienia są włączone,
oraz preferowany format powiadomień (standardowy/dyskretny).

AD 16 Tabela ankiet z definicją pytań oraz tabela odpowiedzi użytkowników. Dodatkowo tabela łącząca użytkownika
z ostatnią datą wyświetlenia ankiety, aby kontrolować częstotliwość.

AD 17 Przechowywanie zahaszowanej wartości PIN w tabeli ustawień użytkownika, razem z flagą określającą, czy
zabezpieczenie jest włączone.

AD 18 Przechowywanie timestampów ostatniej modyfikacji dla każdego rekordu oraz identyfikatora urządzenia.
Implementacja mechanizmu wykrywania i rozwiązywania konfliktów.

AD 19 Warto dodać pole języka do tabeli treści edukacyjnych, nawet jeśli początkowo aplikacja będzie
jednojęzyczna. Ułatwi to przyszłą internacjonalizację.

AD 20 Nie przechowywanie licznika jako osobnego pola, ale obliczanie go dynamicznie na podstawie historii
odpowiedzi. Dodatkowo warto rozważyć materialized views dla częstych zapytań o statystyki.

Powyższe pytania i rekomendacje stanowią podstawę do projektowania schematu bazy danych PostgreSQL dla aplikacji
MamKaca.pl. Warto zauważyć, że Supabase dostarcza dodatkowe funkcjonalności, które mogą być przydatne, takie jak
autentykacja, bezpieczne API, storage, oraz Row Level Security, co jest szczególnie istotne dla aplikacji przechowującej
wrażliwe dane zdrowotne.

---

Jesteś asystentem AI, którego zadaniem jest podsumowanie rozmowy na temat planowania bazy danych dla MVP i przygotowanie zwięzłego podsumowania dla następnego etapu rozwoju. W historii konwersacji znajdziesz następujące informacje:
1. Dokument wymagań produktu (PRD)
2. Informacje o stacku technologicznym
3. Historia rozmów zawierająca pytania i odpowiedzi
4. Zalecenia dotyczące modelu

Twoim zadaniem jest:
1. Podsumować historii konwersacji, koncentrując się na wszystkich decyzjach związanych z planowaniem bazy danych.
2. Dopasowanie zaleceń modelu do odpowiedzi udzielonych w historii konwersacji. Zidentyfikuj, które zalecenia są istotne w oparciu o dyskusję.
3. Przygotuj szczegółowe podsumowanie rozmowy, które obejmuje:
   a. Główne wymagania dotyczące schematu bazy danych
   b. Kluczowe encje i ich relacje
   c. Ważne kwestie dotyczące bezpieczeństwa i skalowalności
   d. Wszelkie nierozwiązane kwestie lub obszary wymagające dalszego wyjaśnienia
4. Sformatuj wyniki w następujący sposób:

<conversation_summary>
<decisions>
[Wymień decyzje podjęte przez użytkownika, ponumerowane].
</decisions>

<matched_recommendations>
[Lista najistotniejszych zaleceń dopasowanych do rozmowy, ponumerowanych]
</matched_recommendations>

<database_planning_summary> [Podsumowanie planowania bazy danych]
[Podaj szczegółowe podsumowanie rozmowy, w tym elementy wymienione w kroku 3].
</database_planning_summary>

<unresolved_issues>
[Wymień wszelkie nierozwiązane kwestie lub obszary wymagające dalszych wyjaśnień, jeśli takie istnieją]
</unresolved_issues>
</conversation_summary>

Końcowy wynik powinien zawierać tylko treść w formacie markdown. Upewnij się, że Twoje podsumowanie jest jasne, zwięzłe i zapewnia cenne informacje dla następnego etapu planowania bazy danych.

##### PODSUMOWANIE

1. Implementacja mechanizmu tymczasowego ID urządzenia dla użytkowników niezalogowanych, z możliwością migracji danych po rejestracji.
2. Przechowywanie dla dziennych odpowiedzi: ID użytkownika/urządzenia, daty, odpowiedzi (tak/nie/brak), godziny odpowiedzi, źródła odpowiedzi i opcjonalnej notatki.
3. Wymuszenie ograniczenia jednej odpowiedzi dziennie na poziomie bazy danych.
4. Przechowywanie kamieni milowych trzeźwości w osobnej tabeli oraz relacji osiągnięć użytkowników.
5. Przechowywanie treści edukacyjnych w bazie danych z możliwością kategoryzacji i filtrowania według stażu trzeźwości.
6. Implementacja mechanizmu rotacji treści poprzez śledzenie wyświetlonych treści.
7. Przechowywanie zasobów pomocowych w osobnej tabeli z możliwością filtrowania według lokalizacji.
8. Implementacja wykrywania wzorców kryzysowych na podstawie analizy historii odpowiedzi.
9. Minimalizacja zbieranych danych osobowych i implementacja Row Level Security.
10. Przechowywanie wszystkich dat/czasów w UTC z uwzględnieniem stref czasowych użytkowników.
11. Przechowywanie agregacji statystyk w osobnej tabeli.
12. Implementacja funkcji "reset bez wstydu" bez usuwania historycznych danych.
13. Przechowywanie ustawień powiadomień użytkownika.
14. Zarządzanie ankietami feedback'u i odpowiedziami użytkowników.
15. Przechowywanie zabezpieczenia aplikacji kodem PIN.
16. Implementacja mechanizmu synchronizacji danych między urządzeniami z timestampami modyfikacji.
17. Dodanie pola języka do treści edukacyjnych dla przyszłej internacjonalizacji.
18. Dynamiczne obliczanie licznika dni trzeźwości z rozważeniem materialized views.

<matched_recommendations>

1. Rekomendacja tymczasowego ID urządzenia dla użytkowników niezalogowanych i mechanizmu migracji danych.
2. Rekomendacja minimalnego zestawu danych dla dziennych odpowiedzi z dodatkowymi polami dla lepszej analizy.
3. Rekomendacja wymuszenia ograniczeń odpowiedzi na poziomie bazy danych.
4. Rekomendacja struktury dla kamieni milowych trzeźwości z tabelami relacji osiągnięć.
5. Rekomendacja przechowywania treści edukacyjnych w bazie danych z odpowiednią kategoryzacją.
6. Rekomendacja mechanizmu rotacji treści poprzez śledzenie historii wyświetleń.
7. Rekomendacja struktury dla zasobów pomocowych z filtrowaniem geograficznym.
8. Rekomendacja mechanizmu wykrywania wzorców kryzysowych poprzez analizę historii.
9. Rekomendacja minimalizacji danych osobowych i implementacji Row Level Security.
10. Rekomendacja przechowywania dat w UTC z uwzględnieniem stref czasowych.
11. Rekomendacja tabel agregacji dla optymalizacji zapytań statystycznych.
12. Rekomendacja implementacji "reset bez wstydu" z zachowaniem historii.
13. Rekomendacja struktury dla ustawień powiadomień użytkownika.
14. Rekomendacja mechanizmu ankiet feedback'u.
15. Rekomendacja implementacji zabezpieczenia PIN-em.
16. Rekomendacja mechanizmu synchronizacji między urządzeniami.
17. Rekomendacja internacjonalizacji treści poprzez dodanie pola języka.
18. Rekomendacja dynamicznego obliczania statystyk z wykorzystaniem materialized views. </matched_recommendations>

<database_planning_summary> Na podstawie analizy dokumentu wymagań produktu (PRD) i odpowiedzi użytkownika, można zaplanować następującą strukturę bazy danych dla aplikacji MamKaca.pl:

### Główne encje:

1. __Użytkownicy (users)__

    - Podstawowa encja przechowująca dane użytkowników
    - Minimalna ilość danych osobowych (email, hasło)
    - Pole dla tymczasowego identyfikatora urządzenia dla migracji danych

2. __Urządzenia (devices)__

    - Identyfikator urządzenia
    - Powiązanie z użytkownikiem (opcjonalne)
    - Timestamp ostatniej synchronizacji

3. __Odpowiedzi (answers)__

    - ID użytkownika lub urządzenia
    - Data i czas odpowiedzi
    - Typ odpowiedzi (tak/nie/brak)
    - Źródło odpowiedzi (powiadomienie/ręczne)
    - Opcjonalna notatka
    - Timestamp modyfikacji

4. __Ustawienia (settings)__

    - ID użytkownika
    - Preferencje powiadomień (czas, włączone/wyłączone, format)
    - Strefa czasowa
    - Zabezpieczenie PIN-em (hash)
    - Ustawienia prywatności
    - Ustawienia widgetu

5. __Kamienie milowe (milestones)__

    - Predefiniowane kamienie milowe trzeźwości
    - Opis, wartość (dni), grafika

6. __Osiągnięcia użytkownika (user_achievements)__

    - Powiązanie użytkownika z osiągniętym kamieniem milowym
    - Data osiągnięcia

7. __Treści edukacyjne (educational_content)__

    - Typ treści (cytat, fakt, grafika)
    - Treść
    - Kategoria
    - Minimalny staż trzeźwości
    - Język

8. __Wyświetlone treści (displayed_content)__

    - Powiązanie użytkownika z treścią edukacyjną
    - Data wyświetlenia

9. __Zasoby pomocy (help_resources)__

    - Typ zasobu (telefon, grupa, technika)
    - Nazwa, opis
    - Dane kontaktowe
    - Region/kraj

10. __Wzorce kryzysowe (crisis_patterns)__

    - Definicja wzorca
    - Opis
    - Sugerowane działania

11. __Resety (resets)__

    - ID użytkownika
    - Data resetu
    - Opcjonalna notatka o przyczynach

12. __Ankiety (surveys)__

    - Definicja pytań
    - Okres wyświetlania

13. __Odpowiedzi na ankiety (survey_responses)__

    - Powiązanie użytkownika z ankietą
    - Odpowiedzi
    - Data wypełnienia

14. __Agregacje statystyk (stat_aggregations)__

    - ID użytkownika
    - Okres (dzień, tydzień, miesiąc, rok)
    - Agregowane wartości (liczba dni trzeźwych, procent)

### Relacje między encjami:

1. Użytkownik 1:N Urządzenia
2. Użytkownik 1:1 Ustawienia
3. Użytkownik/Urządzenie 1:N Odpowiedzi
4. Użytkownik 1:N Osiągnięcia
5. Kamień milowy 1:N Osiągnięcia
6. Użytkownik 1:N Wyświetlone treści
7. Treść edukacyjna 1:N Wyświetlone treści
8. Użytkownik 1:N Resety
9. Użytkownik 1:N Odpowiedzi na ankiety
10. Ankieta 1:N Odpowiedzi na ankiety
11. Użytkownik 1:N Agregacje statystyk

### Aspekty bezpieczeństwa i prywatności:

1. Implementacja Row Level Security (RLS) w Supabase, aby każdy użytkownik miał dostęp tylko do swoich danych
2. Minimalizacja zbieranych danych osobowych
3. Szyfrowanie wrażliwych danych
4. Haszowanie PINów i haseł
5. Automatyczne usuwanie nieaktywnych kont po określonym czasie

### Optymalizacja wydajności:

1. Indeksowanie kluczowych pól wykorzystywanych w zapytaniach (daty, ID użytkownika)
2. Materialized views dla często wykonywanych zapytań statystycznych
3. Przechowywanie zagregowanych danych dla szybkiego dostępu
4. Optymalizacja zapytań z wykorzystaniem specyficznych funkcji PostgreSQL

### Strategia synchronizacji danych:

1. Przechowywanie timestampów ostatniej modyfikacji dla każdego rekordu
2. Identyfikatory urządzeń dla śledzenia źródła danych
3. Mechanizm wykrywania i rozwiązywania konfliktów przy synchronizacji

### Skalowalność:

1. Projektowanie schematu z myślą o przyszłym rozszerzeniu (np. dodatkowe pola dla przyszłych funkcji)
2. Modularny design umożliwiający dodawanie nowych typów treści i funkcji
3. Wsparcie dla internacjonalizacji poprzez dodanie pól językowych </database_planning_summary>


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





