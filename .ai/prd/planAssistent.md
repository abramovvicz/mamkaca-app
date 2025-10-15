# Pytania i zalecenia do dokumentu wymagań projektowych (PRD) dla MamKaca.pl

## Round 1:
1. Aplikacja Android z codziennym przypomnieniem: „Masz dziś kaca?” – użytkownik odpowiada „tak” lub „nie” (tylko raz dziennie, bez cofania).

   „Tak” → informacje o pomocy, motywująca grafika.

   „Nie” → pochwała, motywujący cytat, licznik np. „100 dni trzeźwości”.

2. Grupa docelowa: osoby na różnych etapach zdrowienia, z naciskiem na początkujących i młodzież. Aplikacja ma edukować o alkoholu i zawierać elementy humorystyczne.

3. Sukces: brak kaca danego dnia. Statystyki: liczba dni trzeźwych w miesiącu/roku, procent utrzymujących abstynencję, wskaźnik nawrotów, poziom dobrostanu.

4. Konto: dobrowolne. Aplikacja darmowa i anonimowa, z pełną ochroną prywatności. Brak opcji udostępniania danych. Zalogowani widzą historię i statystyki.

5. Współpraca z ekspertami od uzależnień i grupami wsparcia, zapewnienie rzetelnych treści. Personalizacja zależnie od etapu zdrowienia. Telefony zaufania i szybkie połączenie z infolinią.

6. System osiągnięć: nie tylko licznik dni, ale też nowe nawyki, wizualizacja postępów (oszczędności, zdrowie). W przyszłości: wsparcie społecznościowe i rozszerzenie o inne uzależnienia.

7. Brak kalendarza spotkań – tylko informacje, gdzie szukać pomocy. Możliwość pokazania statystyk terapeucie.

8. Funkcja „reset bez wstydu” – zamiast kary za nawrót: wsparcie, analiza wyzwalaczy, wzmocnienie motywacji. Nawroty to część procesu.

9. Bez funkcji społecznościowych – temat jest delikatny. Aplikacja ma być lekka, szybka i prosta – do codziennego monitorowania nawyku.

10. Rozwój iteracyjny z anonimowym feedbackiem i priorytetyzacją funkcji o największym wpływie na utrzymanie trzeźwości.

## Round 2:

1. Domyślne przypomnienie na godzinę 9:00 rano ale pozwolę użytkownikom na personalizację czasu powiadomienia w ustawieniach, co zwiększy szansę na regularną interakcję.
2. Stwórz bibliotekę krótkich, opartych na faktach informacji (tzw. "czy wiesz, że..."), które będą pokazywane rotacyjnie jako element codziennej interakcji.
   Informacje powinny być zróżnicowane: od medycznych efektów alkoholu, przez ekonomiczne (koszt picia), po społeczne.
3. Jakie konkretnie elementy humorystyczne planujesz zawrzeć i jak zapewnisz, że będą one odpowiednie dla szerokiego grona odbiorców?
   Chciałbym aby po zagłosowaniu na tak były pochlebne cytaty i obrazki. Po glosowaniu negatywnie raczej z lekkim humorem "o dzisiaj też się nie udało no trudno jutro już mi się uda! i może jakaś śmieszna grafika ze zmęczonym gościem. Unikanie żartów, które mogą być odczytane jako gloryfikacja picia lub stygmatyzujące.
4. Progresywny system komunikatów motywacyjnych, dostosowanych do "wieku trzeźwościowego" użytkownika. Początkujący powinni otrzymywać więcej podstawowych informacji i zachęt, podczas gdy długoterminowi użytkownicy - bardziej zaawansowane treści i uznanie za konsekwencję. Ale to na dalszym etapie rozwoju aplikacji. Na ten moment ma byc prosta.
5. Nawiąż formalną współpracę z 2-3 ekspertami (psycholog, terapeuta uzależnień, były uzależniony z długim stażem trzeźwości) jako panel recenzencki dla wszystkich treści aplikacji, z regularnym procesem aktualizacji i weryfikacji materiałów.
6. Oprócz podstawowych statystyk (dni trzeźwości), dodaj wizualizację oszczędności finansowych (bazując na średnim koszcie alkoholu podanym przez użytkownika lub wzietym z internetu), poprawę zdrowia (np. regeneracja wątroby, poprawa jakości snu) oraz niestandardowe "kamienie milowe" trzeźwości.
7. Stwórz prosty, maxymalnie 3-krokowy proces onboardingowy, który wyjaśnia wartość codziennego śledzenia, zapewnia o prywatności danych i pozwala na minimalne, opcjonalne spersonalizowanie aplikacji (np. wybór pory powiadomień, typowego kosztu alkoholu, ile dni bez alkoholu jestem ale potrzebuje dalszego wsparcia?).
8. Przygotuj zróżnicowane zasoby pomocy: od natychmiastowych (numery telefonów zaufania, lokalizacja najbliższych grup AA) po edukacyjne (techniki radzenia sobie z głodem alkoholowym, wyjaśnienie mechanizmów uzależnienia) oraz motywacyjne (historie sukcesu, przypomnienie o dotychczasowych osiągnięciach).
9. Zaimplementuj lekki system ankiet in-app (max 2-3 pytania) pojawiający się raz na 30 dni korzystania, plus stałą opcję wysłania anonimowej sugestii. Stwórz przejrzysty proces analizy feedbacku i uwzględniania go w planach rozwojowych.
10. Zaprojektuj aplikację do pełnego działania offline z okresową synchronizacją danych dla użytkowników z kontem. Zminimalizuj zużycie baterii poprzez ograniczenie aktywności w tle tylko do niezbędnych powiadomień. Rozważ implementację lekkiego widgetu na ekran główny pokazującego liczbę dni trzeźwości. (opcjonalnie)


## Round 3:

1. Skup się na 3-5 kluczowych metrykach, które są mierzalne i bezpośrednio powiązane z głównym celem aplikacji: (1) Daily Active Users, (2) retencja 7/30 dni, (3) procent użytkowników odpowiadających na codzienne przypomnienie, (4) procent użytkowników, którzy osiągnęli 7 kolejnych dni abstynencji. Te metryki pozwolą ocenić, czy aplikacja faktycznie pomaga użytkownikom w budowaniu nawyku trzeźwości.
2. Zaimplementuj anonimowe zbieranie danych zagregowanych (bez możliwości identyfikacji konkretnych użytkowników), z wyraźną informacją dla użytkowników o tym, jakie dane są zbierane i w jakim celu. Rozważ opcjonalną ankietę po 30 dniach używania aplikacji, pytającą o subiektywną ocenę wpływu aplikacji na proces zdrowienia. Dodatkowo można zaimplementować kasowanie wszelkiej histori i danych.
3. Na ten moment będzie to aplikacja dostępna dla wszystkich bez reklamy a wspołpraca współpraca z ośrodkami leczenia uzależnień i terapeutami bedzie może pozniej? ,  dyskretna obecność w mediach społecznościowych z edukacyjnymi treściami o zdrowiu,  ozycjonowanie w wyszukiwarkach pod kątem haseł związanych z pomocą przy problemach z alkoholem
4. Nawet w prostej wersji MVP wprowadź subtelne różnice w komunikacji z użytkownikiem w zależności od jego "stażu": inna tonacja komunikatów, różne zestawy cytatów motywacyjnych i grafik, oraz dostosowana edukacja. Interfejs powinien pozostać spójny dla wszystkich, ale treść może się dynamicznie dostosowywać ze względu na staż i historię statystyk.
5. Oprócz podstawowego numeru telefonu zaufania, rozważ implementację prostego algorytmu wykrywającego wzorce sugerujące kryzys (np. kilka dni z rzędu odpowiedzi "tak" po długim okresie abstynencji) i oferującego dodatkowe zasoby wsparcia w takiej sytuacji. Zawsze priorytetyzuj bezpieczeństwo użytkownika.
6. Stwórz system rotacji treści z biblioteką minimum 100 różnych cytatów/faktów/grafik na start, z planem regularnego dodawania nowych (np. 10 nowych miesięcznie). Rozważ również sezonowe treści związane ze świętami lub wydarzeniami, które tradycyjnie wiążą się z większym spożyciem alkoholu.
7. front strona internetowa ktora bedzie pozwala na przekieorwanie do google play (pozniej apple store) z informacjami pomocnymi dla uzaleznionych, rejestracja z autoryzacją, na poczatek mała ilośc uzyutkwonikow
8. drożenie minimalnego zbierania danych (zasada minimalizacji danych), pełne szyfrowanie danych użytkownika w spoczynku i podczas transmisji, przejrzysta polityka prywatności napisana prostym językiem, oraz automatyczne usuwanie nieaktywnych kont po 12 miesiącach braku aktywności.
9. Opracuj szczegółowy budżet uwzględniający koszty hostingu, rozwoju, moderacji treści i marketingu. Rozważ model freemium z podstawowymi funkcjonalnościami dostępnymi bezpłatnie, a dodatkowymi (np. zaawansowane statystyki, personalizowane plany) w wersji płatnej, lub model sponsoringu przez organizacje zajmujące się profilaktyką uzależnień.
10. Stwórz prosty system raportowania błędów w aplikacji, FAQ odpowiadające na najczęstsze pytania oraz dedykowany adres e-mail do kontaktu. Ze względu na wrażliwość tematu, upewnij się, że wszystkie interakcje związane ze wsparciem technicznym są dyskretne i szanują prywatność użytkownika.

---

Jesteś asystentem AI, którego zadaniem jest podsumowanie rozmowy na temat planowania PRD (Product Requirements Document) dla MVP i przygotowanie zwięzłego podsumowania dla następnego etapu rozwoju. W historii konwersacji znajdziesz następujące informacje:
1. Opis projektu
2. Zidentyfikowany problem użytkownika
3. Historia rozmów zawierająca pytania i odpowiedzi
4. Zalecenia dotyczące zawartości PRD

Twoim zadaniem jest:
1. Podsumować historię konwersacji, koncentrując się na wszystkich decyzjach związanych z planowaniem PRD.
2. Dopasowanie zaleceń modelu do odpowiedzi udzielonych w historii konwersacji. Zidentyfikuj, które zalecenia są istotne w oparciu o dyskusję.
3. Przygotuj szczegółowe podsumowanie rozmowy, które obejmuje:
   a. Główne wymagania funkcjonalne produktu
   b. Kluczowe historie użytkownika i ścieżki korzystania
   c. Ważne kryteria sukcesu i sposoby ich mierzenia
   d. Wszelkie nierozwiązane kwestie lub obszary wymagające dalszego wyjaśnienia
4. Sformatuj wyniki w następujący sposób:

<conversation_summary>
<decisions>
[Wymień decyzje podjęte przez użytkownika, ponumerowane].
</decisions>

<matched_recommendations>
[Lista najistotniejszych zaleceń dopasowanych do rozmowy, ponumerowanych]
</matched_recommendations>

<prd_planning_summary>
[Podaj szczegółowe podsumowanie rozmowy, w tym elementy wymienione w kroku 3].
</prd_planning_summary>

<unresolved_issues>
[Wymień wszelkie nierozwiązane kwestie lub obszary wymagające dalszych wyjaśnień, jeśli takie istnieją]
</unresolved_issues>
</conversation_summary>

Końcowy wynik powinien zawierać tylko treść w formacie markdown. Upewnij się, że Twoje podsumowanie jest jasne, zwięzłe i zapewnia cenne informacje dla następnego etapu tworzenia PRD.


## Summary:

# Podsumowanie rozmowy planowania PRD dla aplikacji MamKaca.pl

<conversation_summary>

1. Aplikacja będzie oferować codzienne przypomnienie "Masz dziś kaca?" z odpowiedziami "tak/nie" raz dzienne.
2. Domyślna pora przypomnienia to 9:00 rano, z możliwością personalizacji przez użytkownika.
3. Odpowiedź "tak" będzie skutkować wyświetleniem informacji pomocowych i motywującej grafiki.
4. Odpowiedź "nie" będzie skutkować pochwałą, motywującym cytatem i licznikiem dni trzeźwości.
5. Aplikacja będzie zawierać elementy humorystyczne, ale unikające gloryfikacji picia. Szczególnie aby zachęcić do korzystania przez młodzież w celu edukacji.
6. Konto użytkownika będzie opcjonalne, a aplikacja będzie darmowa i anonimowa.
7. Zostanie zaimplementowana rotacyjna biblioteka krótkich, faktycznych informacji o alkoholu.
8. Zalogowani użytkownicy będą mieć dostęp do historii i statystyk.
9. Zostaną wdrożone różne zasoby pomocy: numery telefonów zaufania, informacje edukacyjne.
10. System będzie wykrywać wzorce sugerujące kryzys i oferować dodatkowe wsparcie.
11. Dane będą zbierane anonimowo z możliwością ich całkowitego usunięcia.
12. Interfejs pozostanie spójny, ale treści będą dostosowane do "stażu trzeźwościowego".
13. Aplikacja będzie działać offline ale i online. Będzie miała możliwość synchronizacji danych dla zalogowanych.
14. Na start zostanie stworzona biblioteka minimum 100 cytatów/faktów/grafik.
15. Wdrożona zostanie pełna ochrona prywatności zgodna z RODO.
16. System będzie zawierać prosty mechanizm raportowania błędów i FAQ.

<matched_recommendations>

1. Wdrożenie domyślnego przypomnienia o 9:00 rano z opcją personalizacji czasu powiadomień.
2. Wdożenie mechanizmu głosowania "czy masz kaca?" z odpowiedzią tak/nie.
3. Dodanie statystyk ilości osob majacych kaca danego dnia/msc/roku i innych.
4. Stworzenie biblioteki rotacyjnych, krótkich informacji edukacyjnych o alkoholu.
5. Zaprojektowanie prostego, maksymalnie 3-krokowego procesu onboardingowego.
6. Wprowadzenie wizualizacji oszczędności finansowych i poprawy zdrowia w statystykach.
7. Dodanie algorytmu wykrywającego wzorce sugerujące kryzys po okresie abstynencji.
8. Implementacja systemu rotacji treści (minimum 100 różnych elementów na start).
9. Wdrożenie minimalnego zbierania danych z pełnym szyfrowaniem i prostą polityką prywatności.
10. Wprowadzenie subtelnych różnic w komunikacji zależnych od "stażu trzeźwościowego".
11. Skupienie się na 3-5 kluczowych metrykach: DAU, retencja, odpowiadanie na przypomnienia, osiągnięcia.
12. Zaprojektowanie aplikacji do pełnego działania z opcjonalnym widgetem na ekranie głównym. </matched_recommendations>

<prd_planning_summary>

## Główne wymagania funkcjonalne

__Kluczowa funkcjonalność__:

- Codzienna interakcja w formie pytania "Masz dziś kaca?" z binarną odpowiedzią (tak/nie)
- Personalizowane powiadomienia (domyślnie o 9:00 rano)
- Licznik dni trzeźwości i podstawowe statystyki
- Różne zestawy treści zależne od odpowiedzi (tak/nie)
- Rotacyjna biblioteka informacji edukacyjnych o alkoholu
- Opcjonalne konto użytkownika z historią i zaawansowanymi statystykami
- Synchronizacja danych do konta dla niezalogowanych lokalnie (opcjonalnie)

__Interfejs i doświadczenie użytkownika__:

- Prosty, intuicyjny interfejs
- Maksymalnie 3-krokowy proces onboardingowy
- Subtelne różnice w komunikacji zależne od stażu użytkownika
- Elementy humorystyczne (szczególnie przy odpowiedzi "tak")
- Informacje edukacyjne rotujące codziennie
- Wizualizacja postępów (dni trzeźwości, oszczędności, poprawa zdrowia)

__Prywatność i bezpieczeństwo__:

- Opcjonalna rejestracja i logowanie
- Minimalizacja zbieranych danych
- Pełne szyfrowanie danych w spoczynku i podczas transmisji
- Prosta, przejrzysta polityka prywatności
- Możliwość całkowitego usunięcia danych
- Automatyczne usuwanie nieaktywnych kont po 12 miesiącach

__Wsparcie dla użytkownika__:

- Algorytm wykrywający wzorce sugerujące kryzys
- Zasoby pomocy (numery telefonów zaufania, techniki radzenia sobie z głodem alkoholowym)
- Prosty system raportowania błędów
- FAQ i dedykowany kontakt e-mail

## Kluczowe historie użytkownika i ścieżki korzystania

1. __Pierwszy kontakt z aplikacją__:

    - Użytkownik pobiera aplikację z Google Play
    - Przechodzi przez prosty proces onboardingowy (max 3 kroki)
    - Otrzymuje informację o prywatności danych
    - Opcjonalnie personalizuje czas powiadomień
    - Opcjonalnie tworzy konto lub korzysta bez logowania

2. __Codzienna interakcja - odpowiedź "nie"__:

    - Użytkownik otrzymuje powiadomienie o wybranej porze
    - Odpowiada "nie" na pytanie "Masz dziś kaca?"
    - Otrzymuje pochwałę i motywujący cytat
    - Widzi aktualizację licznika dni trzeźwości
    - Dostaje rotacyjną informację edukacyjną o alkoholu

3. __Codzienna interakcja - odpowiedź "tak"__:

    - Użytkownik otrzymuje powiadomienie o wybranej porze
    - Odpowiada "tak" na pytanie "Masz dziś kaca?"
    - Otrzymuje wsparcie z elementem humorystycznym
    - Dostaje informacje o dostępnej pomocy
    - Licznik dni trzeźwości resetuje się

4. __Przeglądanie statystyk i historii__:

    - Zalogowany użytkownik przegląda swoje statystyki
    - Widzi wizualizację oszczędności finansowych
    - Sprawdza informacje o poprawie zdrowia
    - Analizuje wzorce swoich odpowiedzi w czasie

5. __Moment kryzysu__:

    - System wykrywa wzorzec sugerujący kryzys
    - Użytkownik otrzymuje dodatkowe zasoby wsparcia
    - Ma dostęp do numerów telefonów zaufania
    - Może skorzystać z technik radzenia sobie z głodem alkoholowym

## Kryteria sukcesu i sposoby ich mierzenia

__Metryki użytkownika__:

- Liczba dni bez kaca (trzeźwości)
- Procent dni trzeźwych w miesiącu/roku
- Wskaźnik nawrotów
- Subiektywna ocena dobrostanu

__Metryki produktowe__:

- Daily Active Users (DAU)
- Retencja 7/30 dni
- Procent użytkowników odpowiadających na codzienne przypomnienie
- Procent użytkowników, którzy osiągnęli 7 kolejnych dni abstynencji

__Metody zbierania danych__:

- Anonimowe dane zagregowane
- Opcjonalna ankieta po 30 dniach używania
- Mechanizm anonimowych sugestii
- System raportowania błędów </prd_planning_summary>

<unresolved_issues>

1. Szczegółowy model biznesowy i strategia monetyzacji (wspomniano freemium i sponsoring, ale bez konkretnych decyzji).
2. Dokładny zakres i rodzaj treści edukacyjnych oraz sposób ich pozyskiwania i weryfikacji.
3. Szczegóły techniczne implementacji backendowej i integracji z systemami zewnętrznymi.
4. Precyzyjne kryteria dla algorytmu wykrywającego wzorce sugerujące kryzys.
5. Harmonogram rozwoju funkcji po MVP, w tym rozszerzenie na inne uzależnienia.
6. Dokładna strategia marketingowa i metody dotarcia do grupy docelowej.
7. Specyfikacja widgetu na ekran główny (został wspomniany jako opcjonalny).
8. Konkretne specyfikacje dla treści sezonowych związanych ze świętami wysokiego ryzyka. </unresolved_issues> </conversation_summary>



Jesteś doświadczonym menedżerem produktu, którego zadaniem jest stworzenie kompleksowego dokumentu wymagań produktu (PRD) w oparciu o poniższe opisy:

<project_description>
MamKaca.pl to aplikacja mobilna wspierająca osoby w procesie wychodzenia z uzależnienia od alkoholu.
Głównym celem jest budowanie zdrowych nawyków poprzez codzienne śledzenie trzeźwości oraz dostarczanie wsparcia i porad w trudnych momentach.
Będzie strona internetowa oferująca pobieranie tej aplikacji, ale wszystkie interakcje beda robione przez telefon.
</project_description>

<project_details>

<conversation_summary>

1. Aplikacja będzie oferować codzienne przypomnienie "Masz dziś kaca?" z odpowiedziami "tak/nie" raz dzienne.
2. Domyślna pora przypomnienia to 9:00 rano, z możliwością personalizacji przez użytkownika.
3. Odpowiedź "tak" będzie skutkować wyświetleniem informacji pomocowych i motywującej grafiki.
4. Odpowiedź "nie" będzie skutkować pochwałą, motywującym cytatem i licznikiem dni trzeźwości.
5. Aplikacja będzie zawierać elementy humorystyczne, ale unikające gloryfikacji picia. Szczególnie aby zachęcić do korzystania przez młodzież w celu edukacji.
6. Konto użytkownika będzie opcjonalne, a aplikacja będzie darmowa i anonimowa.
7. Zostanie zaimplementowana rotacyjna biblioteka krótkich, faktycznych informacji o alkoholu.
8. Zalogowani użytkownicy będą mieć dostęp do historii i statystyk.
9. Zostaną wdrożone różne zasoby pomocy: numery telefonów zaufania, informacje edukacyjne.
10. System będzie wykrywać wzorce sugerujące kryzys i oferować dodatkowe wsparcie.
11. Dane będą zbierane anonimowo z możliwością ich całkowitego usunięcia.
12. Interfejs pozostanie spójny, ale treści będą dostosowane do "stażu trzeźwościowego".
13. Aplikacja będzie działać offline ale i online. Będzie miała możliwość synchronizacji danych dla zalogowanych.
14. Na start zostanie stworzona biblioteka minimum 100 cytatów/faktów/grafik.
15. Wdrożona zostanie pełna ochrona prywatności zgodna z RODO.
16. System będzie zawierać prosty mechanizm raportowania błędów i FAQ.

<matched_recommendations>

1. Wdrożenie domyślnego przypomnienia o 9:00 rano z opcją personalizacji czasu powiadomień.
2. Wdożenie mechanizmu głosowania "czy masz kaca?" z odpowiedzią tak/nie.
3. Dodanie statystyk ilości osob majacych kaca danego dnia/msc/roku i innych.
4. Stworzenie biblioteki rotacyjnych, krótkich informacji edukacyjnych o alkoholu.
5. Zaprojektowanie prostego, maksymalnie 3-krokowego procesu onboardingowego.
6. Wprowadzenie wizualizacji oszczędności finansowych i poprawy zdrowia w statystykach.
7. Dodanie algorytmu wykrywającego wzorce sugerujące kryzys po okresie abstynencji.
8. Implementacja systemu rotacji treści (minimum 100 różnych elementów na start).
9. Wdrożenie minimalnego zbierania danych z pełnym szyfrowaniem i prostą polityką prywatności.
10. Wprowadzenie subtelnych różnic w komunikacji zależnych od "stażu trzeźwościowego".
11. Skupienie się na 3-5 kluczowych metrykach: DAU, retencja, odpowiadanie na przypomnienia, osiągnięcia.
12. Zaprojektowanie aplikacji do pełnego działania z opcjonalnym widgetem na ekranie głównym. </matched_recommendations>

<prd_planning_summary>

## Główne wymagania funkcjonalne

__Kluczowa funkcjonalność__:

- Codzienna interakcja w formie pytania "Masz dziś kaca?" z binarną odpowiedzią (tak/nie)
- Personalizowane powiadomienia (domyślnie o 9:00 rano)
- Licznik dni trzeźwości i podstawowe statystyki
- Różne zestawy treści zależne od odpowiedzi (tak/nie)
- Rotacyjna biblioteka informacji edukacyjnych o alkoholu
- Opcjonalne konto użytkownika z historią i zaawansowanymi statystykami
- Synchronizacja danych do konta dla niezalogowanych lokalnie (opcjonalnie)

__Interfejs i doświadczenie użytkownika__:

- Prosty, intuicyjny interfejs
- Maksymalnie 3-krokowy proces onboardingowy
- Subtelne różnice w komunikacji zależne od stażu użytkownika
- Elementy humorystyczne (szczególnie przy odpowiedzi "tak")
- Informacje edukacyjne rotujące codziennie
- Wizualizacja postępów (dni trzeźwości, oszczędności, poprawa zdrowia)

__Prywatność i bezpieczeństwo__:

- Opcjonalna rejestracja i logowanie
- Minimalizacja zbieranych danych
- Pełne szyfrowanie danych w spoczynku i podczas transmisji
- Prosta, przejrzysta polityka prywatności
- Możliwość całkowitego usunięcia danych
- Automatyczne usuwanie nieaktywnych kont po 12 miesiącach

__Wsparcie dla użytkownika__:

- Algorytm wykrywający wzorce sugerujące kryzys
- Zasoby pomocy (numery telefonów zaufania, techniki radzenia sobie z głodem alkoholowym)
- Prosty system raportowania błędów
- FAQ i dedykowany kontakt e-mail

## Kluczowe historie użytkownika i ścieżki korzystania

1. __Pierwszy kontakt z aplikacją__:

    - Użytkownik pobiera aplikację z Google Play
    - Przechodzi przez prosty proces onboardingowy (max 3 kroki)
    - Otrzymuje informację o prywatności danych
    - Opcjonalnie personalizuje czas powiadomień
    - Opcjonalnie tworzy konto lub korzysta bez logowania

2. __Codzienna interakcja - odpowiedź "nie"__:

    - Użytkownik otrzymuje powiadomienie o wybranej porze
    - Odpowiada "nie" na pytanie "Masz dziś kaca?"
    - Otrzymuje pochwałę i motywujący cytat
    - Widzi aktualizację licznika dni trzeźwości
    - Dostaje rotacyjną informację edukacyjną o alkoholu

3. __Codzienna interakcja - odpowiedź "tak"__:

    - Użytkownik otrzymuje powiadomienie o wybranej porze
    - Odpowiada "tak" na pytanie "Masz dziś kaca?"
    - Otrzymuje wsparcie z elementem humorystycznym
    - Dostaje informacje o dostępnej pomocy
    - Licznik dni trzeźwości resetuje się

4. __Przeglądanie statystyk i historii__:

    - Zalogowany użytkownik przegląda swoje statystyki
    - Widzi wizualizację oszczędności finansowych
    - Sprawdza informacje o poprawie zdrowia
    - Analizuje wzorce swoich odpowiedzi w czasie

5. __Moment kryzysu__:

    - System wykrywa wzorzec sugerujący kryzys
    - Użytkownik otrzymuje dodatkowe zasoby wsparcia
    - Ma dostęp do numerów telefonów zaufania
    - Może skorzystać z technik radzenia sobie z głodem alkoholowym

## Kryteria sukcesu i sposoby ich mierzenia

__Metryki użytkownika__:

- Liczba dni bez kaca (trzeźwości)
- Procent dni trzeźwych w miesiącu/roku
- Wskaźnik nawrotów
- Subiektywna ocena dobrostanu

__Metryki produktowe__:

- Daily Active Users (DAU)
- Retencja 7/30 dni
- Procent użytkowników odpowiadających na codzienne przypomnienie
- Procent użytkowników, którzy osiągnęli 7 kolejnych dni abstynencji

__Metody zbierania danych__:

- Anonimowe dane zagregowane
- Opcjonalna ankieta po 30 dniach używania
- Mechanizm anonimowych sugestii
- System raportowania błędów </prd_planning_summary>

<unresolved_issues>

1. Szczegółowy model biznesowy i strategia monetyzacji (wspomniano freemium i sponsoring, ale bez konkretnych decyzji).
2. Dokładny zakres i rodzaj treści edukacyjnych oraz sposób ich pozyskiwania i weryfikacji.
3. Szczegóły techniczne implementacji backendowej i integracji z systemami zewnętrznymi.
4. Precyzyjne kryteria dla algorytmu wykrywającego wzorce sugerujące kryzys.
5. Harmonogram rozwoju funkcji po MVP, w tym rozszerzenie na inne uzależnienia.
6. Dokładna strategia marketingowa i metody dotarcia do grupy docelowej.
7. Specyfikacja widgetu na ekran główny (został wspomniany jako opcjonalny).
8. Konkretne specyfikacje dla treści sezonowych związanych ze świętami wysokiego ryzyka. </unresolved_issues> </conversation_summary>
   </project_details>

Wykonaj następujące kroki, aby stworzyć kompleksowy i dobrze zorganizowany dokument:

1. Podziel PRD na następujące sekcje:
   a. Przegląd projektu
   b. Problem użytkownika
   c. Wymagania funkcjonalne
   d. Granice projektu
   e. Historie użytkownika
   f. Metryki sukcesu

2. W każdej sekcji należy podać szczegółowe i istotne informacje w oparciu o opis projektu i odpowiedzi na pytania wyjaśniające. Upewnij się, że:
    - Używasz jasnego i zwięzłego języka
    - W razie potrzeby podajesz konkretne szczegóły i dane
    - Zachowujesz spójność w całym dokumencie
    - Odnosisz się do wszystkich punktów wymienionych w każdej sekcji

3. Podczas tworzenia historyjek użytkownika i kryteriów akceptacji
    - Wymień WSZYSTKIE niezbędne historyjki użytkownika, w tym scenariusze podstawowe, alternatywne i skrajne.
    - Przypisz unikalny identyfikator wymagań (np. US-001) do każdej historyjki użytkownika w celu bezpośredniej identyfikowalności.
    - Uwzględnij co najmniej jedną historię użytkownika specjalnie dla bezpiecznego dostępu lub uwierzytelniania, jeśli aplikacja wymaga identyfikacji użytkownika lub ograniczeń dostępu.
    - Upewnij się, że żadna potencjalna interakcja użytkownika nie została pominięta.
    - Upewnij się, że każda historia użytkownika jest testowalna.

Użyj następującej struktury dla każdej historii użytkownika:
- ID
- Tytuł
- Opis
- Kryteria akceptacji

4. Po ukończeniu PRD przejrzyj go pod kątem tej listy kontrolnej:
    - Czy każdą historię użytkownika można przetestować?
    - Czy kryteria akceptacji są jasne i konkretne?
    - Czy mamy wystarczająco dużo historyjek użytkownika, aby zbudować w pełni funkcjonalną aplikację?
    - Czy uwzględniliśmy wymagania dotyczące uwierzytelniania i autoryzacji (jeśli dotyczy)?

5. Formatowanie PRD:
    - Zachowaj spójne formatowanie i numerację.
    - Nie używaj pogrubionego formatowania w markdown ( ** ).
    - Wymień WSZYSTKIE historyjki użytkownika.
    - Sformatuj PRD w poprawnym markdown.

Przygotuj PRD z następującą strukturą:

```markdown
# Dokument wymagań produktu (PRD) - mamkaca.pl
## 1. Przegląd produktu
## 2. Problem użytkownika
## 3. Wymagania funkcjonalne
## 4. Granice produktu
## 5. Historyjki użytkowników
## 6. Metryki sukcesu
```

Pamiętaj, aby wypełnić każdą sekcję szczegółowymi, istotnymi informacjami w oparciu o opis projektu i nasze pytania wyjaśniające. Upewnij się, że PRD jest wyczerpujący, jasny i zawiera wszystkie istotne informacje potrzebne do dalszej pracy nad produktem.

Ostateczny wynik powinien składać się wyłącznie z PRD zgodnego ze wskazanym formatem w markdown, który zapiszesz w pliku .aiassistant/prd.md
