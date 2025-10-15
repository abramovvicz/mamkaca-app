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

## 8. Implementation Steps

### 1. Create Service File

Create `src/lib/services/achievement.service.ts` with two main functions:

#### `getUserAchievements` Function
```typescript
/**
 * Retrieves a user's achievements with milestone details
 */
export async function getUserAchievements(
  supabase: SupabaseClient<Database>,
  userId: string | null,
  deviceId: string | null
): Promise<UserAchievementDto[]> {
  if (!userId && !deviceId) {
    throw new Error('Either user ID or device ID is required');
  }

  try {
    // Prepare query to get user achievements with milestones
    let query = supabase
      .from('user_achievements')
      .select(`
        id,
        achieved_at,
        milestones:milestone_id (
          id,
          name,
          days_value,
          description,
          icon_path
        )
      `);

    // Filter by user ID if provided
    if (userId) {
      query = query.eq('user_id', userId);
    } else if (deviceId) {
      // For device ID, we need to join with devices to find the user
      const { data: device } = await supabase
        .from('devices')
        .select('user_id')
        .eq('id', deviceId)
        .single();

      if (device && device.user_id) {
        query = query.eq('user_id', device.user_id);
      } else {
        // Device not linked to a user, return empty array
        return [];
      }
    }

    const { data, error } = await query;

    if (error) {
      console.error('Error fetching user achievements:', error);
      throw new Error('Failed to fetch user achievements');
    }

    // Transform data into DTO format
    return (data || []).map(item => ({
      id: item.id,
      achieved_at: item.achieved_at,
      milestone: {
        id: item.milestones.id,
        name: item.milestones.name,
        days_value: item.milestones.days_value,
        description: item.milestones.description,
        icon_path: item.milestones.icon_path
      }
    }));
  } catch (error) {
    console.error('Error in getUserAchievements:', error);
    throw error;
  }
}
```

#### `getMilestones` Function
```typescript
/**
 * Retrieves all available milestones
 */
export async function getMilestones(
  supabase: SupabaseClient<Database>
): Promise<MilestoneDto[]> {
  try {
    const { data, error } = await supabase
      .from('milestones')
      .select('*')
      .order('days_value', { ascending: true });

    if (error) {
      console.error('Error fetching milestones:', error);
      throw new Error('Failed to fetch milestones');
    }

    // Transform data into DTO format
    return (data || []).map(item => ({
      id: item.id,
      name: item.name,
      days_value: item.days_value,
      description: item.description,
      icon_path: item.icon_path
    }));
  } catch (error) {
    console.error('Error in getMilestones:', error);
    throw error;
  }
}
```

### 2. Create Schema File

Create `src/lib/schemas/achievement.schema.ts` (empty for now, but prepared for future expansion)

```typescript
import { z } from 'zod';

// Currently no input validation schemas needed as these are GET-only endpoints without parameters
// Can be expanded in the future if filtering options are added
```

### 3. Create Endpoint Files

#### Create `/src/pages/api/achievements/index.ts`

```typescript
import type { APIContext } from 'astro';
import { getUserAchievements } from '../../../lib/services/achievement.service';

// Disable prerendering since this is a dynamic API endpoint
export const prerender = false;

/**
 * GET /api/achievements - Retrieves user's milestone achievements
 * @param context Astro API Context
 * @returns List of achievements with milestone details
 */
export async function GET({ request, locals }: APIContext) {
  try {
    // Get authentication info
    const authHeader = request.headers.get('Authorization');
    const deviceIdHeader = request.headers.get('X-Device-ID');

    // Check if we have either authentication method
    if (!authHeader && !deviceIdHeader) {
      return new Response(
        JSON.stringify({
          error: 'Unauthorized',
          message: 'Either Authorization or X-Device-ID header is required',
        }),
        { status: 401, headers: { 'Content-Type': 'application/json' } }
      );
    }

    // Extract user ID from JWT if provided
    let userId: string | null = null;
    if (authHeader?.startsWith('Bearer ')) {
      const { data: userData, error: userError } = await locals.supabase.auth.getUser();
      
      if (userError || !userData.user) {
        return new Response(
          JSON.stringify({
            error: 'Unauthorized',
            message: 'Invalid authentication token',
          }),
          { status: 401, headers: { 'Content-Type': 'application/json' } }
        );
      }
      
      userId = userData.user.id;
    }

    // Extract device ID if provided
    const deviceId = deviceIdHeader || null;

    // Get achievements from the service
    const achievements = await getUserAchievements(
      locals.supabase,
      userId,
      deviceId
    );

    // Return successful response
    return new Response(JSON.stringify(achievements), {
      status: 200,
      headers: { 'Content-Type': 'application/json' },
    });
  } catch (error) {
    console.error('Error retrieving achievements:', error);
    return new Response(
      JSON.stringify({
        error: 'Internal Server Error',
        message: 'Failed to retrieve achievements',
      }),
      { status: 500, headers: { 'Content-Type': 'application/json' } }
    );
  }
}
```

#### Create `/src/pages/api/milestones/index.ts`

```typescript
import type { APIContext } from 'astro';
import { getMilestones } from '../../../lib/services/achievement.service';

// Disable prerendering since this is a dynamic API endpoint
export const prerender = false;

/**
 * GET /api/milestones - Retrieves all available milestones
 * @param context Astro API Context
 * @returns List of milestones
 */
export async function GET({ locals }: APIContext) {
  try {
    // Get milestones from the service
    const milestones = await getMilestones(locals.supabase);

    // Return successful response
    return new Response(JSON.stringify(milestones), {
      status: 200,
      headers: { 'Content-Type': 'application/json' },
    });
  } catch (error) {
    console.error('Error retrieving milestones:', error);
    return new Response(
      JSON.stringify({
        error: 'Internal Server Error',
        message: 'Failed to retrieve milestones',
      }),
      { status: 500, headers: { 'Content-Type': 'application/json' } }
    );
  }
}
```

## 9. Testing

### Manual Testing Steps

1. **Test GET /api/milestones**
   ```bash
   curl -X GET http://localhost:4321/api/milestones
   ```

2. **Test GET /api/achievements with JWT token**
   ```bash
   curl -X GET http://localhost:4321/api/achievements \
     -H "Authorization: Bearer <jwt_token>"
   ```

3. **Test GET /api/achievements with device ID**
   ```bash
   curl -X GET http://localhost:4321/api/achievements \
     -H "X-Device-ID: <device_id>"
   ```

4. **Test GET /api/achievements with no authentication**
   ```bash
   curl -X GET http://localhost:4321/api/achievements
   ```
   (Should return a 401 Unauthorized error)

### Expected Results

- **GET /api/milestones**: Returns a 200 OK response with an array of milestone objects
- **GET /api/achievements with valid JWT**: Returns a 200 OK response with an array of the user's achievements
- **GET /api/achievements with valid device ID**: Returns a 200 OK response with an array of achievements for the user linked to that device
- **GET /api/achievements with no authentication**: Returns a 401 Unauthorized error

## 10. Conclusion

This implementation plan provides a comprehensive roadmap for creating the achievements and milestones endpoints. Following the existing codebase patterns, it leverages Supabase for data access and Astro for API routing. The implementation includes proper authentication, error handling, and follows the project's established architectural patterns.

The endpoints are designed to be simple and focused, providing exactly the data required by the specification. Future enhancements could include filtering, pagination, or additional achievement-related functionality as needed.
