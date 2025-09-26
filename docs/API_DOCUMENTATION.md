# API Documentation

## Base URL
- Local Development: `http://localhost:8080/api`
- Production: `https://your-domain.com/api`

## Authentication

All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## Authentication Endpoints

### POST /auth/signin
Login user and receive JWT token.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": "60f1b2a1b2a1b2a1b2a1b2a1",
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "roles": ["USER"]
}
```

### POST /auth/signup
Register a new user account.

**Request Body:**
```json
{
  "username": "jane_doe",
  "email": "jane@example.com",
  "password": "password123",
  "firstName": "Jane",
  "lastName": "Doe",
  "phoneNumber": "+1234567890"
}
```

**Response:**
```json
{
  "message": "User registered successfully!"
}
```

## Report Endpoints

### GET /reports
Get paginated list of public reports.

**Query Parameters:**
- `page` (int, default: 0) - Page number
- `size` (int, default: 10) - Page size
- `sortBy` (string, default: "createdAt") - Sort field
- `sortDir` (string, default: "desc") - Sort direction (asc/desc)
- `type` (enum) - Filter by report type
- `status` (enum) - Filter by report status

**Response:**
```json
{
  "content": [
    {
      "id": "60f1b2a1b2a1b2a1b2a1b2a1",
      "type": "MISSING_PERSON",
      "title": "Missing: John Smith",
      "description": "Last seen wearing blue jacket...",
      "reporter": {
        "id": "60f1b2a1b2a1b2a1b2a1b2a2",
        "username": "reporter1",
        "firstName": "Jane",
        "lastName": "Reporter"
      },
      "personDetails": {
        "fullName": "John Smith",
        "age": 25,
        "gender": "Male",
        "height": 175.0,
        "weight": 70.0,
        "hairColor": "Brown",
        "eyeColor": "Blue"
      },
      "lastSeenLocation": {
        "latitude": 40.7128,
        "longitude": -74.0060,
        "address": "Times Square, New York, NY",
        "city": "New York",
        "state": "NY",
        "country": "USA"
      },
      "status": "ACTIVE",
      "imageUrls": ["https://example.com/image1.jpg"],
      "contactPhone": "+1234567890",
      "contactEmail": "contact@example.com",
      "rewardAmount": 1000.0,
      "isPublic": true,
      "createdAt": "2023-12-01T10:00:00Z",
      "updatedAt": "2023-12-01T10:00:00Z",
      "incidentDateTime": "2023-11-30T18:00:00Z"
    }
  ],
  "totalElements": 50,
  "totalPages": 5
}
```

### POST /reports
Create a new report (requires authentication).

**Request Body:**
```json
{
  "type": "MISSING_PERSON",
  "title": "Missing: John Smith",
  "description": "Last seen wearing blue jacket around 6 PM yesterday.",
  "personDetails": {
    "fullName": "John Smith",
    "age": 25,
    "gender": "Male",
    "height": 175.0,
    "weight": 70.0,
    "hairColor": "Brown",
    "eyeColor": "Blue",
    "clothingDescription": "Blue jacket, black jeans",
    "distinguishingMarks": "Scar on left hand",
    "medicalConditions": ["Diabetes"],
    "emergencyContactName": "Jane Smith",
    "emergencyContactPhone": "+1234567891"
  },
  "lastSeenLocation": {
    "latitude": 40.7128,
    "longitude": -74.0060,
    "address": "Times Square, New York, NY",
    "city": "New York",
    "state": "NY",
    "country": "USA",
    "postalCode": "10036"
  },
  "imageUrls": ["https://example.com/image1.jpg"],
  "contactPhone": "+1234567890",
  "contactEmail": "contact@example.com",
  "rewardAmount": 1000.0,
  "isPublic": true,
  "incidentDateTime": "2023-11-30T18:00:00Z"
}
```

### GET /reports/{id}
Get a specific report by ID.

**Response:** Same as single report object from GET /reports

### GET /reports/my-reports
Get all reports created by the authenticated user.

**Response:** Array of report objects

### GET /reports/nearby
Get reports near a specific location.

**Query Parameters:**
- `latitude` (double, required) - Latitude coordinate
- `longitude` (double, required) - Longitude coordinate
- `radiusInMeters` (double, default: 10000) - Search radius in meters
- `type` (enum, optional) - Filter by report type

**Response:** Array of report objects

### PUT /reports/{id}/status
Update the status of a report (requires authentication and ownership or admin role).

**Query Parameters:**
- `status` (enum, required) - New status (ACTIVE, RESOLVED, CANCELLED, EXPIRED)

**Response:**
```json
{
  "message": "Report status updated successfully"
}
```

## Report Types
- `MISSING_PERSON` - Missing person report
- `LOST_ITEM` - Lost item report
- `FOUND_PERSON` - Found person report
- `FOUND_ITEM` - Found item report

## Report Status
- `ACTIVE` - Report is active and being searched
- `RESOLVED` - Person/item has been found
- `CANCELLED` - Report cancelled by reporter
- `EXPIRED` - Report has expired

## Error Responses

### 400 Bad Request
```json
{
  "message": "Invalid request data",
  "errors": ["Field 'title' is required"]
}
```

### 401 Unauthorized
```json
{
  "message": "Access denied. Please login."
}
```

### 403 Forbidden
```json
{
  "message": "Access denied. Insufficient permissions."
}
```

### 404 Not Found
```json
{
  "message": "Report not found"
}
```

### 500 Internal Server Error
```json
{
  "message": "Internal server error"
}
```

## Rate Limiting

API endpoints are rate limited to prevent abuse:
- Authentication endpoints: 5 requests per minute per IP
- Report creation: 10 requests per hour per user
- General endpoints: 100 requests per minute per user

## Pagination

List endpoints support pagination with the following parameters:
- `page`: Page number (0-based)
- `size`: Number of items per page (max 100)
- `sort`: Sort field and direction (e.g., "createdAt,desc")

Response includes pagination metadata:
```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0
}
```