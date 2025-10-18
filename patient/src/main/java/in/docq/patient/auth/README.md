# ABDM Token Authentication & Authorization System

This package implements a comprehensive authentication and authorization system for patient APIs using ABDM (Ayushman Bharat Digital Mission) tokens.

## Architecture Overview

```
Patient Mobile App → ABDM Token → Patient Service → Validated Patient Access
```

## Components

### 1. `@AbdmAuthorized` Annotation
Secures API endpoints with ABDM token validation and patient ID matching.

```java
@GetMapping("/patients/{patient-id}/appointments")
@AbdmAuthorized(resource = "appointment", validatePatientId = true)
public CompletionStage<ResponseEntity<List<PatientAppointment>>> getMyAppointments(
    @PathVariable("patient-id") String patientId) {
    // Only accessible by authenticated patient with matching ID
}
```

### 2. `AbdmAuthenticationFilter`
- Validates ABDM tokens on every request
- Extracts patient information from JWT tokens
- Sets authentication context in request attributes
- Handles authentication errors with proper HTTP responses

### 3. `AbdmTokenValidator`
- Parses and validates ABDM JWT tokens
- Extracts patient ID, ABHA address, and ABHA number
- Checks token expiration
- Validates patient ID matching for authorization

### 4. `AbdmAuthorizedAspect`
- Intercepts `@AbdmAuthorized` annotated methods
- Performs patient ID validation when required
- Logs access for audit purposes
- Returns 403 Forbidden for unauthorized access

## Usage Examples

### Patient-Specific APIs (with ID validation)
```java
@RestController
@RequestMapping("/patients/{patient-id}")
public class PatientController {
    
    @GetMapping("/appointments")
    @AbdmAuthorized(resource = "appointment", validatePatientId = true)
    public CompletionStage<ResponseEntity<List<Appointment>>> getAppointments(
            @PathVariable("patient-id") String patientId) {
        // Patient can only access their own appointments
        return appointmentService.getPatientAppointments(patientId);
    }
}
```

### General APIs (no ID validation)
```java
@RestController
@RequestMapping("/opds")
public class OPDController {
    
    @GetMapping("/available")
    @AbdmAuthorized(resource = "opd", validatePatientId = false)
    public CompletionStage<ResponseEntity<List<OPD>>> getAvailableOPDs() {
        // Any authenticated patient can view available OPDs
        return opdService.getAvailableOPDs();
    }
}
```

### "Me" Endpoints (self-service)
```java
@GetMapping("/me/profile")
@AbdmAuthorized(resource = "profile", validatePatientId = false)
public CompletionStage<ResponseEntity<PatientProfile>> getMyProfile(HttpServletRequest request) {
    // Get patient ID from authenticated token
    String patientId = (String) request.getAttribute("authenticatedPatientId");
    return profileService.getProfile(patientId);
}
```

## Token Structure

The ABDM token is expected to be a JWT with the following payload structure:

```json
{
  "sub": "patient-id-123",
  "abhaAddress": ["patient@abha"],
  "abhaNumber": "91-1234-5678-9012",
  "exp": 1640995200,
  "iat": 1640991600
}
```

## HTTP Headers

### Request Header
```
Authorization: Bearer <ABDM_JWT_TOKEN>
```

### Error Responses

#### 401 Unauthorized (Missing/Invalid Token)
```json
{
  "error": "Authorization header is required"
}
```

#### 403 Forbidden (Patient ID Mismatch)
```json
{
  "error": "Access denied: You can only access your own data"
}
```

## Configuration

### Skip Authentication for Certain Paths
Modify `AbdmAuthenticationFilter.shouldSkipAuthentication()`:

```java
private boolean shouldSkipAuthentication(String path) {
    return path.startsWith("/actuator/") ||
           path.startsWith("/patients/login/") ||
           path.startsWith("/patients/signup/") ||
           path.equals("/") ||
           path.startsWith("/swagger") ||
           path.startsWith("/v3/api-docs");
}
```

## Security Features

1. **Token Validation**: Validates JWT structure and expiration
2. **Patient ID Matching**: Ensures patients can only access their own data
3. **Audit Logging**: Logs all access attempts for security monitoring
4. **Error Handling**: Proper HTTP status codes and error messages
5. **Flexible Authorization**: Can validate patient ID or allow general access

## Integration with Existing Services

The authentication system integrates seamlessly with existing services:

```java
@Service
public class PatientAppointmentService {
    
    public CompletionStage<List<Appointment>> getPatientAppointments(String patientId) {
        // Patient ID is already validated by @AbdmAuthorized
        // Service can trust the patient ID parameter
        return appointmentDao.findByPatientId(patientId);
    }
}
```

This system ensures that:
- ✅ Only authenticated patients can access APIs
- ✅ Patients can only access their own data
- ✅ All access is logged for audit purposes
- ✅ Proper error handling and security responses
- ✅ Flexible authorization rules per endpoint
