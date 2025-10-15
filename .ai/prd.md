# Dokument wymagań produktu (PRD) - mamkaca.pl

## 1. Przegląd produktu

MamKaca.pl to aplikacja mobilna wspierająca osoby w procesie wychodzenia z uzależnienia od alkoholu. Głównym celem aplikacji jest budowanie zdrowych nawyków poprzez codzienne śledzenie trzeźwości oraz dostarczanie wsparcia i porad w trudnych momentach. Aplikacja będzie dostępna na platformę Android (z planowanym rozszerzeniem na iOS w przyszłości), a jej główną funkcjonalnością będzie codzienne pytanie użytkownika "Masz dziś kaca?" z możliwością odpowiedzi "tak" lub "nie".

Kluczowe cechy produktu:
- Codzienne przypomnienia z pytaniem "Masz dziś kaca?"
- Proste śledzenie okresów trzeźwości
- Informacje edukacyjne o wpływie alkoholu na organizm i życie
- Wsparcie w momentach kryzysu
- Wizualizacja postępów i osiągnięć
- Pełna prywatność i anonimowość

Aplikacja ma charakter edukacyjny i wspierający. Nie zastępuje profesjonalnej pomocy medycznej czy psychologicznej, ale może stanowić codzienne narzędzie motywacyjne w procesie zdrowienia.

## 2. Problem użytkownika

Uzależnienie od alkoholu dotyka miliony osób na całym świecie. Według danych, w Polsce problem ten może dotyczyć nawet 2,5 miliona osób. Droga do trzeźwości jest długa i pełna wyzwań, a powroty do nałogu są częstym zjawiskiem. Główne problemy, z którymi mierzą się osoby wychodzące z uzależnienia to:

1. Brak codziennego wsparcia i motywacji pomiędzy spotkaniami terapeutycznymi
2. Trudności w śledzeniu własnych postępów
3. Ograniczony dostęp do informacji edukacyjnych dostosowanych do ich etapu zdrowienia
4. Momenty kryzysu, w których potrzebne jest natychmiastowe wsparcie
5. Stygmatyzacja społeczna i wstyd związany z przyznaniem się do problemu
6. Brak prostych narzędzi pomagających budować nawyk trzeźwości

MamKaca.pl adresuje te problemy poprzez:
- Codzienną interakcję, która buduje nawyk refleksji nad swoim stanem
- Proste i przyjazne śledzenie okresów trzeźwości bez osądzania
- Dostarczanie rotacyjnych treści edukacyjnych dostosowanych do etapu zdrowienia
- Natychmiastowy dostęp do zasobów pomocy w momentach kryzysu
- Pełną anonimowość i prywatność, która eliminuje barierę wstydu
- Elementy humorystyczne i motywacyjne, które czynią proces zdrowienia mniej przytłaczającym

## 3. Wymagania funkcjonalne

### 3.1. Codzienne przypomnienia

- System będzie wysyłać codzienne powiadomienie z pytaniem "Masz dziś kaca?"
- Domyślna pora powiadomienia to 9:00 rano
- Użytkownik będzie mógł dostosować porę powiadomień w ustawieniach
- Na powiadomienie można odpowiedzieć tylko raz dziennie (tak/nie)
- Nie będzie możliwości zmiany odpowiedzi po jej udzieleniu

### 3.2. System odpowiedzi i reakcji

- Po odpowiedzi "tak":
  - Aplikacja wyświetli informacje o dostępnej pomocy
  - Pokaże motywującą grafikę
  - Zaoferuje krótkie, wspierające treści
  - Opcjonalnie wyświetli elementy humorystyczne (bez gloryfikacji picia)
  - Zresetuje licznik dni trzeźwości

- Po odpowiedzi "nie":
  - Aplikacja wyświetli pochwałę i gratulacje
  - Pokaże motywujący cytat
  - Zaktualizuje i wyświetli licznik dni trzeźwości
  - Przedstawi rotacyjną informację edukacyjną o alkoholu

### 3.3. Treści edukacyjne

- Biblioteka minimum 100 różnych cytatów/faktów/grafik na start
- Rotacyjny system treści, by unikać powtórzeń
- Treści dostosowane do "stażu trzeźwościowego" użytkownika
- Sezonowe treści związane ze świętami lub wydarzeniami wysokiego ryzyka
- Wszystkie treści weryfikowane przez ekspertów (psychologów, terapeutów uzależnień)

### 3.4. Konta użytkowników

- Rejestracja konta jest opcjonalna
- Możliwość korzystania z aplikacji bez logowania
- Zalogowani użytkownicy mają dostęp do:
  - Historii odpowiedzi
  - Rozszerzonych statystyk
  - Synchronizacji danych między urządzeniami
- Proces rejestracji prosty, maksymalnie 3-krokowy
- Możliwość całkowitego usunięcia konta i wszystkich danych

### 3.5. Statystyki i wizualizacje

- Podstawowe statystyki dostępne dla wszystkich:
  - Aktualny licznik dni trzeźwości
  - Najdłuższy okres trzeźwości
  - Procent dni trzeźwych w bieżącym miesiącu

- Rozszerzone statystyki dla zalogowanych:
  - Historia odpowiedzi w formie kalendarza
  - Wizualizacja oszczędności finansowych
  - Informacje o poprawie zdrowia (np. regeneracja wątroby)
  - Wskaźnik nawrotów
  - "Kamienie milowe" trzeźwości

### 3.6. System wsparcia kryzysowego

- Algorytm wykrywający wzorce sugerujące kryzys
- Dodatkowe zasoby wsparcia w momentach kryzysu
- Numery telefonów zaufania z możliwością bezpośredniego połączenia
- Informacje o grupach wsparcia w pobliżu
- Techniki radzenia sobie z głodem alkoholowym
- Funkcja "reset bez wstydu" po nawrocie

### 3.7. Prywatność i bezpieczeństwo

- Minimalizacja zbieranych danych osobowych
- Pełne szyfrowanie danych w spoczynku i podczas transmisji
- Prosta, przejrzysta polityka prywatności
- Automatyczne usuwanie nieaktywnych kont po 12 miesiącach
- Brak funkcji społecznościowych, które mogłyby naruszać prywatność
- Możliwość ukrycia lub zabezpieczenia aplikacji hasłem

### 3.8. Interfejs użytkownika

- Prosty, intuicyjny interfejs
- Lekki, szybki w działaniu
- Możliwość działania offline
- Opcjonalny widget na ekran główny pokazujący liczbę dni trzeźwości
- Subtelne różnice w komunikacji zależne od stażu użytkownika

### 3.9. System feedback'u

- Lekki system ankiet in-app (max 2-3 pytania) pojawiający się raz na 30 dni
- Stała opcja wysłania anonimowej sugestii
- Prosty system raportowania błędów
- FAQ z odpowiedziami na najczęstsze pytania
- Dedykowany adres e-mail do kontaktu

## 4. Granice produktu

### 4.1. Co jest częścią produktu

- Aplikacja mobilna na system Android (MVP)
- Strona internetowa z informacją o aplikacji i linkiem do pobrania
- System codziennych powiadomień i śledzenia trzeźwości
- Podstawowe statystyki i wizualizacje
- Treści edukacyjne o alkoholu i uzależnieniu
- System wsparcia w kryzysie
- Opcjonalne konto użytkownika z rozszerzonymi funkcjami

### 4.2. Co nie jest częścią produktu

- Profesjonalna terapia uzależnień
- Diagnostyka medyczna
- Funkcje społecznościowe/grupy wsparcia online
- Integracja z zewnętrznymi systemami opieki zdrowotnej
- Automatyczna synchronizacja z terapeutą
- Kalendarz spotkań grup AA (jedynie informacje gdzie szukać)
- Funkcje związane z innymi uzależnieniami (w wersji MVP)

### 4.3. Przyszłe rozszerzenia (poza MVP)

- Wersja na iOS
- Zaawansowany system personalizacji treści
- Rozszerzone funkcje statystyczne
- Możliwość udostępniania statystyk terapeucie
- Rozszerzenie na inne uzależnienia
- Integracja z urządzeniami monitorującymi zdrowie
- System nagród i motywacji

### 4.4. Ograniczenia techniczne

- Aplikacja musi działać na urządzeniach z Androidem 8.0 i nowszych
- Minimalne zużycie baterii i danych
- Pełna funkcjonalność offline z opcjonalną synchronizacją
- Maksymalny rozmiar aplikacji: 50MB
- Minimalne wymagania sprzętowe

## 5. Historyjki użytkowników

### Onboarding i konfiguracja

#### US-001 Pierwszy uruchomienie aplikacji
Jako nowy użytkownik, chcę przejść przez prosty proces wprowadzający, abym mógł szybko zrozumieć, jak działa aplikacja.

Kryteria akceptacji:
- Po pierwszym uruchomieniu aplikacji, użytkownik widzi maksymalnie 3 ekrany wprowadzające
- Na ekranach wyświetlane są kluczowe informacje o funkcjonalności aplikacji
- Użytkownik może przejść dalej dotykając przycisk "Dalej" lub przewijając ekrany
- Na ostatnim ekranie znajduje się przycisk "Rozpocznij"
- Proces onboardingu można pominąć poprzez przycisk "Pomiń" (widoczny na każdym ekranie)

#### US-002 Konfiguracja powiadomień
Jako użytkownik aplikacji, chcę ustawić preferowaną porę otrzymywania codziennych powiadomień, aby dopasować aplikację do mojego rytmu dnia.

Kryteria akceptacji:
- Domyślna pora powiadomień jest ustawiona na 9:00 rano
- Użytkownik może zmienić godzinę powiadomień w ustawieniach
- System wyświetla potwierdzenie po zmianie ustawień
- Zmiana zaczyna obowiązywać od następnego dnia
- Użytkownik może wyłączyć powiadomienia (z ostrzeżeniem o konsekwencjach)

#### US-003 Tworzenie konta (opcjonalne)
Jako użytkownik, chcę móc utworzyć konto, aby zachować swoje dane i statystyki.

Kryteria akceptacji:
- Tworzenie konta jest opcjonalne i dostępne z menu "Ustawienia"
- Do rejestracji wymagany jest adres e-mail i hasło
- System weryfikuje poprawność adresu e-mail
- Hasło musi spełniać minimalne wymogi bezpieczeństwa (min. 8 znaków, jedna wielka litera, jedna cyfra)
- Po rejestracji użytkownik otrzymuje e-mail z potwierdzeniem
- Użytkownik może korzystać z aplikacji przed potwierdzeniem e-maila

#### US-004 Logowanie do konta
Jako zarejestrowany użytkownik, chcę móc zalogować się do swojego konta, aby uzyskać dostęp do swoich danych na różnych urządzeniach.

Kryteria akceptacji:
- Użytkownik może zalogować się używając adresu e-mail i hasła
- Dostępna jest opcja "Zapamiętaj mnie"
- System oferuje opcję resetowania zapomnianego hasła
- Po zalogowaniu dane są synchronizowane z serwerem
- Użytkownik pozostaje zalogowany aż do wylogowania

#### US-005 Usunięcie konta i danych
Jako użytkownik z kontem, chcę móc usunąć swoje konto i wszystkie powiązane dane, aby chronić swoją prywatność.

Kryteria akceptacji:
- Opcja usunięcia konta jest dostępna w ustawieniach
- Przed usunięciem system wymaga potwierdzenia
- System informuje o konsekwencjach usunięcia konta
- Po usunięciu wszystkie dane użytkownika są trwale kasowane z serwerów
- Użytkownik otrzymuje potwierdzenie usunięcia konta na adres e-mail

### Codzienna interakcja

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

#### US-010 Dostęp do rotacyjnych treści edukacyjnych
Jako użytkownik aplikacji, chcę codziennie otrzymywać nowe, interesujące informacje o alkoholu i trzeźwości, aby pogłębiać swoją wiedzę i motywację.

Kryteria akceptacji:
- Codziennie wyświetlana jest nowa informacja edukacyjna
- Treści nie powtarzają się przez co najmniej 30 dni
- Informacje są krótkie i łatwe do przyswojenia (max 2-3 zdania)
- Treści są dostosowane do "stażu trzeźwościowego" użytkownika
- Dostępna jest opcja "Pokaż więcej", prowadząca do rozszerzonej informacji

### Śledzenie postępów i statystyki

#### US-011 Przeglądanie podstawowych statystyk
Jako użytkownik aplikacji, chcę widzieć podstawowe statystyki mojej trzeźwości, aby śledzić swoje postępy.

Kryteria akceptacji:
- Na głównym ekranie widoczny jest aktualny licznik dni trzeźwości
- Dostępna jest informacja o najdłuższym okresie trzeźwości
- Wyświetlany jest procent dni trzeźwych w bieżącym miesiącu
- Statystyki są aktualizowane po każdej odpowiedzi
- Podstawowe statystyki są dostępne także dla niezalogowanych użytkowników

#### US-012 Przeglądanie rozszerzonych statystyk (dla zalogowanych)
Jako zalogowany użytkownik, chcę mieć dostęp do rozszerzonych statystyk, aby lepiej analizować swoje postępy.

Kryteria akceptacji:
- Dostępna jest historia odpowiedzi w formie kalendarza
- System pokazuje wizualizację oszczędności finansowych
- Prezentowane są informacje o poprawie zdrowia
- Dostępny jest wykres trendów trzeźwości
- Możliwe jest filtrowanie statystyk według okresów (tydzień, miesiąc, rok)

#### US-013 Osiąganie "kamieni milowych" trzeźwości
Jako użytkownik, chcę otrzymywać powiadomienia o osiągnięciu ważnych etapów w mojej trzeźwości, aby celebrować swoje osiągnięcia.

Kryteria akceptacji:
- System rozpoznaje i powiadamia o osiągnięciu kamieni milowych (1 dzień, 1 tydzień, 1 miesiąc, 100 dni, 1 rok, itd.)
- Przy osiągnięciu kamienia milowego wyświetlana jest specjalna grafika i komunikat
- Informacja o osiągnięciach jest zapisywana w historii
- Możliwe jest przeglądanie zdobytych osiągnięć w dedykowanej sekcji
- System pokazuje nadchodzące kamienie milowe jako motywację

### Wsparcie w kryzysie

#### US-014 Wykrywanie wzorców kryzysowych
Jako użytkownik aplikacji, chcę, aby system rozpoznawał potencjalne sytuacje kryzysowe na podstawie moich odpowiedzi, aby zaoferować mi dodatkowe wsparcie.

Kryteria akceptacji:
- System wykrywa wzorce sugerujące kryzys (np. kilka dni z rzędu odpowiedzi "tak" po długim okresie abstynencji)
- Po wykryciu wzorca kryzysowego aplikacja wyświetla specjalny komunikat wspierający
- Oferowane są dodatkowe zasoby pomocy
- Użytkownik może odrzucić lub przyjąć dodatkowe wsparcie
- System nie jest nachalny, zachowując balans między wsparciem a prywatnością

#### US-015 Dostęp do zasobów pomocy
Jako użytkownik aplikacji, chcę mieć łatwy dostęp do zasobów pomocy, aby móc skorzystać z nich w momentach kryzysu.

Kryteria akceptacji:
- Sekcja "Pomoc" jest dostępna z głównego menu aplikacji
- Lista zawiera aktualne numery telefonów zaufania
- Możliwe jest bezpośrednie połączenie z infolinią pomocy
- Dostępna jest lista lokalnych grup wsparcia
- Prezentowane są techniki radzenia sobie z głodem alkoholowym
- Zasoby są regularnie aktualizowane

#### US-016 Funkcja "Reset bez wstydu"
Jako użytkownik, który doświadczył nawrotu, chcę otrzymać wsparcie bez osądzania, aby móc ponownie rozpocząć proces trzeźwienia.

Kryteria akceptacji:
- Po serii odpowiedzi "tak" system oferuje funkcję "Reset bez wstydu"
- Komunikaty są wspierające, bez stygmatyzacji
- System pomaga w analizie wyzwalaczy i okoliczności nawrotu
- Możliwe jest dodanie notatki o przyczynach nawrotu (opcjonalnie)
- Po resecie system oferuje zwiększone wsparcie przez następne dni

### Ustawienia i personalizacja

#### US-017 Zarządzanie ustawieniami aplikacji
Jako użytkownik aplikacji, chcę móc dostosować jej ustawienia, aby aplikacja lepiej odpowiadała moim potrzebom.

Kryteria akceptacji:
- Sekcja "Ustawienia" jest dostępna z głównego menu
- Możliwa jest zmiana czasu powiadomień
- Dostępna jest opcja włączenia/wyłączenia widgetu
- Możliwe jest dostosowanie elementów humorystycznych (więcej/mniej/wyłącz)
- Ustawienia są zapisywane natychmiast po zmianie

#### US-018 Personalizacja kosztów alkoholu
Jako użytkownik, chcę móc podać średni koszt alkoholu, który spożywałem, aby widzieć realistyczne oszczędności finansowe.

Kryteria akceptacji:
- W ustawieniach dostępna jest opcja "Średnie wydatki na alkohol"
- Użytkownik może wprowadzić kwotę dzienną/tygodniową/miesięczną
- System przelicza wprowadzoną wartość na oszczędności podczas trzeźwości
- Dostępna jest opcja waluty
- Jeśli użytkownik nie poda własnej wartości, system korzysta z uśrednionej wartości domyślnej

### Prywatność i bezpieczeństwo

#### US-019 Zabezpieczenie dostępu do aplikacji
Jako użytkownik aplikacji dotyczącej wrażliwego tematu, chcę móc zabezpieczyć dostęp do aplikacji, aby chronić swoją prywatność.

Kryteria akceptacji:
- W ustawieniach dostępna jest opcja zabezpieczenia aplikacji kodem PIN
- Możliwe jest ukrycie ikony aplikacji lub zmiana jej nazwy
- System oferuje opcję "dyskretne powiadomienia" (bez wspominania o alkoholu)
- Po włączeniu zabezpieczeń, każde uruchomienie aplikacji wymaga podania PIN-u
- Dostępna jest opcja resetu PIN-u przez e-mail (dla zalogowanych)

#### US-020 Zarządzanie swoimi danymi
Jako użytkownik, chcę mieć pełną kontrolę nad swoimi danymi, aby chronić swoją prywatność.

Kryteria akceptacji:
- W ustawieniach dostępna jest sekcja "Moje dane"
- Użytkownik może wyeksportować swoje dane w formacie JSON
- Możliwe jest usunięcie wszystkich lokalnych danych
- Dla zalogowanych użytkowników dostępna jest opcja usunięcia danych z serwera
- System pokazuje, jakie dokładnie dane są przechowywane

### Feedback i wsparcie techniczne

#### US-021 Zgłaszanie błędów i sugestii
Jako użytkownik aplikacji, chcę móc zgłaszać błędy i sugestie, aby przyczynić się do ulepszania aplikacji.

Kryteria akceptacji:
- W aplikacji dostępny jest formularz zgłaszania błędów
- Możliwe jest wysłanie anonimowej sugestii
- System potwierdza otrzymanie zgłoszenia
- Użytkownik może dołączyć zrzut ekranu do zgłoszenia (opcjonalnie)
- Formularz jest prosty i nie wymaga podawania danych osobowych

#### US-022 Wypełnianie okresowej ankiety
Jako użytkownik, chcę mieć możliwość wypełnienia krótkiej ankiety dotyczącej aplikacji, aby podzielić się swoimi doświadczeniami.

Kryteria akceptacji:
- Raz na 30 dni aktywnego korzystania system proponuje wypełnienie ankiety
- Ankieta zawiera maksymalnie 2-3 pytania
- Wypełnienie ankiety jest opcjonalne (można odrzucić lub odłożyć)
- Dane z ankiety są anonimowe
- Po wypełnieniu ankiety użytkownik otrzymuje podziękowanie

#### US-023 Przeglądanie FAQ
Jako użytkownik aplikacji, chcę mieć dostęp do sekcji FAQ, aby znaleźć odpowiedzi na najczęstsze pytania bez kontaktowania się z supportem.

Kryteria akceptacji:
- Sekcja FAQ jest dostępna z menu głównego lub ustawień
- Pytania są pogrupowane tematycznie
- Możliwe jest wyszukiwanie w FAQ
- Każde pytanie ma rozwijaną odpowiedź
- Na końcu każdej odpowiedzi jest pytanie "Czy ta odpowiedź była pomocna?"

### Funkcje dodatkowe

#### US-024 Korzystanie z widgetu na ekranie głównym
Jako użytkownik aplikacji, chcę mieć możliwość dodania widgetu na ekran główny telefonu, aby łatwo śledzić dni trzeźwości.

Kryteria akceptacji:
- Aplikacja oferuje widget na ekran główny
- Widget pokazuje aktualną liczbę dni trzeźwości
- Dotknięcie widgetu otwiera aplikację
- Widget jest dostępny w kilku rozmiarach
- Wygląd widgetu można dostosować (minimalistyczny/standardowy)

#### US-025 Działanie aplikacji offline
Jako użytkownik aplikacji, chcę, aby działała ona w pełni offline, abym mógł korzystać z niej nawet bez dostępu do internetu.

Kryteria akceptacji:
- Wszystkie podstawowe funkcje działają bez połączenia z internetem
- Odpowiedzi są zapisywane lokalnie
- Statystyki są obliczane na urządzeniu
- Po przywróceniu połączenia dane są synchronizowane z serwerem (dla zalogowanych)
- System informuje o brakującej synchronizacji dyskretnym wskaźnikiem

## 6. Metryki sukcesu

### 6.1. Metryki użytkownika

- Liczba dni bez kaca (trzeźwości)
  - Cel: Wzrost średniej liczby dni trzeźwości na użytkownika o 15% co kwartał
  - Pomiar: Średnia liczba dni trzeźwości wszystkich użytkowników

- Procent dni trzeźwych w miesiącu/roku
  - Cel: Osiągnięcie średnio 70% dni trzeźwych w miesiącu dla aktywnych użytkowników
  - Pomiar: (Liczba odpowiedzi "nie" / Liczba wszystkich odpowiedzi) * 100%

- Wskaźnik nawrotów
  - Cel: Zmniejszenie wskaźnika nawrotów o 5% co kwartał
  - Pomiar: Procent użytkowników, którzy po osiągnięciu 14+ dni trzeźwości, odpowiedzieli "tak"

- Subiektywna ocena dobrostanu
  - Cel: 75% użytkowników raportuje poprawę dobrostanu po 30 dniach korzystania
  - Pomiar: Wyniki ankiety po 30 dniach korzystania z aplikacji

### 6.2. Metryki produktowe

- Daily Active Users (DAU)
  - Cel: 1000 DAU po 3 miesiącach, wzrost o 20% kwartalnie
  - Pomiar: Liczba unikalnych użytkowników odpowiadających na pytanie dziennie

- Retencja 7/30 dni
  - Cel: 60% retencji po 7 dniach, 40% retencji po 30 dniach
  - Pomiar: Procent użytkowników, którzy wracają do aplikacji po 7/30 dniach od pierwszego użycia

- Procent użytkowników odpowiadających na codzienne przypomnienie
  - Cel: 80% użytkowników odpowiada na przypomnienie
  - Pomiar: (Liczba odpowiedzi / Liczba wysłanych powiadomień) * 100%

- Procent użytkowników, którzy osiągnęli 7 kolejnych dni abstynencji
  - Cel: 50% nowych użytkowników osiąga 7 dni abstynencji w pierwszym miesiącu
  - Pomiar: Liczba użytkowników z 7+ dniami trzeźwości / Całkowita liczba użytkowników

### 6.3. Metryki techniczne

- Czas odpowiedzi aplikacji
  - Cel: 95% interakcji z czasem odpowiedzi poniżej 1 sekundy
  - Pomiar: Czas między akcją użytkownika a reakcją interfejsu

- Awarie aplikacji
  - Cel: Mniej niż 1 awaria na 1000 sesji
  - Pomiar: Liczba raportowanych awarii aplikacji / Liczba sesji użytkownika

- Zużycie baterii
  - Cel: Zużycie baterii poniżej 2% dziennie przy standardowym użytkowaniu
  - Pomiar: Średnie zużycie baterii przez aplikację na podstawie danych systemowych

- Rozmiar aplikacji
  - Cel: Utrzymanie rozmiaru aplikacji poniżej 50MB
  - Pomiar: Rozmiar instalacji aplikacji monitorowany przy każdej aktualizacji

- Czas synchronizacji
  - Cel: Synchronizacja danych z serwerem w czasie poniżej 3 sekund
  - Pomiar: Czas potrzebny na synchronizację danych użytkownika z serwerem
