# API Endpoint Implementation Plan: Achievements and Milestones

## 1. Overview of Endpoints

**Achievements Endpoint**
- Purpose: Retrieve a user's milestone achievements with detailed milestone information
- Authentication: Required (JWT token or device ID)
- Response: Array of achievement objects with embedded milestone details

**Milestones Endpoint**
- Purpose: Retrieve all available milestone definitions
- Authentication: Not required
- Response: Array of milestone objects

## 2. Request Details

**GET /api/achievements**
- HTTP Method: GET
- URL: `/api/achievements`
- Headers:
  - Required (one of):
    - `Authorization: Bearer {token}` - For authenticated users
    - `X-Device-ID: {device_id}` - For device-only access
- Query Parameters: None required
- Success Response: 200 OK
- Error Responses:
  - 401 Unauthorized - If authentication headers are missing or invalid

**GET /api/milestones**
- HTTP Method: GET
- URL: `/api/milestones`
- Headers: None required
- Query Parameters: None required
- Success Response: 200 OK

## 3. Types and Schemas

The following DTOs from `src/types.ts` will be used:

```typescript
// For milestone data
export interface MilestoneDto {
  id: string;
  name: string;
  days_value: number;
  description: string | null;
  icon_path: string | null;
}

// For user achievement data with embedded milestone
export interface UserAchievementDto {
  id: string;
  milestone: MilestoneDto;
  achieved_at: string;
}
```

For validation, we'll create a new schema file: `src/lib/schemas/achievement.schema.ts`

```typescript
import { z } from 'zod';

// Currently no input validation schemas needed as these are GET-only endpoints without parameters
// Can be expanded in the future if filtering options are added
```

## 4. Data Flow

**GET /api/achievements**
1. Endpoint receives request with authentication headers
2. Authenticate the user based on JWT token or device ID
3. Call the service method to retrieve user achievements with milestone details
4. Return formatted achievements as JSON response

**GET /api/milestones**
1. Endpoint receives request (no authentication required)
2. Call the service method to retrieve all milestones
3. Return formatted milestones as JSON response

## 5. Security Considerations

- **Authentication**: The achievements endpoint must validate either a JWT token or device ID header
- **Authorization**: Only return achievements for the authenticated user or device
- **Input Validation**: Currently minimal as these are GET endpoints without parameters
- **Rate Limiting**: Apply standard API rate limits if implemented in the system
- **Error Handling**: Provide clear error messages without exposing sensitive information
- **HTTPS**: Ensure all API calls are made over HTTPS

## 6. Error Handling

**GET /api/achievements**
- 401 Unauthorized: Missing or invalid authentication headers
- 500 Internal Server Error: Database query errors or other internal issues

**GET /api/milestones**
- 500 Internal Server Error: Database query errors or other internal issues

## 7. Performance Considerations

- **Database Queries**:
  - Use proper JOINs for achievements to avoid N+1 query issues
  - Leverage the existing indices on `user_achievements` (`idx_user_achievements_user_id`, `idx_user_achievements_milestone_id`)
- **Result Size**:
  - Consider implementing pagination if the number of achievements/milestones grows significantly
  - For now, the data set is likely small enough to return all records without pagination
- **Caching**:
  - The milestones endpoint could benefit from caching as this data rarely changes

## 8. Testing

- remember to add test have covered all cases negative and possitive

### Expected Results

- **GET /api/milestones**: Returns a 200 OK response with an array of milestone objects
- **GET /api/achievements with valid JWT**: Returns a 200 OK response with an array of the user's achievements
- **GET /api/achievements with valid device ID**: Returns a 200 OK response with an array of achievements for the user linked to that device
- **GET /api/achievements with no authentication**: Returns a 401 Unauthorized error

## 9. Conclusion

This implementation plan provides a comprehensive roadmap for creating the achievements and milestones endpoints. Following the existing codebase patterns, it leverages Supabase for data access and Astro for API routing. The implementation includes proper authentication, error handling, and follows the project's established architectural patterns.

The endpoints are designed to be simple and focused, providing exactly the data required by the specification. Future enhancements could include filtering, pagination, or additional achievement-related functionality as needed.

