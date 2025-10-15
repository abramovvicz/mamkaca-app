-- Migration file: 20251012092437_add_rls_policies.sql
-- Purpose: Add Row Level Security policies to the database
-- Created: 2025-10-12

------------------------------------------------------------------------------
-- ROW LEVEL SECURITY (RLS) POLICIES
------------------------------------------------------------------------------

comment on migration is 'Adding Row Level Security policies to all tables';

-- Users policies
create policy "Users can view their own data"
on public.users for select
using (auth.uid() = id);

create policy "Users can update their own data"
on public.users for update
using (auth.uid() = id);

-- Devices policies
create policy "Users can view their own devices"
on public.devices for select
using (auth.uid() = user_id);

create policy "Users can insert their own devices"
on public.devices for insert
with check (auth.uid() = user_id);

create policy "Users can update their own devices"
on public.devices for update
using (auth.uid() = user_id);

create policy "Users can delete their own devices"
on public.devices for delete
using (auth.uid() = user_id);

create policy "Anonymous can use device_identifier"
on public.devices for select
using (device_identifier = current_setting('request.headers')::json->>'x-device-id');

-- User settings policies
create policy "Users can view their own settings"
on public.user_settings for select
using (auth.uid() = user_id);

create policy "Users can update their own settings"
on public.user_settings for update
using (auth.uid() = user_id);

create policy "Users can insert their own settings"
on public.user_settings for insert
with check (auth.uid() = user_id);

-- Answers policies
create policy "Users can view their own answers"
on public.answers for select
using (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid())
);

create policy "Users can insert their own answers"
on public.answers for insert
with check (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid()) or
  device_id::text = current_setting('request.headers')::json->>'x-device-id'
);

-- Resets policies
create policy "Users can view their own resets"
on public.resets for select
using (auth.uid() = user_id);

create policy "Users can insert their own resets"
on public.resets for insert
with check (auth.uid() = user_id);

-- Stats aggregation policies
create policy "Users can view their own stats"
on public.stat_aggregations for select
using (auth.uid() = user_id);

-- Achievements policies
create policy "Users can view their own achievements"
on public.user_achievements for select
using (auth.uid() = user_id);

-- Public access policies for resources
create policy "Public can view educational content"
on public.educational_content for select
using (true);

create policy "Public can view help resources"
on public.help_resources for select
using (true);

create policy "Public can view milestones"
on public.milestones for select
using (true);

-- Content display policies
create policy "Users can view their displayed content"
on public.displayed_content for select
using (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid())
);

create policy "Users can insert displayed content"
on public.displayed_content for insert
with check (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid()) or
  device_id::text = current_setting('request.headers')::json->>'x-device-id'
);

-- User help interactions policies
create policy "Users can view their own help interactions"
on public.user_help_interactions for select
using (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid())
);

create policy "Users can insert their own help interactions"
on public.user_help_interactions for insert
with check (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid()) or
  device_id::text = current_setting('request.headers')::json->>'x-device-id'
);

-- Crisis pattern policies
create policy "Public can view crisis patterns"
on public.crisis_patterns for select
using (true);

-- Crisis detections policies
create policy "Users can view their own crisis detections"
on public.crisis_detections for select
using (auth.uid() = user_id);

create policy "Admin can insert crisis detections"
on public.crisis_detections for insert
with check (auth.role() = 'service_role');

create policy "Admin can update crisis detections"
on public.crisis_detections for update
using (auth.role() = 'service_role');

-- Survey policies
create policy "Public can view active surveys"
on public.surveys for select
using (active = true);

create policy "Public can view survey questions"
on public.survey_questions for select
using (exists (select 1 from public.surveys where id = survey_id and active = true));

-- Survey responses policies
create policy "Users can view their own survey responses"
on public.survey_responses for select
using (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid())
);

create policy "Users can insert their own survey responses"
on public.survey_responses for insert
with check (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid()) or
  device_id::text = current_setting('request.headers')::json->>'x-device-id'
);

-- Survey display policies
create policy "Users can view their survey displays"
on public.survey_displays for select
using (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid())
);

create policy "Users can update their survey displays"
on public.survey_displays for update
using (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid())
);

create policy "Users can insert survey displays"
on public.survey_displays for insert
with check (
  auth.uid() = user_id or 
  exists (select 1 from public.devices where id = device_id and user_id = auth.uid()) or
  device_id::text = current_setting('request.headers')::json->>'x-device-id'
);

-- Data export policies
create policy "Users can view their own data exports"
on public.data_exports for select
using (auth.uid() = user_id);

create policy "Users can request their own data exports"
on public.data_exports for insert
with check (auth.uid() = user_id);

-- Sync events policies
create policy "Users can view their own sync events"
on public.sync_events for select
using (auth.uid() = user_id);

create policy "Users can insert their own sync events"
on public.sync_events for insert
with check (auth.uid() = user_id);

-- Sync conflicts policies
create policy "Users can view their own sync conflicts"
on public.sync_conflicts for select
using (
  exists (
    select 1 from public.sync_events 
    where id = sync_event_id and user_id = auth.uid()
  )
);

create policy "Users can update their own sync conflicts"
on public.sync_conflicts for update
using (
  exists (
    select 1 from public.sync_events 
    where id = sync_event_id and user_id = auth.uid()
  )
);
