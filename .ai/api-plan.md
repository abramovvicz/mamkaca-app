# REST API Plan

## 1. Resources
- **Users** - Maps to `users` table, represents user accounts
- **Devices** - Maps to `devices` table, represents user devices
- **UserSettings** - Maps to `user_settings` table, contains user preferences
- **Answers** - Maps to `answers` table, stores daily responses to the "Masz dziś kaca?" question
- **Streaks** - Maps to `current_sobriety_streaks` view, represents current sobriety periods
- **Statistics** - Maps to `stat_aggregations` table, contains aggregated user statistics
- **Resets** - Maps to `resets` table, represents manual sobriety resets
- **Achievements** - Maps to `user_achievements` table, tracks milestone accomplishments
- **Milestones** - Maps to `milestones` table, defines sobriety achievement milestones
- **EducationalContent** - Maps to `educational_content` table, stores educational materials
- **DisplayedContent** - Maps to `displayed_content` table, tracks which content was shown to users
- **HelpResources** - Maps to `help_resources` table, contains support resources
- **CrisisPatterns** - Maps to `crisis_patterns` table, defines patterns for crisis detection
- **CrisisDetections** - Maps to `crisis_detections` table, tracks detected crisis situations
- **Surveys** - Maps to `surveys` table, contains feedback surveys
- **SurveyQuestions** - Maps to `survey_questions` table, stores survey questions
- **SurveyResponses** - Maps to `survey_responses` table, contains user responses to surveys
- **SurveyDisplays** - Maps to `survey_displays` table, tracks survey display status
- **DataExports** - Maps to `data_exports` table, manages data export requests
- **SyncEvents** - Maps to `sync_events` table, tracks synchronization operations
- **SyncConflicts** - Maps to `sync_conflicts` table, records synchronization conflicts

## 2. Endpoints

### Authentication

#### Register
- Method: POST
- Path: `/api/auth/register`
- Description: Register a new user account
- Request Body:
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "timezone": "Europe/Warsaw"
  }
  ```
- Response:
  ```json
  {
    "id": "uuid",
    "email": "user@example.com",
    "created_at": "2025-10-12T09:45:03Z"
  }
  ```
- Success: 201 Created
- Errors: 
  - 400 Bad Request - Invalid email or password
  - 409 Conflict - Email already in use

#### Login
- Method: POST
- Path: `/api/auth/login`
- Description: Authenticate a user and get access tokens
- Request Body:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- Response:
  ```json
  {
    "access_token": "jwt_token",
    "refresh_token": "refresh_token",
    "user": {
      "id": "uuid",
      "email": "user@example.com"
    }
  }
  ```
- Success: 200 OK
- Errors:
  - 400 Bad Request - Missing credentials
  - 401 Unauthorized - Invalid credentials

#### Logout
- Method: POST
- Path: `/api/auth/logout`
- Description: Invalidate current session
- Request Headers: Authorization: Bearer {token}
- Response: 204 No Content
- Errors:
  - 401 Unauthorized - Invalid token

#### Reset Password
- Method: POST
- Path: `/api/auth/reset-password`
- Description: Send password reset email
- Request Body:
  ```json
  {
    "email": "user@example.com"
  }
  ```
- Response: 204 No Content
- Errors:
  - 400 Bad Request - Invalid email

### Device Management

#### Register Device
- Method: POST
- Path: `/api/devices`
- Description: Register a new device
- Request Body:
  ```json
  {
    "device_identifier": "unique-device-id",
    "device_name": "iPhone 13",
    "device_type": "ios"
  }
  ```
- Response:
  ```json
  {
    "id": "uuid",
    "device_identifier": "unique-device-id",
    "device_name": "iPhone 13",
    "device_type": "ios",
    "created_at": "2025-10-12T09:45:03Z"
  }
  ```
- Success: 201 Created
- Errors:
  - 400 Bad Request - Invalid device data
  - 409 Conflict - Device already registered

#### Link Device to User
- Method: POST
- Path: `/api/devices/link`
- Description: Link an anonymous device to a user account
- Request Headers: Authorization: Bearer {token}
- Request Body:
  ```json
  {
    "device_id": "device-uuid"
  }
  ```
- Response: 204 No Content
- Errors:
  - 400 Bad Request - Invalid device ID
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Device not found

#### Get User Devices
- Method: GET
- Path: `/api/devices`
- Description: Get all devices linked to the authenticated user
- Request Headers: Authorization: Bearer {token}
- Response:
  ```json
  [
    {
      "id": "uuid",
      "device_identifier": "unique-device-id",
      "device_name": "iPhone 13",
      "device_type": "ios",
      "last_sync_at": "2025-10-12T09:45:03Z"
    }
  ]
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated

### User Settings

#### Get Settings
- Method: GET
- Path: `/api/settings`
- Description: Get user settings
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Response:
  ```json
  {
    "id": "uuid",
    "notification_time": "09:00:00",
    "notifications_enabled": true,
    "notification_format": "standard",
    "alcohol_expense_amount": 50.00,
    "alcohol_expense_frequency": "weekly",
    "alcohol_expense_currency": "PLN",
    "alcohol_expense_option": "custom",
    "humor_level": "standard",
    "hide_app_icon": false
  }
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Settings not found

#### Update Settings
- Method: PATCH
- Path: `/api/settings`
- Description: Update user settings
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Request Body: Any subset of settings fields
  ```json
  {
    "notification_time": "08:00:00",
    "humor_level": "high"
  }
  ```
- Response: Updated settings object
- Success: 200 OK
- Errors:
  - 400 Bad Request - Invalid settings data
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Settings not found

### Daily Answers

#### Submit Answer
- Method: POST
- Path: `/api/answers`
- Description: Submit answer to daily "Masz dziś kaca?" question
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Request Body:
  ```json
  {
    "answer_date": "2025-10-12",
    "answer_time": "09:15:00",
    "answer_type": "yes",
    "answer_source": "notification",
    "note": "Optional note about the answer"
  }
  ```
- Response:
  ```json
  {
    "id": "uuid",
    "answer_date": "2025-10-12",
    "answer_time": "09:15:00",
    "answer_type": "yes",
    "answer_source": "notification",
    "note": "Optional note about the answer",
    "created_at": "2025-10-12T09:15:03Z"
  }
  ```
- Success: 201 Created
- Errors:
  - 400 Bad Request - Invalid answer data
  - 401 Unauthorized - Not authenticated
  - 409 Conflict - Already answered today

#### Get Answers
- Method: GET
- Path: `/api/answers`
- Description: Get user's answer history
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Query Parameters: 
  - start_date: Filter by start date (YYYY-MM-DD)
  - end_date: Filter by end date (YYYY-MM-DD)
  - answer_type: Filter by answer type (yes/no/none)
  - limit: Limit results (default: 30)
  - offset: Offset for pagination
- Response:
  ```json
  {
    "total": 60,
    "items": [
      {
        "id": "uuid",
        "answer_date": "2025-10-12",
        "answer_time": "09:15:00",
        "answer_type": "no",
        "answer_source": "notification",
        "note": null,
        "created_at": "2025-10-12T09:15:03Z"
      }
    ]
  }
  ```
- Success: 200 OK
- Errors:
  - 400 Bad Request - Invalid query parameters
  - 401 Unauthorized - Not authenticated

### Sobriety Streaks

#### Get Current Streak
- Method: GET
- Path: `/api/streaks/current`
- Description: Get current sobriety streak
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Response:
  ```json
  {
    "days_sober": 30,
    "since_date": "2025-09-12"
  }
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated

#### Get Longest Streak
- Method: GET
- Path: `/api/streaks/longest`
- Description: Get longest sobriety streak
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Response:
  ```json
  {
    "longest_streak_days": 45,
    "start_date": "2025-05-01",
    "end_date": "2025-06-15"
  }
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated

#### Reset Streak
- Method: POST
- Path: `/api/resets`
- Description: Create a "Reset bez wstydu" record
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Request Body:
  ```json
  {
    "reset_date": "2025-10-12",
    "note": "Optional note about reset"
  }
  ```
- Response:
  ```json
  {
    "id": "uuid",
    "reset_date": "2025-10-12",
    "note": "Optional note about reset",
    "created_at": "2025-10-12T09:45:03Z"
  }
  ```
- Success: 201 Created
- Errors:
  - 400 Bad Request - Invalid reset data
  - 401 Unauthorized - Not authenticated

### Statistics

#### Get Statistics
- Method: GET
- Path: `/api/statistics`
- Description: Get user's sobriety statistics
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Query Parameters:
  - period_type: day, week, month, year (default: month)
  - start_date: Filter by start date (YYYY-MM-DD)
  - limit: Limit results (default: 12)
- Response:
  ```json
  [
    {
      "period_type": "month",
      "period_start": "2025-09-01",
      "sober_days_count": 25,
      "sober_days_percentage": 83.33
    }
  ]
  ```
- Success: 200 OK
- Errors:
  - 400 Bad Request - Invalid query parameters
  - 401 Unauthorized - Not authenticated

#### Get Savings Estimate
- Method: GET
- Path: `/api/statistics/savings`
- Description: Get estimated financial savings from sobriety
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Response:
  ```json
  {
    "currency": "PLN",
    "daily_saving": 15.00,
    "weekly_saving": 105.00,
    "monthly_saving": 450.00,
    "total_saving": 1350.00,
    "calculation_method": "custom"
  }
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated

### Achievements

#### Get User Achievements
- Method: GET
- Path: `/api/achievements`
- Description: Get user's milestone achievements
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Response:
  ```json
  [
    {
      "id": "uuid",
      "milestone": {
        "id": "milestone-uuid",
        "name": "One Week Sober",
        "days_value": 7,
        "description": "Completed one week of sobriety",
        "icon_path": "/icons/milestone-7.png"
      },
      "achieved_at": "2025-10-01T12:30:00Z"
    }
  ]
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated

#### Get Available Milestones
- Method: GET
- Path: `/api/milestones`
- Description: Get all available milestones
- Response:
  ```json
  [
    {
      "id": "uuid",
      "name": "One Month Sober",
      "days_value": 30,
      "description": "Completed one month of sobriety",
      "icon_path": "/icons/milestone-30.png"
    }
  ]
  ```
- Success: 200 OK

### Educational Content

#### Get Content
- Method: GET
- Path: `/api/content`
- Description: Get educational content based on user profile
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Query Parameters:
  - content_type: Filter by content type (quote, fact, image)
  - category: Filter by category
  - language: Filter by language (default: pl)
  - limit: Limit results (default: 1)
  - already_seen: Include already seen content (default: false)
- Response:
  ```json
  [
    {
      "id": "uuid",
      "content_type": "fact",
      "content": "Sample educational content",
      "extended_content": "More detailed information",
      "category": "health",
      "min_sobriety_days": 0,
      "seasonal": false
    }
  ]
  ```
- Success: 200 OK
- Errors:
  - 400 Bad Request - Invalid query parameters
  - 401 Unauthorized - Not authenticated

#### Mark Content as Displayed
- Method: POST
- Path: `/api/content/{content_id}/display`
- Description: Mark content as displayed to the user
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Response: 204 No Content
- Success: 204 No Content
- Errors:
  - 400 Bad Request - Invalid content ID
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Content not found

### Help Resources

#### Get Help Resources
- Method: GET
- Path: `/api/help`
- Description: Get available help resources
- Query Parameters:
  - resource_type: Filter by resource type (phone, group, technique, online)
  - region: Filter by geographical region
  - country: Filter by country (default: Poland)
  - available_247: Filter by 24/7 availability (boolean)
- Response:
  ```json
  [
    {
      "id": "uuid",
      "resource_type": "phone",
      "name": "Alcohol Support Hotline",
      "description": "24/7 support line for alcohol addiction",
      "contact_info": {
        "phone": "+48 800 123 456",
        "website": "https://example.org",
        "hours": "24/7"
      },
      "region": "Nationwide",
      "country": "Poland",
      "available_247": true
    }
  ]
  ```
- Success: 200 OK
- Errors:
  - 400 Bad Request - Invalid query parameters

#### Record Help Resource Interaction
- Method: POST
- Path: `/api/help/{resource_id}/interaction`
- Description: Record user interaction with a help resource
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Request Body:
  ```json
  {
    "interaction_type": "call",
    "notes": "Optional notes about the interaction"
  }
  ```
- Response: 204 No Content
- Success: 204 No Content
- Errors:
  - 400 Bad Request - Invalid interaction data
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Resource not found

### Crisis Detection

#### Get Active Crisis Patterns
- Method: GET
- Path: `/api/crisis/patterns`
- Description: Get list of crisis patterns (admin only)
- Request Headers: Authorization: Bearer {token} (admin)
- Response:
  ```json
  [
    {
      "id": "uuid",
      "pattern_name": "Weekend Relapse Pattern",
      "pattern_description": "Multiple relapses occurring on weekends",
      "severity": 3
    }
  ]
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated
  - 403 Forbidden - Not authorized (not admin)

#### Get User Crisis Detections
- Method: GET
- Path: `/api/crisis/detections`
- Description: Get crisis detections for the user
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Query Parameters:
  - resolved: Filter by resolution status (boolean)
- Response:
  ```json
  [
    {
      "id": "uuid",
      "detected_at": "2025-10-05T15:30:00Z",
      "pattern": {
        "id": "pattern-uuid",
        "pattern_name": "Weekend Relapse Pattern",
        "severity": 3
      },
      "resolved": false,
      "actions_taken": null
    }
  ]
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated

#### Update Crisis Resolution
- Method: PATCH
- Path: `/api/crisis/detections/{detection_id}`
- Description: Update crisis detection resolution status
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Request Body:
  ```json
  {
    "resolved": true,
    "resolution_notes": "User successfully implemented weekend activity plan"
  }
  ```
- Response: Updated crisis detection object
- Success: 200 OK
- Errors:
  - 400 Bad Request - Invalid resolution data
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Detection not found

### Surveys

#### Get Available Surveys
- Method: GET
- Path: `/api/surveys`
- Description: Get surveys available to the user
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Response:
  ```json
  [
    {
      "id": "uuid",
      "title": "User Experience Survey",
      "description": "Help us improve the app",
      "display_frequency_days": 30,
      "active": true,
      "questions_count": 3
    }
  ]
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated

#### Get Survey Questions
- Method: GET
- Path: `/api/surveys/{survey_id}/questions`
- Description: Get questions for a specific survey
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Response:
  ```json
  [
    {
      "id": "uuid",
      "question_text": "How would you rate the app?",
      "question_type": "rating",
      "options": {
        "min": 1,
        "max": 5,
        "labels": {
          "1": "Poor",
          "5": "Excellent"
        }
      },
      "required": true,
      "display_order": 1
    }
  ]
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Survey not found

#### Submit Survey Response
- Method: POST
- Path: `/api/surveys/{survey_id}/responses`
- Description: Submit responses to a survey
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Request Body:
  ```json
  [
    {
      "question_id": "question-uuid",
      "response": "5",
      "response_data": {
        "rating": 5
      }
    }
  ]
  ```
- Response: 204 No Content
- Success: 204 No Content
- Errors:
  - 400 Bad Request - Invalid response data
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Survey or question not found

#### Update Survey Display Status
- Method: PATCH
- Path: `/api/surveys/{survey_id}/display`
- Description: Update survey display status
- Request Headers: Authorization: Bearer {token} or X-Device-ID: {device_id}
- Request Body:
  ```json
  {
    "completed": true,
    "dismissed": false
  }
  ```
- Response: 204 No Content
- Success: 204 No Content
- Errors:
  - 400 Bad Request - Invalid status data
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Survey not found

### Data Export

#### Request Data Export
- Method: POST
- Path: `/api/exports`
- Description: Request data export
- Request Headers: Authorization: Bearer {token}
- Request Body:
  ```json
  {
    "export_type": "full"
  }
  ```
- Response:
  ```json
  {
    "id": "uuid",
    "export_type": "full",
    "status": "pending",
    "requested_at": "2025-10-12T09:45:03Z"
  }
  ```
- Success: 201 Created
- Errors:
  - 400 Bad Request - Invalid export type
  - 401 Unauthorized - Not authenticated

#### Get Export Status
- Method: GET
- Path: `/api/exports/{export_id}`
- Description: Check status of data export
- Request Headers: Authorization: Bearer {token}
- Response:
  ```json
  {
    "id": "uuid",
    "export_type": "full",
    "status": "completed",
    "requested_at": "2025-10-12T09:45:03Z",
    "completed_at": "2025-10-12T09:48:15Z",
    "file_path": "/exports/user_data_20251012.zip"
  }
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Export not found

#### Download Export File
- Method: GET
- Path: `/api/exports/{export_id}/download`
- Description: Download export file
- Request Headers: Authorization: Bearer {token}
- Response: File download
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Export not found

### Synchronization

#### Initiate Sync
- Method: POST
- Path: `/api/sync`
- Description: Initiate data synchronization
- Request Headers: Authorization: Bearer {token} and X-Device-ID: {device_id}
- Request Body:
  ```json
  {
    "sync_type": "full"
  }
  ```
- Response:
  ```json
  {
    "id": "uuid",
    "sync_type": "full",
    "sync_started_at": "2025-10-12T09:45:03Z"
  }
  ```
- Success: 201 Created
- Errors:
  - 400 Bad Request - Invalid sync type
  - 401 Unauthorized - Not authenticated

#### Get Sync Status
- Method: GET
- Path: `/api/sync/{sync_id}`
- Description: Check status of sync operation
- Request Headers: Authorization: Bearer {token} and X-Device-ID: {device_id}
- Response:
  ```json
  {
    "id": "uuid",
    "sync_type": "full",
    "status": "success",
    "records_synced": 32,
    "conflicts_detected": 0,
    "sync_started_at": "2025-10-12T09:45:03Z",
    "sync_completed_at": "2025-10-12T09:45:15Z"
  }
  ```
- Success: 200 OK
- Errors:
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Sync operation not found

#### Resolve Sync Conflicts
- Method: POST
- Path: `/api/sync/{sync_id}/conflicts`
- Description: Resolve conflicts from sync operation
- Request Headers: Authorization: Bearer {token} and X-Device-ID: {device_id}
- Request Body:
  ```json
  [
    {
      "conflict_id": "conflict-uuid",
      "resolution": "client_wins"
    }
  ]
  ```
- Response: 204 No Content
- Success: 204 No Content
- Errors:
  - 400 Bad Request - Invalid resolution data
  - 401 Unauthorized - Not authenticated
  - 404 Not Found - Sync operation or conflict not found

## 3. Authentication and Authorization

The API uses a dual authentication system to support both logged-in users and anonymous devices:

### Authentication Methods

1. **JWT Authentication**
   - Supabase Authentication is used to issue JWT tokens
   - Access tokens are included in the Authorization header: `Authorization: Bearer {token}`
   - Access tokens expire after 60 minutes
   - Refresh tokens can be used to obtain new access tokens
   - Used for authenticated users with accounts

2. **Device ID Authentication**
   - Device identifiers are used for anonymous users
   - Device ID is included in the X-Device-ID header: `X-Device-ID: {device_id}`
   - Devices must be registered before using this authentication method
   - Provides limited access to device-specific data only

### Authorization

Leveraging Supabase Row Level Security (RLS) policies:

1. **User-based Policies**
   - Users can only access their own data
   - Authenticated with JWT tokens
   - Applied to all tables with user_id foreign key

2. **Device-based Policies**
   - Devices can only access their own data
   - Authenticated with device identifiers
   - Applied to tables with device_id foreign key

3. **Public Resources**
   - Some resources are publicly available without authentication
   - Examples: milestones, educational content, help resources
   - Read-only access is provided

4. **Admin Policies**
   - Admin users have elevated privileges
   - Can access and manage all resources
   - Identified through JWT claims

## 4. Validation and Business Logic

### User Validation
- Email must be in valid format
- Passwords must be at least 8 characters and include at least one number and one uppercase letter
- Timezone must be a valid IANA timezone identifier

### Answer Validation
- Only one answer allowed per user/device per day
- Answer type must be one of: "yes", "no", "none"
- Answer date must not be in the future
- Backdated answers limited to 3 days in the past

### Settings Validation
- Notification time must be a valid time in "HH:MM:SS" format
- Expense values must be positive numbers
- Expense frequency must be one of: "daily", "weekly", "monthly"
- Expense currency must be a valid ISO currency code
- Humor level must be one of: "none", "low", "standard", "high"

### Streak Calculations
- Current streak is calculated from the last drinking date to the current date
- Streak is reset when a user answers "yes" or manually resets
- The longest streak is calculated and stored for quick access
- No streak should have future dates

### Content Delivery Logic
- Content is filtered based on user's sobriety days
- Seasonal content is only available during defined date ranges
- Content should not repeat until all appropriate content has been shown
- Content with minimum sobriety days requirement should only be shown to eligible users

### Crisis Detection Logic
- Patterns are defined by SQL queries that analyze user behavior
- When a pattern matches, a crisis detection is created
- Severity levels determine notification urgency
- Resolved crises are tracked for future analysis

### Survey Logic
- Surveys are shown based on display frequency and minimum app usage
- Once completed, surveys are not shown again
- Dismissed surveys can be reshown after frequency period
- Responses must match question type constraints

### Synchronization Logic
- Full sync includes all user data
- Partial sync can target specific data types
- Conflicts are detected when both server and client have changes
- Resolution strategies include server_wins, client_wins, merged, and unresolved

### Performance Considerations
- List endpoints should support pagination
- Heavy calculations should leverage database views and functions
- Caching should be implemented for frequently accessed resources
- Long-running operations should be asynchronous with status tracking
