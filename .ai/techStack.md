## Etap 1: MVP

### Backend: Supabase

### Zapewnia bazę danych PostgreSQL
* Supabase zapewnia kompletny zestaw narzędzi backendowych (autentykacja, baza danych, storage, API) dostępnych niemal natychmiast po konfiguracji, bez konieczności pisania kodu serwerowego. Jako rozwiązanie "backend as a service" radykalnie przyspiesza rozwój MVP, pozwalając na skupienie się na funkcjonalnościach biznesowych zamiast infrastrukturze.
* Zapewnia SDK w wielu językach
* Jest open-source, które można hostować lokalnie lub w chmurze.
* Posiada autentykację użytkowników

### Frontend web: Vite + React + TypeScript + Tailwind CSS + shadcn-ui
- astro 5 pozwala na tworzenie szybkich i wydajnych stron internetowych i aplikacji z minimalną ilością JavaScriptu
- react 19 zapewni interaktywność tam dzie jest potrzebna
- TypeScript 5 dla statycznego typowania kodu i lepszego wsparcia IDE
- Tailwind 4 pozwala na wygodne stylowanie apliakcji
- Shadcn/uui zapewnia komponenty UI

### Mobile: Kotlin z Jetpack Compose

Kotlin z Jetpack Compose zapewnia nowoczesne podejście do budowy natywnych aplikacji na platformę Android. Jetpack Compose to deklaratywny framework UI, który upraszcza i przyspiesza proces tworzenia interfejsu użytkownika, jednocześnie zapewniając pełną wydajność natywnej aplikacji. Kotlin jako język oficjalnie wspierany przez Google dla platformy Android oferuje zwiększone bezpieczeństwo kodu, zwięzłą składnię i funkcje, które znacząco podnoszą produktywność deweloperów.

## Etap 2: Produkt pełny

### Backend: Spring Boot + PostgreSQL

Spring Boot oferuje enterprise-grade framework z bogatym ekosystemem, który zapewnia solidne podstawy dla rozwijanej aplikacji, szczególnie w obszarach bezpieczeństwa, skalowalności i integracji. PostgreSQL jako relacyjna baza danych zapewnia niezawodność, zaawansowane funkcje (jak JSONB, transakcje, triggery) oraz doskonałą wydajność dla złożonych zapytań i dużych zestawów danych.

### Frontend web: Pozostaje bez zmian

Utrzymanie tego samego stosu frontendowego między etapami zapewnia stabilność i ciągłość rozwoju, eliminując potrzebę przepisywania działającego kodu. Wybrane technologie są przyszłościowe i skalowalne, co oznacza, że będą odpowiednie zarówno dla MVP, jak i dla dojrzałego produktu.

### Mobile: Kotlin z Jetpack Compose (kontynuacja)

Kontynuacja rozwoju w Kotlinie z Jetpack Compose zapewni spójność codebase'u między etapami projektu. Jetpack Compose jako nowoczesny toolkit do budowy natywnych UI pozwala na tworzenie eleganckich, responsywnych interfejsów z mniejszą ilością kodu. Na tym etapie możliwe będzie rozszerzenie funkcjonalności aplikacji o bardziej zaawansowane komponenty, animacje i integracje z innymi bibliotekami Android Jetpack.

## Strategia migracji między etapami

Kluczem do udanej migracji będzie odpowiednie zaprojektowanie API między frontendem a backendem. Supabase dostarcza API REST/GraphQL, które można później odtworzyć w Spring Boocie, minimalizując zmiany po stronie klienta. Migracja danych między Supabase a PostgreSQL będzie względnie prosta, gdyż Supabase jest oparty na PostgreSQL.

Dla aplikacji mobilnej, utrzymanie Kotlina z Jetpack Compose przez cały cykl życia projektu zapewni spójność i eliminuje potrzebę migracji między technologiami. Modułowa struktura aplikacji pozwoli na stopniowe rozwijanie funkcjonalności bez konieczności przepisywania kodu.

## Podsumowanie zalet podejścia dwuetapowego

1. Szybszy time-to-market dla MVP dzięki wykorzystaniu gotowych rozwiązań (Supabase) oraz nowoczesnych narzędzi do tworzenia natywnych aplikacji (Kotlin z Jetpack Compose)
2. Zrównoważone wykorzystanie zasobów - na początku projektu fokus na funkcjonalnościach, później na skalowalności i wydajności
3. Możliwość zbierania feedbacku użytkowników i walidacji założeń biznesowych przed znaczącymi inwestycjami w infrastrukturę
4. Lepsze dopasowanie złożoności technologicznej do etapu rozwoju produktu
5. Stopniowa nauka nowych technologii (Kotlin) przy jednoczesnym dostarczaniu wartości biznesowej
