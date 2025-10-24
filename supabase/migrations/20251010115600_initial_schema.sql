-- Migration file: 20251010115600_initial_schema.sql
-- Purpose: Initial database schema setup for MamKaca.pl application
-- Created: 2025-10-10

-- Enable UUID extension for generating unique identifiers
create
extension if not exists "uuid-ossp";

------------------------------------------------------------------------------
-- 1. USERS AND DEVICES
------------------------------------------------------------------------------

-- Users table - storing minimal personal information
create table public.users
(
    id                 uuid primary key     default uuid_generate_v4(),
    email              text unique,
    last_drinking_date timestamptz, -- For calculating sobriety streaks
    timezone           text                 default 'Europe/Warsaw',
    created_at         timestamptz not null default now(),
    updated_at         timestamptz not null default now()
);

comment
on table public.users is 'Application users with minimal personal information';

-- Devices table - for anonymous users and multi-device support
create table public.devices
(
    id                uuid primary key     default uuid_generate_v4(),
    user_id           uuid references public.users (id),
    device_identifier text        not null unique,
    device_name       text,
    device_type       text,
    last_sync_at      timestamptz not null default now(),
    created_at        timestamptz not null default now(),
    updated_at        timestamptz not null default now()
);

comment
on table public.devices is 'User devices for anonymous usage and synchronization';

-- User settings table
create table public.user_settings
(
    id                        uuid primary key     default uuid_generate_v4(),
    user_id                   uuid        not null references public.users (id) unique,
    notification_time         time        not null default '09:00:00', -- Default notification at 9:00 AM
    notifications_enabled     boolean     not null default true,
    notification_format       text        not null default 'standard', -- 'standard' or 'discreet'
    pin_code_hash             text,                                    -- For app access protection (optional)
    alcohol_expense_amount    decimal(10, 2),                          -- Amount spent on alcohol
    alcohol_expense_frequency text,                                    -- 'daily', 'weekly', 'monthly'
    alcohol_expense_currency  text                 default 'PLN',
    alcohol_expense_option    text                 default 'default',  -- 'default', 'predefined', 'custom'
    humor_level               text                 default 'standard', -- 'more', 'standard', 'less', 'none'
    hide_app_icon             boolean              default false,      -- Option to hide app icon
    created_at                timestamptz not null default now(),
    updated_at                timestamptz not null default now()
);

comment
on table public.user_settings is 'User preferences and application settings';

------------------------------------------------------------------------------
-- 2. SOBRIETY TRACKING SYSTEM
------------------------------------------------------------------------------

-- Daily answers table
create table public.answers
(
    id            uuid primary key     default uuid_generate_v4(),
    user_id       uuid references public.users (id),
    device_id     uuid references public.devices (id),
    answer_date   date        not null,
    answer_time   time        not null,
    answer_type   text        not null check (answer_type in ('yes', 'no', 'none')),
    answer_source text        not null default 'manual' check (answer_source in ('manual', 'notification')),
    note          text, -- Optional note, especially for 'yes' answers
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now(),

    -- Either user_id or device_id must be provided, but not both null
    constraint answers_user_or_device_not_null check (
        (user_id is not null) or (device_id is not null)
        ),

    -- Ensure one answer per day per user or device
    constraint answers_unique_per_day unique (
                                              user_id,
                                              device_id,
                                              answer_date
        )
);

comment
on table public.answers is 'Daily user responses to the question "Do you have a hangover today?"';

-- Reset without shame table
create table public.resets
(
    id         uuid primary key     default uuid_generate_v4(),
    user_id    uuid        not null references public.users (id),
    reset_date date        not null,
    note       text, -- Optional note about reset reasons
    created_at timestamptz not null default now()
);

comment
on table public.resets is 'Records of "reset without shame" events';

-- Statistical aggregations for efficient reporting
create table public.stat_aggregations
(
    id                    uuid primary key       default uuid_generate_v4(),
    user_id               uuid          not null references public.users (id),
    period_type           text          not null check (period_type in ('day', 'week', 'month', 'year')),
    period_start          date          not null,
    sober_days_count      integer       not null default 0,
    sober_days_percentage decimal(5, 2) not null default 0,
    created_at            timestamptz   not null default now(),
    updated_at            timestamptz   not null default now(),

    -- Ensure one aggregation per period type and start date per user
    constraint stat_aggregations_unique_per_period unique (user_id, period_type, period_start)
);

comment
on table public.stat_aggregations is 'Precalculated statistics for faster reporting';

-- View for current sobriety streaks (dynamic calculation)
create
or replace view current_sobriety_streaks as
select u.id                                                                                         as user_id,
       current_date - coalesce(u.last_drinking_date ::date, u.created_at ::date - interval '1 day') as days_sober
from users u;

comment
on view current_sobriety_streaks is 'Current sobriety streaks calculated dynamically';

-- Materialized view for longest sobriety streaks (updated daily)
create
materialized view longest_sobriety_streaks as
with answer_with_prev as (
    select
        user_id,
        answer_date,
        answer_type,
        lag(answer_type) over (partition by user_id order by answer_date) as prev_answer_type
    from
        answers
    where
        user_id is not null
),
answer_sequences as (
    select
        user_id,
        answer_date,
        answer_type,
        sum(case when answer_type = 'yes' or prev_answer_type = 'yes' then 1 else 0 end) over (partition by user_id order by answer_date) as sequence_group
    from
        answer_with_prev
),
sobriety_periods as (
    select
        user_id,
        min(answer_date) as period_start,
        max(answer_date) as period_end,
        max(answer_date) - min(answer_date) + 1 as period_length
    from
        answer_sequences
    where
        answer_type = 'no'
    group by
        user_id, sequence_group
)
select user_id,
       max(period_length) as longest_streak_days
from sobriety_periods
group by user_id;

comment
on materialized view longest_sobriety_streaks is 'Longest sobriety streaks for each user';

------------------------------------------------------------------------------
-- 3. ACHIEVEMENTS AND MILESTONES SYSTEM
------------------------------------------------------------------------------

-- Milestone definitions
create table public.milestones
(
    id          uuid primary key     default uuid_generate_v4(),
    days_value  integer     not null unique,
    name        text        not null,
    description text,
    icon_path   text,
    created_at  timestamptz not null default now(),
    updated_at  timestamptz not null default now()
);

comment
on table public.milestones is 'Predefined sobriety milestones like 1 day, 7 days, 30 days, etc.';

-- User achievements
create table public.user_achievements
(
    id           uuid primary key     default uuid_generate_v4(),
    user_id      uuid        not null references public.users (id),
    milestone_id uuid        not null references public.milestones (id),
    achieved_at  timestamptz not null default now(),

    -- Ensure each milestone is achieved only once per user
    constraint user_achievements_unique_per_milestone unique (user_id, milestone_id)
);

comment
on table public.user_achievements is 'Records of milestones achieved by users';

------------------------------------------------------------------------------
-- 4. EDUCATIONAL CONTENT SYSTEM
------------------------------------------------------------------------------

-- Educational content
create table public.educational_content
(
    id                  uuid primary key     default uuid_generate_v4(),
    content_type        text        not null check (content_type in ('quote', 'fact', 'image')),
    content             text        not null,
    extended_content    text,                               -- For "Show More" option
    category            text,
    min_sobriety_days   integer              default 0,     -- Minimum sobriety days required to see this content
    language            text                 default 'pl',  -- For future internationalization
    seasonal            boolean              default false, -- For holiday/event specific content
    seasonal_start_date date,                               -- Start date for seasonal content
    seasonal_end_date   date,                               -- End date for seasonal content
    created_at          timestamptz not null default now(),
    updated_at          timestamptz not null default now()
);

comment
on table public.educational_content is 'Educational content about alcohol and sobriety';

-- Content display history for rotation mechanism
create table public.displayed_content
(
    id           uuid primary key     default uuid_generate_v4(),
    user_id      uuid references public.users (id),
    device_id    uuid references public.devices (id),
    content_id   uuid        not null references public.educational_content (id),
    displayed_at timestamptz not null default now(),

    -- Either user_id or device_id must be provided, but not both null
    constraint displayed_content_user_or_device_not_null check (
        (user_id is not null) or (device_id is not null)
        )
);

comment
on table public.displayed_content is 'History of content displayed to users for content rotation';

------------------------------------------------------------------------------
-- 5. SUPPORT RESOURCES SYSTEM
------------------------------------------------------------------------------

-- Help resources
create table public.help_resources
(
    id            uuid primary key     default uuid_generate_v4(),
    resource_type text        not null check (resource_type in ('phone', 'group', 'technique', 'online')),
    name          text        not null,
    description   text,
    contact_info  jsonb,                              -- Flexible storage for different contact types
    region        text,                               -- For geographical filtering
    country       text                 default 'Poland',
    available_247 boolean              default false, -- Indicates if resource is available 24/7
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now()
);

comment
on table public.help_resources is 'Support resources for users in crisis';

-- User help resource interactions
create table public.user_help_interactions
(
    id               uuid primary key     default uuid_generate_v4(),
    user_id          uuid references public.users (id),
    device_id        uuid references public.devices (id),
    resource_id      uuid        not null references public.help_resources (id),
    interaction_type text        not null check (interaction_type in ('viewed', 'clicked', 'contacted', 'saved')),
    interaction_date timestamptz not null default now(),
    notes            text,

    -- Either user_id or device_id must be provided, but not both null
    constraint user_help_interactions_user_or_device_not_null check (
        (user_id is not null) or (device_id is not null)
        )
);

comment
on table public.user_help_interactions is 'Records of user interactions with help resources';

-- Crisis patterns for detection algorithm
create table public.crisis_patterns
(
    id                  uuid primary key     default uuid_generate_v4(),
    pattern_name        text        not null unique,
    pattern_description text,
    detection_query     text,                           -- SQL query for pattern detection
    suggested_actions   jsonb,
    severity            integer              default 1, -- 1-5 scale for prioritization
    created_at          timestamptz not null default now(),
    updated_at          timestamptz not null default now()
);

comment
on table public.crisis_patterns is 'Patterns for detecting user crisis situations';

-- User crisis detections
create table public.crisis_detections
(
    id               uuid primary key     default uuid_generate_v4(),
    user_id          uuid        not null references public.users (id),
    pattern_id       uuid        not null references public.crisis_patterns (id),
    detected_at      timestamptz not null default now(),
    resolved         boolean              default false,
    resolved_at      timestamptz,
    resolution_notes text,
    actions_taken    jsonb
);

comment
on table public.crisis_detections is 'Records of crisis patterns detected for users';

------------------------------------------------------------------------------
-- 6. FEEDBACK AND SURVEYS SYSTEM
------------------------------------------------------------------------------

-- Surveys
create table public.surveys
(
    id                     uuid primary key     default uuid_generate_v4(),
    title                  text        not null,
    description            text,
    active                 boolean              default true,
    display_frequency_days integer              default 30, -- How often to show this survey
    min_app_usage_days     integer              default 7,  -- Minimum days using app before showing survey
    created_at             timestamptz not null default now(),
    updated_at             timestamptz not null default now()
);

comment
on table public.surveys is 'In-app feedback surveys';

-- Survey questions
create table public.survey_questions
(
    id            uuid primary key     default uuid_generate_v4(),
    survey_id     uuid        not null references public.surveys (id),
    question_text text        not null,
    question_type text        not null check (question_type in ('text', 'single_choice', 'multiple_choice', 'rating')),
    options       jsonb, -- For choice questions
    required      boolean              default true,
    display_order integer     not null,
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now()
);

comment
on table public.survey_questions is 'Questions for feedback surveys';

-- Survey responses
create table public.survey_responses
(
    id            uuid primary key     default uuid_generate_v4(),
    user_id       uuid references public.users (id),
    device_id     uuid references public.devices (id),
    question_id   uuid        not null references public.survey_questions (id),
    response      text,
    response_data jsonb, -- For structured responses
    submitted_at  timestamptz not null default now(),

    -- Either user_id or device_id must be provided, but not both null
    constraint survey_responses_user_or_device_not_null check (
        (user_id is not null) or (device_id is not null)
        )
);

comment
on table public.survey_responses is 'User responses to survey questions';

-- Survey display tracking
create table public.survey_displays
(
    id                uuid primary key     default uuid_generate_v4(),
    user_id           uuid references public.users (id),
    device_id         uuid references public.devices (id),
    survey_id         uuid        not null references public.surveys (id),
    last_displayed_at timestamptz not null default now(),
    display_count     integer     not null default 1,
    completed         boolean              default false,
    dismissed         boolean              default false,

    -- Either user_id or device_id must be provided, but not both null
    constraint survey_displays_user_or_device_not_null check (
        (user_id is not null) or (device_id is not null)
        ),

    -- Ensure one record per survey per user/device
    constraint survey_displays_unique_per_survey unique (
                                                         user_id,
                                                         device_id,
                                                         survey_id
        )
);

comment
on table public.survey_displays is 'Tracking when surveys are displayed to users';

------------------------------------------------------------------------------
-- 7. DATA EXPORT SYSTEM
------------------------------------------------------------------------------

-- Export requests tracking
create table public.data_exports
(
    id            uuid primary key     default uuid_generate_v4(),
    user_id       uuid        not null references public.users (id),
    export_type   text        not null check (export_type in ('full', 'answers', 'achievements', 'statistics')),
    status        text        not null default 'pending' check (status in ('pending', 'processing', 'completed', 'failed')),
    file_path     text,
    requested_at  timestamptz not null default now(),
    completed_at  timestamptz,
    error_message text
);

comment
on table public.data_exports is 'Tracking user data export requests';

------------------------------------------------------------------------------
-- 8. SYNCHRONIZATION SYSTEM
------------------------------------------------------------------------------

-- Sync events logging
create table public.sync_events
(
    id                 uuid primary key     default uuid_generate_v4(),
    user_id            uuid        not null references public.users (id),
    device_id          uuid        not null references public.devices (id),
    sync_type          text        not null check (sync_type in ('full', 'answers', 'settings')),
    status             text        not null check (status in ('success', 'partial', 'failed')),
    records_synced     integer     not null default 0,
    conflicts_detected integer     not null default 0,
    sync_started_at    timestamptz not null default now(),
    sync_completed_at  timestamptz,
    error_details      text
);

comment
on table public.sync_events is 'Logging synchronization events between devices';

-- Conflict resolution table
create table public.sync_conflicts
(
    id            uuid primary key     default uuid_generate_v4(),
    sync_event_id uuid        not null references public.sync_events (id),
    table_name    text        not null,
    record_id     uuid        not null,
    conflict_type text        not null check (conflict_type in ('insert', 'update', 'delete')),
    server_data   jsonb,
    client_data   jsonb,
    resolution    text check (resolution in ('server_wins', 'client_wins', 'merged', 'unresolved')),
    resolved_at   timestamptz,
    created_at    timestamptz not null default now()
);

comment
on table public.sync_conflicts is 'Details of synchronization conflicts for resolution';

------------------------------------------------------------------------------
-- 9. INDEXES FOR PERFORMANCE
------------------------------------------------------------------------------

-- Answers table indexes
create index idx_answers_user_id on public.answers (user_id);
create index idx_answers_device_id on public.answers (device_id);
create index idx_answers_date on public.answers (answer_date);
create index idx_answers_type on public.answers (answer_type);

-- User achievements indexes
create index idx_user_achievements_user_id on public.user_achievements (user_id);
create index idx_user_achievements_milestone_id on public.user_achievements (milestone_id);

-- Displayed content indexes
create index idx_displayed_content_user_id on public.displayed_content (user_id);
create index idx_displayed_content_device_id on public.displayed_content (device_id);
create index idx_displayed_content_content_id on public.displayed_content (content_id);

-- Educational content indexes
create index idx_educational_content_type on public.educational_content (content_type);
create index idx_educational_content_language on public.educational_content (language);
create index idx_educational_content_min_sobriety on public.educational_content (min_sobriety_days);

-- Help resources indexes
create index idx_help_resources_type on public.help_resources (resource_type);
create index idx_help_resources_region on public.help_resources (region);
create index idx_help_resources_country on public.help_resources (country);

-- Survey response indexes
create index idx_survey_responses_user_id on public.survey_responses (user_id);
create index idx_survey_responses_device_id on public.survey_responses (device_id);
create index idx_survey_responses_question_id on public.survey_responses (question_id);

-- User help interactions indexes
create index idx_user_help_interactions_user_id on public.user_help_interactions (user_id);
create index idx_user_help_interactions_device_id on public.user_help_interactions (device_id);
create index idx_user_help_interactions_resource_id on public.user_help_interactions (resource_id);

-- Crisis detections indexes
create index idx_crisis_detections_user_id on public.crisis_detections (user_id);
create index idx_crisis_detections_pattern_id on public.crisis_detections (pattern_id);

------------------------------------------------------------------------------
-- 10. ROW LEVEL SECURITY (RLS)
------------------------------------------------------------------------------

-- Row level security disabled for initial development
-- Uncomment below to enable RLS when ready for production
/*
alter table public.users enable row level security;
alter table public.devices enable row level security;
alter table public.user_settings enable row level security;
alter table public.answers enable row level security;
alter table public.resets enable row level security;
alter table public.stat_aggregations enable row level security;
alter table public.milestones enable row level security;
alter table public.user_achievements enable row level security;
alter table public.educational_content enable row level security;
alter table public.displayed_content enable row level security;
alter table public.help_resources enable row level security;
alter table public.user_help_interactions enable row level security;
alter table public.crisis_patterns enable row level security;
alter table public.crisis_detections enable row level security;
alter table public.surveys enable row level security;
alter table public.survey_questions enable row level security;
alter table public.survey_responses enable row level security;
alter table public.survey_displays enable row level security;
alter table public.data_exports enable row level security;
alter table public.sync_events enable row level security;
alter table public.sync_conflicts enable row level security;
*/

-- Policies will be defined in a separate migration file when RLS is enabled

-- Function to provide a zero UUID (replacement for uuid_nil function)
create
or replace function uuid_zero()
returns uuid as $$
begin
return '00000000-0000-0000-0000-000000000000'::uuid;
end;
$$
language plpgsql immutable;

comment
on function uuid_zero() is 'Returns a zero UUID value (00000000-0000-0000-0000-000000000000) for use in constraints and queries when handling NULL UUIDs';

-- Functions for device/user binding and data migration
create
or replace function bind_device_to_user(device_id uuid, user_id uuid)
returns void as $$
begin
    -- Update the device record with the user_id
update public.devices
set user_id    = $2,
    updated_at = now()
where id = $1;

-- Update all records in answers table that reference this device
update public.answers
set user_id    = $2,
    updated_at = now()
where device_id = $1
  and user_id is null;

-- Update all records in displayed_content table that reference this device
update public.displayed_content
set user_id    = $2,
    updated_at = now()
where device_id = $1
  and user_id is null;

-- Update all records in survey_responses table that reference this device
update public.survey_responses
set user_id    = $2,
    updated_at = now()
where device_id = $1
  and user_id is null;

-- Update all records in survey_displays table that reference this device
update public.survey_displays
set user_id    = $2,
    updated_at = now()
where device_id = $1
  and user_id is null;

-- Update all records in user_help_interactions table that reference this device
update public.user_help_interactions
set user_id    = $2,
    updated_at = now()
where device_id = $1
  and user_id is null;
end;
$$
language plpgsql security definer;

------------------------------------------------------------------------------
-- 11. TRIGGERS FOR DATA CONSISTENCY
------------------------------------------------------------------------------

-- Function to update the timestamp in the updated_at column
create
or replace function update_modified_column()
returns trigger as $$
begin
    new.updated_at
= now();
return new;
end;
$$
language plpgsql;

-- Apply the trigger to all tables with updated_at column
create trigger update_users_modtime
    before update
    on public.users
    for each row execute function update_modified_column();

create trigger update_devices_modtime
    before update
    on public.devices
    for each row execute function update_modified_column();

-- Similar triggers for all other tables with updated_at column would be created

-- Trigger to update user's last_drinking_date when 'yes' answer is provided
create
or replace function update_last_drinking_date()
returns trigger as $$
begin
    if
new.answer_type = 'yes' and new.user_id is not null then
update public.users
set last_drinking_date = new.created_at
where id = new.user_id;
end if;
return new;
end;
$$
language plpgsql;

create trigger update_last_drinking_date_trigger
    after insert or
update on public.answers
    for each row execute function update_last_drinking_date();

-- Trigger to check for and record milestone achievements
create
or replace function check_milestone_achievements()
returns trigger as $$
declare
days_sober integer;
milestone_rec
record;
begin
    -- Calculate current sobriety days
select current_date - coalesce(u.last_drinking_date ::date, u.created_at ::date - 1)
into days_sober
from users u
where u.id = new.user_id;

-- Check for eligible milestones
for milestone_rec in
select *
from milestones
where days_value <= days_sober
  and not exists (select 1
                  from user_achievements
                  where user_id = new.user_id
                    and milestone_id = milestones.id)
    loop
-- Record achievement
insert
into user_achievements (user_id, milestone_id)
values (new.user_id, milestone_rec.id);
end loop;

return new;
end;
$$
language plpgsql;

create trigger check_milestone_achievements_trigger
    after insert or
update on public.answers
    for each row
    when (new.answer_type = 'no' and new.user_id is not null)
    execute function check_milestone_achievements();

-- Initialize with default milestone values
insert into public.milestones (days_value, name, description)
values (1, 'First Day Sober', 'Congratulations on your first day of sobriety!'),
       (7, 'One Week Milestone', 'You''ve been sober for a full week!'),
       (14, 'Two Weeks Strong', 'Two weeks of sobriety is a significant achievement.'),
       (30, 'One Month Milestone', 'A full month of sobriety shows your commitment.'),
       (60, 'Two Months Sober', 'Two months of continuous sobriety is impressive.'),
       (90, 'Three Months Achievement', 'Three months sober is a major milestone.'),
       (100, 'Century Club', 'You''ve reached 100 days of sobriety!'),
       (180, 'Half Year Sobriety', 'Six months of continuous sobriety is remarkable.'),
       (365, 'One Year Sober', 'A full year of sobriety is an incredible achievement.');

-- Users
insert into public.users (id, email, last_drinking_date, timezone)
values ('00000000-0000-4000-8000-000000000001', 'alice@example.com', '2024-12-24T18:00:00+00'::timestamptz,
        'Europe/Warsaw'),
       ('00000000-0000-4000-8000-000000000002', 'bob@example.com', '2025-01-05T09:00:00+00'::timestamptz,
        'Europe/Warsaw');

-- Devices
insert into public.devices (id, user_id, device_identifier, device_name, device_type, last_sync_at)
values ('00000000-0000-4000-8000-000000000101', '00000000-0000-4000-8000-000000000001', 'mamkaca-alice-iphone',
        'Alice iPhone', 'ios', '2025-01-15T06:30:00+00'::timestamptz),
       ('00000000-0000-4000-8000-000000000102', '00000000-0000-4000-8000-000000000002', 'mamkaca-bob-pixel',
        'Bob Pixel', 'android', '2025-01-12T08:00:00+00'::timestamptz);

-- User settings
insert into public.user_settings (user_id, notification_time, notifications_enabled, notification_format, pin_code_hash,
                                  alcohol_expense_amount,
                                  alcohol_expense_frequency, alcohol_expense_currency, alcohol_expense_option,
                                  humor_level, hide_app_icon)
values ('00000000-0000-4000-8000-000000000001', '08:00:00'::time, true, 'standard', null, 250.00, 'monthly', 'PLN',
        'default', 'standard', false),
       ('00000000-0000-4000-8000-000000000002', '07:30:00'::time, true, 'discreet', null, 120.00, 'weekly', 'PLN',
        'custom', 'less', false);

-- Answers
insert into public.answers (id, user_id, device_id, answer_date, answer_time, answer_type, answer_source, note)
values ('00000000-0000-4000-8000-000000001001', '00000000-0000-4000-8000-000000000001',
        '00000000-0000-4000-8000-000000000101', '2025-01-10', '07:45:00'::time, 'no', 'manual',
        'Feeling strong today.'),
       ('00000000-0000-4000-8000-000000001002', '00000000-0000-4000-8000-000000000001',
        '00000000-0000-4000-8000-000000000101', '2025-01-11',
        '07:50:00'::time, 'yes', 'manual', 'Celebrated with friends.'),
       ('00000000-0000-4000-8000-000000001003', '00000000-0000-4000-8000-000000000002',
        '00000000-0000-4000-8000-000000000102', '2025-01-10',
        '08:15:00'::time, 'no', 'notification', null);

-- Resets
insert into public.resets (id, user_id, reset_date, note)
values ('00000000-0000-4000-8000-000000002001', '00000000-0000-4000-8000-000000000001', '2025-01-11',
        'Recommitting after social event.');

-- Statistics
insert into public.stat_aggregations (id, user_id, period_type, period_start, sober_days_count, sober_days_percentage)
values ('00000000-0000-4000-8000-000000003001', '00000000-0000-4000-8000-000000000001', 'month', '2025-01-01', 9,
        90.00),
       ('00000000-0000-4000-8000-000000003002', '00000000-0000-4000-8000-000000000002', 'week', '2025-01-06', 5,
        100.00);

-- Educational content
insert into public.educational_content (id, content_type, content, extended_content, category, min_sobriety_days,
                                        seasonal)
values ('00000000-0000-4000-8000-000000004001', 'quote', 'Every sober day matters.',
        'Celebrate small wins and log your progress.', 'motivation', 0, false),
       ('00000000-0000-4000-8000-000000004002', 'fact', 'Three alcohol-free days improve sleep quality.', 'Restful sleep accelerates recovery and boosts
  mood.', 'health', 3, false);

-- Displayed content history
insert into public.displayed_content (id, user_id, content_id, displayed_at)
values ('00000000-0000-4000-8000-000000005001', '00000000-0000-4000-8000-000000000001',
        '00000000-0000-4000-8000-000000004001', '2025-01-10T07:50:00+00'::timestamptz);

-- Help resources
insert into public.help_resources (id, resource_type, name, description, contact_info, region, country, available_247)
values ('00000000-0000-4000-8000-000000006001', 'phone', 'Sobriety Support Hotline',
        '24/7 hotline for immediate support.', '{
    "phone": "+48-123-456-789"
  }',
        'Mazowieckie', 'Poland', true);

-- User help interactions
insert into public.user_help_interactions (id, user_id, resource_id, interaction_type, interaction_date, notes)
values ('00000000-0000-4000-8000-000000007001', '00000000-0000-4000-8000-000000000001',
        '00000000-0000-4000-8000-000000006001', 'contacted', '2025-01-11T09:30:00+00'::timestamptz, 'Phone call with counselor.');

-- Crisis patterns
insert into public.crisis_patterns (id, pattern_name, pattern_description, detection_query, suggested_actions, severity)
values ('00000000-0000-4000-8000-000000008001', 'Frequent YES answers',
        'Detects repeated YES answers within the last week.', 'select 1;', '{
    "suggestions": [
      "Notify coach",
      "Serve extra educational content"
    ]
  }', 4);

-- Crisis detections
insert into public.crisis_detections (id, user_id, pattern_id, detected_at, actions_taken)
values ('00000000-0000-4000-8000-000000009001', '00000000-0000-4000-8000-000000000001',
        '00000000-0000-4000-8000-000000008001', '2025-01-11T10:00:00+00'::timestamptz, '{
    "action": "Coach notified"
  }');

-- Surveys
insert into public.surveys (id, title, description, display_frequency_days, min_app_usage_days)
values ('00000000-0000-4000-8000-00000000a001', 'How is MamKaca working for you?',
        'Quick satisfaction survey for early adopters.', 30, 7);

-- Survey questions
insert into public.survey_questions (id, survey_id, question_text, question_type, options, required, display_order)
values ('00000000-0000-4000-8000-00000000b001', '00000000-0000-4000-8000-00000000a001',
        'Rate the motivational messages.', 'rating', '{
    "scale": 5
  }', true, 1);

-- Survey displays
insert into public.survey_displays (id, user_id, survey_id, last_displayed_at, display_count, completed)
values ('00000000-0000-4000-8000-00000000c001', '00000000-0000-4000-8000-000000000002',
        '00000000-0000-4000-8000-00000000a001', '2025-01-12T08:30:00+00'::timestamptz, 1, true);

-- Survey responses
insert into public.survey_responses (id, user_id, question_id, response, response_data, submitted_at)
values ('00000000-0000-4000-8000-00000000d001', '00000000-0000-4000-8000-000000000002',
        '00000000-0000-4000-8000-00000000b001', '5', '{
    "comment": "Great motivational messages!"
  }', '2025-01-12T08:32:00+00'::timestamptz);

-- Data export requests
insert into public.data_exports (id, user_id, export_type, status, file_path, requested_at, completed_at)
values ('00000000-0000-4000-8000-00000000e001', '00000000-0000-4000-8000-000000000001', 'answers', 'completed', 'exports/users/000000000001/answers-2025-01-
  11.csv', '2025-01-11T06:00:00+00'::timestamptz, '2025-01-11T06:05:00+00'::timestamptz);

-- Sync events
insert into public.sync_events (id, user_id, device_id, sync_type, status, records_synced, conflicts_detected,
                                sync_started_at, sync_completed_at)
values ('00000000-0000-4000-8000-00000000f001', '00000000-0000-4000-8000-000000000001',
        '00000000-0000-4000-8000-000000000101', 'full', 'success', 42, 1,
        '2025-01-10T07:00:00+00'::timestamptz, '2025-01-10T07:02:00+00'::timestamptz);

-- Sync conflicts
insert into public.sync_conflicts (id, sync_event_id, table_name, record_id, conflict_type, server_data, client_data,
                                   resolution, resolved_at)
values ('00000000-0000-4000-8000-000000010001', '00000000-0000-4000-8000-00000000f001', 'answers',
        '00000000-0000-4000-8000-000000001002', 'update',
        '{
          "answer_type": "no"
        }', '{
    "answer_type": "yes"
  }', 'server_wins', '2025-01-10T07:03:00+00'::timestamptz);

-- User achievements (uses milestone seeded above)
insert into public.user_achievements (id, user_id, milestone_id, achieved_at)
select '00000000-0000-4000-8000-000000011001'::uuid, '00000000-0000-4000-8000-000000000001'::uuid, m.id,
       '2025-01-10T08:00:00+00'::timestamptz
from public.milestones m
where m.days_value = 7
    and not exists (
          select 1
          from public.user_achievements ua
          where ua.user_id = '00000000-0000-4000-8000-000000000001'
            and ua.milestone_id = m.id
      );
