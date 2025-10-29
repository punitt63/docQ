# Data Setup Module

This module provides the data setup orchestration for the docQ health facility system.

## Overview

The `data-setup` module orchestrates the onboarding process for:
- Facility managers
- Doctors (under facility managers)

It makes REST API calls to the `health-facility` service to execute the onboarding flow.

## Architecture

```
┌─────────────────────────────────────────┐
│  HTTP Request: POST /admin/data-setup    │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│  DataSetupController                   │
│  (in health-facility module)           │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│  DataSetupOrchestrator                  │
│  (in data-setup module)                │
│                                          │
│  1. Gets admin token from Keycloak      │
│  2. Loads config from JSON              │
│  3. Onboards facility managers          │
│  4. Onboards doctors                    │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│  HealthFacilityRestClient               │
│  Makes HTTP calls to:                   │
│  • POST /.../facility-manager/onboard   │
│  • POST /.../facility-manager/login     │
│  • POST /.../doctor/onboard             │
└─────────────────────────────────────────┘
```

## Execution Flow

### 1. Automatic Execution (via Docker Compose)

When you run:
```bash
docker-compose up
```

The execution order will be:
1. `postgres` starts
2. `keycloak` starts (depends on postgres)
3. `keycloak-setup` runs (depends on keycloak)
4. `flyway` runs (depends on postgres)
5. `health-facility` starts (depends on flyway & keycloak-setup)
6. `patient` starts (depends on health-facility)
7. **`data-setup` runs** (depends on patient service being healthy)
   - Waits for `patient` service to be healthy
   - Waits additional 10 seconds for full startup
   - Checks health endpoint availability (30 attempts, 2s intervals)
   - Executes data setup by calling the endpoint
   - Reports success or failure

The `data-setup` service will only run after the `patient` service is up and healthy.

### 2. Manual Execution

You can manually trigger the data setup:

```bash
# Using curl
curl -X POST http://localhost:9097/admin/data-setup

# Using docker
docker exec health-facility curl -X POST http://localhost:9097/admin/data-setup
```

### 3. Configuration

The data setup reads from:
- `data-setup/src/main/resources/data-setup-config.json` - JSON config file with:
  - Health facility details
  - Facility managers list
  - Doctors list
  - Patients list

Environment variables (from health-facility configuration):
- `KEYCLOAK_BASE_URL` - Keycloak server URL (default: http://localhost:8080)
- `KEYCLOAK_REALM` - Realm name (default: health-facility)
- `KEYCLOAK_ADMIN_USERNAME` - Admin username (default: docq-admin)
- `KEYCLOAK_ADMIN_PASSWORD` - Admin password
- `KEYCLOAK_DESKTOP_CLIENT_ID` - Desktop client ID
- `KEYCLOAK_DESKTOP_CLIENT_SECRET` - Desktop client secret

## API Endpoints Called

1. **Get Admin Token**
   - Uses Keycloak AuthenticationApi to get admin user token
   
2. **Onboard Facility Manager**
   - `POST /health-facilities/{facilityId}/health-facility-professionals/facility-manager/onboard`
   - Headers: `Authorization: Bearer {adminToken}`
   - Body: `{ "facilityManagerID": "...", "password": "..." }`

3. **Login as Facility Manager**
   - `POST /health-facilities/{facilityId}/health-facility-professionals/{facilityManagerId}/login`
   - Body: `{ "password": "..." }`
   
4. **Onboard Doctor**
   - `POST /health-facilities/{facilityId}/health-facility-professionals/doctor/onboard`
   - Headers: `Authorization: Bearer {facilityManagerToken}`
   - Body: `{ "doctorID": "...", "password": "...", "facilityManagerID": "..." }`

## Files

- `src/main/java/in/docq/data/setup/client/HealthFacilityRestClient.java` - REST client
- `src/main/java/in/docq/data/setup/service/DataSetupOrchestrator.java` - Orchestration logic
- `src/main/resources/data-setup-config.json` - Configuration data

## Dependencies

- `spring-boot-commons` - Spring Boot common components
- `keycloak-rest-client` - Keycloak REST API client
- `spring-boot-starter-webflux` - WebFlux for reactive HTTP calls
- `jackson-databind` - JSON serialization/deserialization

