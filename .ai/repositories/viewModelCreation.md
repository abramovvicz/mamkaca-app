Twoim zadaniem jest wdrożenie:
1. klas mapujących między modelami domeny a odpowiedziami z API Supabase
2. ViewModeli które będą korzystać z repozytoriów do komunikacji z backendem
Twoim celem jest stworzenie solidnej i dobrze zorganizowanej implementacji, która zawiera
odpowiednią walidację, obsługę błędów i podąża za wszystkimi logicznymi krokami opisanymi w planie.

Najpierw dokładnie przejrzyj dostarczony plan wdrożenia:
# Plan wdrożenia ViewModeli i klas mapujących

Na podstawie analizy zaimplementowanych repozytoriów oraz ekranów aplikacji, przedstawiam plan wdrożenia brakujących komponentów MVI. Aplikacja ma już zaimplementowane repozytoria, które obsługują komunikację z API Supabase oraz cachowanie danych.

## 1. Klasy mapujące (Mappery)

Rekomendowana struktura klas mapujących:

```javascript
com.abramovvicz.mamkaca.data.mapper
├── UserMapper.kt         // Mapowanie między modelami User a DTO
└── AnswerMapper.kt       // Mapowanie między modelami Answer a DTO
```

Każda klasa mapująca powinna zawierać metody:

- `toEntity(domainModel)` - konwersja z modelu domeny do encji DTO
- `toDomain(entity)` - konwersja z encji DTO na model domeny
- `toDomainList(entityList)` - konwersja listy encji na listę modeli domeny

## 2. ViewModele

Zalecane ViewModele oparte na wzorcu MVI (Model-View-Intent):

```javascript
com.abramovvicz.mamkaca.presentation.viewmodel
├── HomeViewModel.kt      // Zarządzanie odpowiedziami na pytanie "Masz dziś kaca?"
├── StatsViewModel.kt     // Analiza statystyk odpowiedzi
└── ProfileViewModel.kt   // Zarządzanie profilem użytkownika
```

Każdy ViewModel powinien zawierać:

- Stany UI (sealed class reprezentujący stan ekranu)
- Intencje (sealed class reprezentujący akcje użytkownika)
- Funkcje do obsługi intencji
- Obserwowalne stany (StateFlow lub LiveData)

## 3. Klasy stanów i intencji

Dla każdego ViewModelu należy zdefiniować odpowiednie stany i intencje:

### HomeViewModel

- Stany: Loading, Error, ReadyToAnswer, AnswerSaved
- Intencje: SubmitAnswer, ResetAnswer, AddNote

### StatsViewModel

- Stany: Loading, Error, StatsLoaded
- Intencje: LoadStats, FilterByPeriod

### ProfileViewModel

- Stany: Loading, Error, ProfileLoaded
- Intencje: UpdateProfile, UpdateLastDrinkingDate, SignOut

## 4. Moduł Dependency Injection

Rozszerzenie istniejącego modułu RepositoryModule lub dodanie nowego ViewModelModule:

```kotlin
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
```

## 5. Kolejność implementacji

1. Implementacja klas mapujących
2. Implementacja stanów i intencji dla ViewModeli
3. Implementacja ViewModeli
4. Konfiguracja DI dla ViewModeli
5. Integracja ViewModeli z ekranami

## 6. Przykładowy przepływ danych

```javascript
UI (Composable) -> Intent -> ViewModel -> Repository -> Mapper -> API -> Mapper -> Repository -> ViewModel -> UI State -> UI (Composable)
```

Czy chcesz, abym dokładniej rozwinął któryś z powyższych punktów lub masz dodatkowe pytania dotyczące planowanej implementacji?

<implementation_plan>
@/.ai/achivments-implementation-plan.md
</implementation_plan>

<types>
@src/types.ts
</types>

<implementation_rules>
@/.cursor/rules/backend.mdc ,
</implementation_rules>

<implementation_approach>
# Plan wdrożenia ViewModeli i klas mapujących

Na podstawie analizy zaimplementowanych repozytoriów oraz ekranów aplikacji, przedstawiam plan wdrożenia brakujących komponentów MVI. Aplikacja ma już zaimplementowane repozytoria, które obsługują komunikację z API Supabase oraz cachowanie danych.

## 1. Klasy mapujące (Mappery)

Rekomendowana struktura klas mapujących:

```javascript
com.abramovvicz.mamkaca.data.mapper
├── UserMapper.kt         // Mapowanie między modelami User a DTO
└── AnswerMapper.kt       // Mapowanie między modelami Answer a DTO
```

Każda klasa mapująca powinna zawierać metody:

- `toEntity(domainModel)` - konwersja z modelu domeny do encji DTO
- `toDomain(entity)` - konwersja z encji DTO na model domeny
- `toDomainList(entityList)` - konwersja listy encji na listę modeli domeny

## 2. ViewModele

Zalecane ViewModele oparte na wzorcu MVI (Model-View-Intent):

```javascript
com.abramovvicz.mamkaca.presentation.viewmodel
├── HomeViewModel.kt      // Zarządzanie odpowiedziami na pytanie "Masz dziś kaca?"
├── StatsViewModel.kt     // Analiza statystyk odpowiedzi
└── ProfileViewModel.kt   // Zarządzanie profilem użytkownika
```

Każdy ViewModel powinien zawierać:

- Stany UI (sealed class reprezentujący stan ekranu)
- Intencje (sealed class reprezentujący akcje użytkownika)
- Funkcje do obsługi intencji
- Obserwowalne stany (StateFlow lub LiveData)

## 3. Klasy stanów i intencji

Dla każdego ViewModelu należy zdefiniować odpowiednie stany i intencje:

### HomeViewModel

- Stany: Loading, Error, ReadyToAnswer, AnswerSaved
- Intencje: SubmitAnswer, ResetAnswer, AddNote

### StatsViewModel

- Stany: Loading, Error, StatsLoaded
- Intencje: LoadStats, FilterByPeriod

### ProfileViewModel

- Stany: Loading, Error, ProfileLoaded
- Intencje: UpdateProfile, UpdateLastDrinkingDate, SignOut

## 4. Moduł Dependency Injection

Rozszerzenie istniejącego modułu RepositoryModule lub dodanie nowego ViewModelModule:

```kotlin
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
```

## 5. Kolejność implementacji

1. Implementacja klas mapujących
2. Implementacja stanów i intencji dla ViewModeli
3. Implementacja ViewModeli
4. Konfiguracja DI dla ViewModeli
5. Integracja ViewModeli z ekranami

## 6. Przykładowy przepływ danych

```javascript
UI (Composable) -> Intent -> ViewModel -> Repository -> Mapper -> API -> Mapper -> Repository -> ViewModel -> UI State -> UI (Composable)
```

Czy chcesz, abym dokładniej rozwinął któryś z powyższych punktów lub masz dodatkowe pytania dotyczące planowanej implementacji?

Teraz wykonaj następujące kroki, aby zaimplementować klasy mapujace oraz view modele

2. Rozpocznij implementację:
    - Skonfiguruj parametry funkcji w oparciu o oczekiwane dane wejściowe
    - Wdrożenie walidacji danych wejściowych dla wszystkich parametrów
    - Postępuj zgodnie z logicznymi krokami opisanymi w planie wdrożenia
    - Wdrożenie obsługi błędów dla każdego etapu procesu
    - Zapewnienie właściwego przetwarzania i transformacji danych zgodnie z wymaganiami
    - Przygotowanie struktury danych odpowiedzi

3. Walidacja i obsługa błędów:
    - Wdrożenie dokładnej walidacji danych wejściowych dla wszystkich parametrów
    - Dostarczanie jasnych i informacyjnych komunikatów o błędach w odpowiedzi.
    - Obsługa potencjalnych wyjątków, które mogą wystąpić podczas przetwarzania.

4. Rozważania dotyczące testowania:
    - Należy rozważyć edge case'y i potencjalne problemy, które powinny zostać przetestowane.
    - Upewnienie się, że wdrożenie obejmuje wszystkie scenariusze wymienione w planie.

5. Dokumentacja:
    - Dodaj jasne komentarze, aby wyjaśnić złożoną logikę lub ważne decyzje
    - Dołącz dokumentację dla głównej funkcji i wszelkich funkcji pomocniczych.

Po zakończeniu implementacji upewnij się, że zawiera wszystkie niezbędne importy, definicje funkcji
i wszelkie dodatkowe funkcje pomocnicze lub klasy wymagane do implementacji.

Jeśli musisz przyjąć jakieś założenia lub masz jakiekolwiek pytania dotyczące planu implementacji,
przedstaw je przed pisaniem kodu.

Pamiętaj, aby przestrzegać najlepszych praktyk projektowania repozytoriów, stosować się do
wytycznych
dotyczących stylu języka programowania i upewnić się, że kod jest czysty, czytelny i dobrze
zorganizowany.