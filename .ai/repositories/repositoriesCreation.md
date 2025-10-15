Twoim zadaniem jest wdrożenie repozytoria korzystające z:
- z klienta Supabase do komunikacji z API 
- Obsługują mapowanie danych z/do modeli domeny
- Implementują logikę synchronizacji i cachowania

Twoim celem jest stworzenie solidnej i dobrze zorganizowanej implementacji, która zawiera odpowiednią
walidację, obsługę błędów i podąża za wszystkimi logicznymi krokami opisanymi w planie.

Najpierw dokładnie przejrzyj dostarczony plan wdrożenia:

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
Realizuj maksymalnie 3 kroki planu implementacji, podsumuj krótko co zrobiłeś i opisz plan na 3
kolejne działania - zatrzymaj w tym momencie pracę i czekaj na mój feedback.
</implementation_approach>

Teraz wykonaj następujące kroki, aby zaimplementować repozytoria


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

Pamiętaj, aby przestrzegać najlepszych praktyk projektowania repozytoriów, stosować się do wytycznych
dotyczących stylu języka programowania i upewnić się, że kod jest czysty, czytelny i dobrze
zorganizowany.