# Schemat bazy danych dla MamKaca.pl

## 1. Lista tabel z kolumnami, typami danych i ograniczeniami

### System użytkowników

#### `users` - Użytkownicy aplikacji
This table is managed by Supabase Auth.

| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator użytkownika |
| email | text | UNIQUE | Adres email (opcjonalny dla niezalogowanych użytkowników) |
| last_drinking_date | timestamptz | | Data ostatniego spożycia alkoholu (do obliczania okresu trzeźwości) |
| timezone | text | DEFAULT 'Europe/Warsaw' | Strefa czasowa użytkownika |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia konta |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji konta |

#### `devices` - Urządzenia użytkowników
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator urządzenia |
| user_id | uuid | REFERENCES users(id) | Identyfikator użytkownika (opcjonalny dla niezalogowanych) |
| device_identifier | text | NOT NULL, UNIQUE | Unikalny identyfikator urządzenia |
| device_name | text | | Nazwa urządzenia |
| device_type | text | | Typ urządzenia |
| last_sync_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej synchronizacji |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |

#### `user_settings` - Ustawienia użytkownika
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator ustawień |
| user_id | uuid | NOT NULL, REFERENCES users(id), UNIQUE | Identyfikator użytkownika |
| notification_time | time | NOT NULL, DEFAULT '09:00:00' | Czas powiadomień |
| notifications_enabled | boolean | NOT NULL, DEFAULT true | Czy powiadomienia są włączone |
| notification_format | text | NOT NULL, DEFAULT 'standard' | Format powiadomień (standardowy/dyskretny) |
| pin_code_hash | text | | Hash kodu PIN do zabezpieczenia aplikacji |
| alcohol_expense_amount | decimal(10,2) | | Kwota wydatków na alkohol |
| alcohol_expense_frequency | text | | Częstotliwość wydatków (dziennie/tygodniowo/miesięcznie) |
| alcohol_expense_currency | text | DEFAULT 'PLN' | Waluta wydatków |
| alcohol_expense_option | text | DEFAULT 'default' | Opcja obliczania wydatków (domyślna/predefiniowana/własna) |
| humor_level | text | DEFAULT 'standard' | Poziom elementów humorystycznych |
| hide_app_icon | boolean | DEFAULT false | Czy ukrywać ikonę aplikacji |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |

### System śledzenia trzeźwości

#### `answers` - Odpowiedzi na pytanie "Masz dziś kaca?"
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator odpowiedzi |
| user_id | uuid | REFERENCES users(id) | Identyfikator użytkownika |
| device_id | uuid | REFERENCES devices(id) | Identyfikator urządzenia |
| answer_date | date | NOT NULL | Data odpowiedzi |
| answer_time | time | NOT NULL | Czas odpowiedzi |
| answer_type | text | NOT NULL, CHECK (answer_type IN ('yes', 'no', 'none')) | Typ odpowiedzi |
| answer_source | text | NOT NULL, DEFAULT 'manual', CHECK (answer_source IN ('manual', 'notification')) | Źródło odpowiedzi |
| note | text | | Opcjonalna notatka |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |

*Dodatkowe ograniczenia:*
- `answers_user_or_device_not_null`: Wymusza, że albo user_id, albo device_id musi być podane
- `answers_unique_per_day`: Zapewnia maksymalnie jedną odpowiedź dziennie per użytkownik/urządzenie

#### `resets` - Resety "bez wstydu"
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator resetu |
| user_id | uuid | NOT NULL, REFERENCES users(id) | Identyfikator użytkownika |
| reset_date | date | NOT NULL | Data resetu |
| note | text | | Opcjonalna notatka o przyczynach resetu |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |

#### `stat_aggregations` - Zagregowane statystyki
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator agregacji |
| user_id | uuid | NOT NULL, REFERENCES users(id) | Identyfikator użytkownika |
| period_type | text | NOT NULL, CHECK (period_type IN ('day', 'week', 'month', 'year')) | Typ okresu |
| period_start | date | NOT NULL | Początek okresu |
| sober_days_count | integer | NOT NULL, DEFAULT 0 | Liczba dni trzeźwości w okresie |
| sober_days_percentage | decimal(5,2) | NOT NULL, DEFAULT 0 | Procent dni trzeźwości w okresie |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |

*Dodatkowe ograniczenia:*
- `stat_aggregations_unique_per_period`: Zapewnia unikalne agregacje per okres/użytkownik

### System osiągnięć

#### `milestones` - Kamienie milowe trzeźwości
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator kamienia milowego |
| days_value | integer | NOT NULL, UNIQUE | Wartość w dniach |
| name | text | NOT NULL | Nazwa kamienia milowego |
| description | text | | Opis |
| icon_path | text | | Ścieżka do ikony |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |

#### `user_achievements` - Osiągnięcia użytkownika
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator osiągnięcia |
| user_id | uuid | NOT NULL, REFERENCES users(id) | Identyfikator użytkownika |
| milestone_id | uuid | NOT NULL, REFERENCES milestones(id) | Identyfikator kamienia milowego |
| achieved_at | timestamptz | NOT NULL, DEFAULT now() | Data osiągnięcia |

*Dodatkowe ograniczenia:*
- `user_achievements_unique_per_milestone`: Każdy kamień milowy może być osiągnięty tylko raz per użytkownik

### System treści edukacyjnych

#### `educational_content` - Treści edukacyjne
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator treści |
| content_type | text | NOT NULL, CHECK (content_type IN ('quote', 'fact', 'image')) | Typ treści |
| content | text | NOT NULL | Treść |
| extended_content | text | | Rozszerzona treść (dla opcji "Pokaż więcej") |
| category | text | | Kategoria treści |
| min_sobriety_days | integer | DEFAULT 0 | Minimalny staż trzeźwości wymagany do wyświetlenia |
| language | text | DEFAULT 'pl' | Język treści |
| seasonal | boolean | DEFAULT false | Czy treść jest sezonowa |
| seasonal_start_date | date | | Data początkowa treści sezonowej |
| seasonal_end_date | date | | Data końcowa treści sezonowej |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |

#### `displayed_content` - Wyświetlone treści
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator wyświetlenia |
| user_id | uuid | REFERENCES users(id) | Identyfikator użytkownika |
| device_id | uuid | REFERENCES devices(id) | Identyfikator urządzenia |
| content_id | uuid | NOT NULL, REFERENCES educational_content(id) | Identyfikator treści |
| displayed_at | timestamptz | NOT NULL, DEFAULT now() | Data wyświetlenia |

*Dodatkowe ograniczenia:*
- `displayed_content_user_or_device_not_null`: Wymusza, że albo user_id, albo device_id musi być podane

### System wsparcia

#### `help_resources` - Zasoby pomocy
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator zasobu |
| resource_type | text | NOT NULL, CHECK (resource_type IN ('phone', 'group', 'technique', 'online')) | Typ zasobu |
| name | text | NOT NULL | Nazwa zasobu |
| description | text | | Opis |
| contact_info | jsonb | | Dane kontaktowe w elastycznym formacie |
| region | text | | Region geograficzny |
| country | text | DEFAULT 'Poland' | Kraj |
| available_247 | boolean | DEFAULT false | Czy dostępne całodobowo |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |

#### `crisis_patterns` - Wzorce kryzysowe
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator wzorca |
| pattern_name | text | NOT NULL, UNIQUE | Nazwa wzorca |
| pattern_description | text | | Opis wzorca |
| detection_query | text | | Zapytanie SQL do wykrywania wzorca |
| suggested_actions | jsonb | | Sugerowane działania w przypadku wykrycia |
| severity | integer | DEFAULT 1 | Poziom powagi (1-5) |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |

### System ankiet

#### `surveys` - Ankiety
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator ankiety |
| title | text | NOT NULL | Tytuł ankiety |
| description | text | | Opis |
| active | boolean | DEFAULT true | Czy ankieta jest aktywna |
| display_frequency_days | integer | DEFAULT 30 | Częstotliwość wyświetlania w dniach |
| min_app_usage_days | integer | DEFAULT 7 | Minimalny czas korzystania z aplikacji |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |

#### `survey_questions` - Pytania ankietowe
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator pytania |
| survey_id | uuid | NOT NULL, REFERENCES surveys(id) | Identyfikator ankiety |
| question_text | text | NOT NULL | Treść pytania |
| question_type | text | NOT NULL, CHECK (question_type IN ('text', 'single_choice', 'multiple_choice', 'rating')) | Typ pytania |
| options | jsonb | | Opcje odpowiedzi dla pytań wyboru |
| required | boolean | DEFAULT true | Czy odpowiedź jest wymagana |
| display_order | integer | NOT NULL | Kolejność wyświetlania |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |
| updated_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniej aktualizacji |

#### `survey_responses` - Odpowiedzi na ankiety
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator odpowiedzi |
| user_id | uuid | REFERENCES users(id) | Identyfikator użytkownika |
| device_id | uuid | REFERENCES devices(id) | Identyfikator urządzenia |
| question_id | uuid | NOT NULL, REFERENCES survey_questions(id) | Identyfikator pytania |
| response | text | | Tekstowa odpowiedź |
| response_data | jsonb | | Strukturyzowane dane odpowiedzi |
| submitted_at | timestamptz | NOT NULL, DEFAULT now() | Data przesłania odpowiedzi |

*Dodatkowe ograniczenia:*
- `survey_responses_user_or_device_not_null`: Wymusza, że albo user_id, albo device_id musi być podane

#### `survey_displays` - Śledzenie wyświetleń ankiet
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator wyświetlenia |
| user_id | uuid | REFERENCES users(id) | Identyfikator użytkownika |
| device_id | uuid | REFERENCES devices(id) | Identyfikator urządzenia |
| survey_id | uuid | NOT NULL, REFERENCES surveys(id) | Identyfikator ankiety |
| last_displayed_at | timestamptz | NOT NULL, DEFAULT now() | Data ostatniego wyświetlenia |
| display_count | integer | NOT NULL, DEFAULT 1 | Liczba wyświetleń |
| completed | boolean | DEFAULT false | Czy ankieta została wypełniona |
| dismissed | boolean | DEFAULT false | Czy ankieta została odrzucona |

*Dodatkowe ograniczenia:*
- `survey_displays_user_or_device_not_null`: Wymusza, że albo user_id, albo device_id musi być podane
- `survey_displays_unique_per_survey`: Zapewnia jeden rekord per ankieta/użytkownik/urządzenie

### System eksportu danych

#### `data_exports` - Eksporty danych
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator eksportu |
| user_id | uuid | NOT NULL, REFERENCES users(id) | Identyfikator użytkownika |
| export_type | text | NOT NULL, CHECK (export_type IN ('full', 'answers', 'achievements', 'statistics')) | Typ eksportu |
| status | text | NOT NULL, DEFAULT 'pending', CHECK (status IN ('pending', 'processing', 'completed', 'failed')) | Status eksportu |
| file_path | text | | Ścieżka do pliku eksportu |
| requested_at | timestamptz | NOT NULL, DEFAULT now() | Data zgłoszenia eksportu |
| completed_at | timestamptz | | Data zakończenia eksportu |
| error_message | text | | Komunikat błędu (jeśli wystąpił) |

### System synchronizacji

#### `sync_events` - Wydarzenia synchronizacji
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator wydarzenia |
| user_id | uuid | NOT NULL, REFERENCES users(id) | Identyfikator użytkownika |
| device_id | uuid | NOT NULL, REFERENCES devices(id) | Identyfikator urządzenia |
| sync_type | text | NOT NULL, CHECK (sync_type IN ('full', 'answers', 'settings')) | Typ synchronizacji |
| status | text | NOT NULL, CHECK (status IN ('success', 'partial', 'failed')) | Status synchronizacji |
| records_synced | integer | NOT NULL, DEFAULT 0 | Liczba zsynchronizowanych rekordów |
| conflicts_detected | integer | NOT NULL, DEFAULT 0 | Liczba wykrytych konfliktów |
| sync_started_at | timestamptz | NOT NULL, DEFAULT now() | Data rozpoczęcia synchronizacji |
| sync_completed_at | timestamptz | | Data zakończenia synchronizacji |
| error_details | text | | Szczegóły błędu (jeśli wystąpił) |

#### `sync_conflicts` - Konflikty synchronizacji
| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|--------------|------|
| id | uuid | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unikalny identyfikator konfliktu |
| sync_event_id | uuid | NOT NULL, REFERENCES sync_events(id) | Identyfikator wydarzenia synchronizacji |
| table_name | text | NOT NULL | Nazwa tabeli, w której wystąpił konflikt |
| record_id | uuid | NOT NULL | Identyfikator rekordu |
| conflict_type | text | NOT NULL, CHECK (conflict_type IN ('insert', 'update', 'delete')) | Typ konfliktu |
| server_data | jsonb | | Dane na serwerze |
| client_data | jsonb | | Dane na urządzeniu |
| resolution | text | CHECK (resolution IN ('server_wins', 'client_wins', 'merged', 'unresolved')) | Sposób rozwiązania konfliktu |
| resolved_at | timestamptz | | Data rozwiązania konfliktu |
| created_at | timestamptz | NOT NULL, DEFAULT now() | Data utworzenia rekordu |

## 2. Relacje między tabelami

### Relacje jeden-do-wielu (1:N)

- `users` 1:N `devices` - Użytkownik może mieć wiele urządzeń
- `users` 1:N `user_settings` - Użytkownik ma jedno ustawienie (relacja 1:1 przez ograniczenie UNIQUE)
- `users` 1:N `answers` - Użytkownik może mieć wiele odpowiedzi
- `devices` 1:N `answers` - Urządzenie może mieć wiele odpowiedzi
- `users` 1:N `resets` - Użytkownik może mieć wiele resetów
- `users` 1:N `stat_aggregations` - Użytkownik może mieć wiele agregacji statystyk
- `users` 1:N `user_achievements` - Użytkownik może mieć wiele osiągnięć
- `milestones` 1:N `user_achievements` - Kamień milowy może być osiągnięty przez wielu użytkowników
- `users` 1:N `displayed_content` - Użytkownik może mieć wiele wyświetlonych treści
- `devices` 1:N `displayed_content` - Urządzenie może mieć wiele wyświetlonych treści
- `educational_content` 1:N `displayed_content` - Treść może być wyświetlona wielu użytkownikom/urządzeniom
- `users` 1:N `data_exports` - Użytkownik może mieć wiele eksportów danych
- `users` 1:N `sync_events` - Użytkownik może mieć wiele wydarzeń synchronizacji
- `devices` 1:N `sync_events` - Urządzenie może mieć wiele wydarzeń synchronizacji
- `sync_events` 1:N `sync_conflicts` - Wydarzenie synchronizacji może zawierać wiele konfliktów
- `surveys` 1:N `survey_questions` - Ankieta może zawierać wiele pytań
- `survey_questions` 1:N `survey_responses` - Pytanie może mieć wiele odpowiedzi
- `users` 1:N `survey_responses` - Użytkownik może udzielić wielu odpowiedzi
- `devices` 1:N `survey_responses` - Urządzenie może być źródłem wielu odpowiedzi
- `users` 1:N `survey_displays` - Użytkownik może mieć wiele wyświetleń ankiet
- `devices` 1:N `survey_displays` - Urządzenie może mieć wiele wyświetleń ankiet
- `surveys` 1:N `survey_displays` - Ankieta może być wyświetlona wielu użytkownikom/urządzeniom

## 3. Indeksy

### Indeksy dla tabeli `answers`
- `idx_answers_user_id` na `user_id` - Przyspiesza wyszukiwanie odpowiedzi dla konkretnego użytkownika
- `idx_answers_device_id` na `device_id` - Przyspiesza wyszukiwanie odpowiedzi dla konkretnego urządzenia
- `idx_answers_date` na `answer_date` - Przyspiesza wyszukiwanie odpowiedzi według daty
- `idx_answers_type` na `answer_type` - Przyspiesza filtrowanie według typu odpowiedzi

### Indeksy dla tabeli `user_achievements`
- `idx_user_achievements_user_id` na `user_id` - Przyspiesza wyszukiwanie osiągnięć dla konkretnego użytkownika
- `idx_user_achievements_milestone_id` na `milestone_id` - Przyspiesza wyszukiwanie użytkowników z konkretnym osiągnięciem

### Indeksy dla tabeli `displayed_content`
- `idx_displayed_content_user_id` na `user_id` - Przyspiesza wyszukiwanie wyświetlonych treści dla konkretnego użytkownika
- `idx_displayed_content_device_id` na `device_id` - Przyspiesza wyszukiwanie wyświetlonych treści dla konkretnego urządzenia
- `idx_displayed_content_content_id` na `content_id` - Przyspiesza wyszukiwanie użytkowników, którym wyświetlono konkretną treść

### Indeksy dla tabeli `educational_content`
- `idx_educational_content_type` na `content_type` - Przyspiesza filtrowanie treści według typu
- `idx_educational_content_language` na `language` - Przyspiesza filtrowanie treści według języka
- `idx_educational_content_min_sobriety` na `min_sobriety_days` - Przyspiesza filtrowanie treści według minimalnego stażu trzeźwości

### Indeksy dla tabeli `help_resources`
- `idx_help_resources_type` na `resource_type` - Przyspiesza filtrowanie zasobów pomocy według typu
- `idx_help_resources_region` na `region` - Przyspiesza filtrowanie zasobów pomocy według regionu
- `idx_help_resources_country` na `country` - Przyspiesza filtrowanie zasobów pomocy według kraju

### Indeksy dla tabeli `survey_responses`
- `idx_survey_responses_user_id` na `user_id` - Przyspiesza wyszukiwanie odpowiedzi ankietowych dla konkretnego użytkownika
- `idx_survey_responses_device_id` na `device_id` - Przyspiesza wyszukiwanie odpowiedzi ankietowych dla konkretnego urządzenia
- `idx_survey_responses_question_id` na `question_id` - Przyspiesza wyszukiwanie odpowiedzi na konkretne pytanie

## 4. Zasady Row Level Security (RLS)

Wszystkie tabele mają włączone Row Level Security (RLS) w celu zapewnienia dostępu tylko do własnych danych. Poniżej przedstawiono główne polityki:

### Polityki dla tabeli `users`
- Użytkownicy mogą przeglądać tylko swoje dane: `auth.uid() = id`
- Użytkownicy mogą aktualizować tylko swoje dane: `auth.uid() = id`

### Polityki dla tabeli `devices`
- Użytkownicy mogą przeglądać tylko swoje urządzenia: `auth.uid() = user_id`
- Użytkownicy mogą dodawać urządzenia tylko do swojego konta: `auth.uid() = user_id`
- Użytkownicy mogą aktualizować tylko swoje urządzenia: `auth.uid() = user_id`
- Użytkownicy mogą usuwać tylko swoje urządzenia: `auth.uid() = user_id`
- Niezalogowani użytkownicy mogą uzyskać dostęp przez identyfikator urządzenia: `device_identifier = current_setting('request.headers')::json->>'x-device-id'`

### Polityki dla tabeli `user_settings`
- Użytkownicy mogą przeglądać tylko swoje ustawienia: `auth.uid() = user_id`
- Użytkownicy mogą aktualizować tylko swoje ustawienia: `auth.uid() = user_id`
- Użytkownicy mogą dodawać ustawienia tylko do swojego konta: `auth.uid() = user_id`

### Polityki dla tabeli `answers`
- Użytkownicy mogą przeglądać tylko swoje odpowiedzi lub odpowiedzi ze swoich urządzeń
- Użytkownicy mogą dodawać odpowiedzi tylko do swojego konta lub swoich urządzeń

### Polityki dla zasobów publicznych
- Treści edukacyjne są dostępne dla wszystkich: `true`
- Zasoby pomocy są dostępne dla wszystkich: `true`
- Kamienie milowe są dostępne dla wszystkich: `true`

## 5. Dodatkowe wyjaśnienia i decyzje projektowe

### Widoki dla wydajnego obliczania statystyk
- `current_sobriety_streaks` - widok obliczający aktualny licznik dni trzeźwości na podstawie ostatniej daty picia
- `longest_sobriety_streaks` - widok zmaterializowany obliczający najdłuższy okres trzeźwości

### Migracja danych między urządzeniami a kontami użytkowników
- Funkcja `bind_device_to_user` umożliwia łączenie danych z anonimowego urządzenia z kontem użytkownika po rejestracji

### Triggery dla spójności danych
- Automatyczna aktualizacja pola `updated_at` we wszystkich tabelach przy modyfikacji rekordu
- Aktualizacja `last_drinking_date` w tabeli `users` po odpowiedzi "tak"
- Automatyczne wykrywanie i rejestrowanie osiągnięć kamieni milowych

### Rozwiązania nierozstrzygniętych kwestii

#### 1. Metoda obliczania oszczędności finansowych
Zaimplementowano trójwarstwowe podejście w tabeli `user_settings`:
- Pole `alcohol_expense_option` określa metodę obliczania: 
  - 'default' (domyślne statystyki)
  - 'predefined' (predefiniowane grupy konsumpcji)
  - 'custom' (własne wartości)
- Pola `alcohol_expense_amount`, `alcohol_expense_frequency` i `alcohol_expense_currency` przechowują szczegóły

#### 2. Treści edukacyjne
Zaprojektowano tabelę `educational_content` w sposób umożliwiający:
- Przechowywanie statycznych treści bezpośrednio w bazie danych
- Kategoryzację i filtrowanie według różnych kryteriów
- Dodawanie treści sezonowych i powiązanych z konkretnymi kamieniami milowymi
- Przyszłe rozszerzenie o wsparcie wielojęzyczne

#### 3. Funkcja eksportu danych
Zaimplementowano tabelę `data_exports` do śledzenia żądań eksportu danych użytkownika, spełniającą wymogi RODO/GDPR:
- Możliwość eksportu pełnych danych lub tylko wybranych kategorii
- Śledzenie statusu eksportu
- Rejestrowanie ewentualnych błędów

#### 4. Obliczanie licznika dni trzeźwości
Zaimplementowano hybrydowe podejście:
- Kolumna `last_drinking_date` w tabeli `users` przechowuje datę ostatniego spożycia alkoholu
- Widok `current_sobriety_streaks` dynamicznie oblicza aktualny okres trzeźwości na podstawie różnicy między dzisiejszą datą a ostatnią datą picia
- Widok zmaterializowany `longest_sobriety_streaks` przechowuje informacje o najdłuższych okresach trzeźwości
- Triggery automatycznie aktualizują `last_drinking_date` po każdej odpowiedzi "tak"

### Optymalizacja wydajności
- Wykorzystanie indeksów dla najczęściej używanych kolumn w zapytaniach
- Predefiniowane widoki dla złożonych i często wykonywanych obliczeń
- Przechowywanie zagregowanych statystyk w tabeli `stat_aggregations` dla szybkiego dostępu
- Wykorzystanie triggerów do automatycznej aktualizacji danych pochodnych

### Wsparcie dla anonimowych użytkowników i migracji danych
- System wspiera zarówno użytkowników zalogowanych jak i niezalogowanych przez strukturę tabel `users` i `devices`
- Funkcja `bind_device_to_user` umożliwia migrację danych z urządzenia do konta użytkownika po rejestracji
- Elastyczna struktura tabel z opcjonalnymi relacjami między użytkownikami i urządzeniami

### Bezpieczeństwo i prywatność danych
- Minimalna ilość danych osobowych (tylko email)
- Row Level Security (RLS) zapewnia, że użytkownicy mają dostęp tylko do swoich danych
- Zabezpieczenia na poziomie aplikacji (opcjonalny PIN)
- Wspierane procesy RODO/GDPR (eksport i usuwanie danych)

### Skalowalność i przyszły rozwój
- Struktura tabel wspiera planowane przyszłe rozszerzenia (wielojęzyczność, nowe funkcje)
- Możliwość łatwej migracji do pełnego rozwiązania backendowego w przyszłości
- Przechowywanie danych w formacie umożliwiającym łatwą analizę i raportowanie
