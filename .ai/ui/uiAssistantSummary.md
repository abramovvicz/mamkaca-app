# Analiza architektury UI dla MVP mamkaca.pl

Po przeanalizowaniu dostarczonych dokumentów, zidentyfikowałem kluczowe aspekty, które należy uwzględnić w architekturze interfejsu użytkownika dla MVP aplikacji mamkaca.pl.

Zalecam projektowanie interfejsu zgodnie z zasadą "progressive enhancement", gdzie podstawowa funkcjonalność jest wspólna dla wszystkich platform, ale doświadczenie jest zoptymalizowane dla specyfiki każdej platformy. Wersja webowa może mieć bardziej rozbudowane statystyki i widoki, podczas gdy wersja mobilna powinna skupiać się na codziennych interakcjach i powiadomieniach.

AD 2 Jak powinna wyglądać hierarchia nawigacji, biorąc pod uwagę kluczowe funkcje aplikacji?

Proponuję trójpoziomową hierarchię nawigacji:

- Główny poziom: Dzienny widok odpowiedzi, Statystyki, Pomoc, Ustawienia
- Drugi poziom: Szczegółowe widoki w ramach głównych kategorii (np. różne typy statystyk)
- Trzeci poziom: Akcje szczegółowe i formularze (np. formularz resetowania streaka) Ta struktura odzwierciedla najważniejsze funkcje API i zapewnia intuicyjne doświadczenie użytkownika.

AD3 Interfejs powinien domyślnie prezentować funkcje dostępne dla niezalogowanych użytkowników, z wyraźnymi wskazaniami, które funkcje wymagają logowania. Zalecam projektowanie z myślą o "progressive disclosure" - pokazywanie dodatkowych opcji dostępnych po zalogowaniu jako rozszerzenia podstawowych funkcji, a nie całkowicie oddzielnych sekcji.

AD 4: Codzienne pytanie powinno być centralnym elementem interfejsu, widocznym bezpośrednio po otwarciu aplikacji (jeśli użytkownik jeszcze nie odpowiedział). Po udzieleniu odpowiedzi, interfejs powinien płynnie przejść do ekranu z odpowiednią reakcją i statystykami, które są generowane na podstawie odpowiedzi użytkownika przez endpointy API.

AD 5: Uwierzytelnianie powinno wykorzystywać natywne funkcje Supabase, z interfejsem integrującym się z API `/api/auth/*`. Ekrany logowania i rejestracji powinny być proste, ograniczone do niezbędnego minimum danych (email, hasło), z jasnym komunikatem o opcjonalności rejestracji. Sesja użytkownika powinna być przechowywana zgodnie z mechanizmami tokenów JWT opisanymi w API.

AD 6 Treści edukacyjne powinny być dostępne w dwóch formach:

- Jako element odpowiedzi na codzienne pytanie (krótka forma)
- W dedykowanej sekcji z możliwością przeglądania wszystkich treści (pełna forma) Wsparcie w kryzysie powinno być dostępne przez wyraźny, zawsze widoczny przycisk/sekcję "Pomoc" prowadzącą do zasobów pobieranych przez endpoint `/api/help`.


AD 7: Kluczowe komponenty UI powinny obejmować:

- DailyQuestion - centralny komponent do odpowiedzi na pytanie "Masz dziś kaca?"
- SobrietyCounter - komponent wyświetlający aktualny okres trzeźwości
- AnswerResponse - komponent wyświetlający reakcję na odpowiedź użytkownika
- StatisticsDisplay - zestaw komponentów do wizualizacji statystyk
- AchievementCard - komponent wyświetlający osiągnięcia
- EducationalContentCard - komponent do wyświetlania treści edukacyjnych
- HelpResourcesList - komponent wyświetlający dostępne zasoby pomocy
- SettingsForm - komponent do zarządzania ustawieniami Te komponenty powinny korzystać z biblioteki shadcn/ui dla spójnego wyglądu.


AD 8: Wykorzystać pełny potencjał Tailwind CSS z jego wariantami responsywności (sm:, md:, lg:, itd.). Projektować z myślą o podejściu "mobile-first", gdzie interfejs jest najpierw zoptymalizowany dla urządzeń mobilnych, a następnie rozszerzany na większe ekrany. Kluczowe akcje (jak odpowiedź na pytanie) powinny być łatwo dostępne niezależnie od rozmiaru ekranu.

AD 9: Interfejs powinien informować użytkownika o statusie synchronizacji w dyskretny sposób (np. mały wskaźnik w nagłówku). W przypadku braku połączenia, aplikacja powinna nadal umożliwiać podstawowe funkcje, z wyraźnym komunikatem o tym, że dane zostaną zsynchronizowane po przywróceniu połączenia. Potrzebne będzie stworzenie lokalnego magazynu danych wykorzystującego API przeglądarki (localStorage, IndexedDB) lub natywne mechanizmy przechowywania dla React Native.


AD 10: System ankiet powinien być zintegrowany jako nienatarczywy element interfejsu, pojawiający się po osiągnięciu określonych kamieni milowych lub po ustalonym czasie korzystania z aplikacji. Formularz ankiety powinien być prosty i krótki, z możliwością pominięcia. Podobnie system zgłaszania błędów i sugestii powinien być łatwo dostępny z menu głównego, ale nie powinien przytłaczać podstawowych funkcji aplikacji.

Na podstawie analizy proponuję następującą architekturę UI dla MVP:

## Główne widoki aplikacji:

1. __Ekran powitalny / Onboarding__ - pierwsze uruchomienie, wprowadzenie do aplikacji
2. __Ekran główny z codziennym pytaniem__ - centralny element aplikacji
3. __Ekran odpowiedzi pozytywnej__ - wsparcie po odpowiedzi "Tak"
4. __Ekran odpowiedzi negatywnej__ - gratulacje po odpowiedzi "Nie"
5. __Ekran statystyk__ - wizualizacje postępów (podstawowe i rozszerzone)
6. __Ekran zasobów pomocy__ - lista dostępnych form wsparcia
7. __Ekran ustawień__ - preferencje użytkownika
8. __Ekran logowania/rejestracji__ - opcjonalny dla użytkowników chcących zachować dane
9. __Ekran osiągnięć__ - lista zdobytych kamieni milowych
10. __Ekran profilu__ - zarządzanie danymi użytkownika (dla zalogowanych)

## Architektura nawigacyjna:

```javascript
├── Onboarding (tylko przy pierwszym uruchomieniu)
│
├── Ekran główny (codzienne pytanie / odpowiedź)
│   ├── Odpowiedź pozytywna ("Tak")
│   │   └── Zasoby pomocy
│   └── Odpowiedź negatywna ("Nie")
│       └── Treść edukacyjna
│
├── Menu główne
│   ├── Statystyki
│   │   ├── Podstawowe (dla wszystkich)
│   │   └── Rozszerzone (dla zalogowanych)
│   │
│   ├── Osiągnięcia
│   │
│   ├── Pomoc
│   │   ├── Telefony zaufania
│   │   ├── Grupy wsparcia
│   │   └── Techniki radzenia sobie
│   │
│   ├── Ustawienia
│   │   ├── Powiadomienia
│   │   ├── Personalizacja kosztów
│   │   ├── Prywatność
│   │   └── Konto użytkownika
│   │
│   └── Profil (dla zalogowanych)
│       ├── Dane użytkownika
│       ├── Eksport danych
│       └── Usunięcie konta
```

Ta struktura UI jest ściśle zintegrowana z endpointami API, zapewniając spójne doświadczenie użytkownika. Aby przejść do szczegółowego projektu, potrzebuję Twojej opinii na temat przedstawionych pytań i rekomendacji.


---

Jesteś asystentem AI, którego zadaniem jest podsumowanie rozmowy na temat planowania architektury UI dla MVP i przygotowanie zwięzłego podsumowania dla następnego etapu rozwoju. W historii konwersacji znajdziesz następujące informacje:
1. Dokument wymagań produktu (PRD)
2. Informacje o stacku technologicznym
3. Plan API
4. Historia rozmów zawierająca pytania i odpowiedzi
5. Zalecenia dotyczące architektury UI

Twoim zadaniem jest:
1. Podsumować historię konwersacji, koncentrując się na wszystkich decyzjach związanych z planowaniem architektury UI.
2. Dopasować zalecenia modelu do odpowiedzi udzielonych w historii konwersacji. Zidentyfikować, które zalecenia są istotne w oparciu o dyskusję.
3. Przygotować szczegółowe podsumowanie rozmowy, które obejmuje:
   a. Główne wymagania dotyczące architektury UI
   b. Kluczowe widoki, ekrany i przepływy użytkownika
   c. Strategię integracji z API i zarządzania stanem
   d. Kwestie dotyczące responsywności, dostępności i bezpieczeństwa
   e. Wszelkie nierozwiązane kwestie lub obszary wymagające dalszego wyjaśnienia
4. Sformatować wyniki w następujący sposób:

<conversation_summary>
<decisions>
[Wymień decyzje podjęte przez użytkownika, ponumerowane].
</decisions>
<matched_recommendations>
[Lista najistotniejszych zaleceń dopasowanych do rozmowy, ponumerowanych]
</matched_recommendations>
<ui_architecture_planning_summary>
[Podaj szczegółowe podsumowanie rozmowy, w tym elementy wymienione w kroku 3].
</ui_architecture_planning_summary>
<unresolved_issues>
[Wymień wszelkie nierozwiązane kwestie lub obszary wymagające dalszych wyjaśnień, jeśli takie istnieją]
</unresolved_issues>
</conversation_summary>

Końcowy wynik powinien zawierać tylko treść w formacie markdown. Upewnij się, że Twoje podsumowanie jest jasne, zwięzłe i zapewnia cenne informacje dla następnego etapu planowania architektury UI i integracji z API.