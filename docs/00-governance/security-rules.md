# Technical Security Rules

> Mandatory technical controls that apply to all project code.
> These rules complement the security policy (`security-policy.md`) with
> concrete implementation practices.

---

OWASP Top 10 Security Controls - Salud Activa
This document defines the mandatory security controls for the Salud Activa (GestionTurnosApp) ecosystem, mapping directly to the OWASP Top 10 categories to ensure a production-grade security posture.
A01: Broken Access Control
To prevent unauthorized access to medical records or administrative functions:
//  BAD — trusting frontend IDs
const turnoId = req.body.appointmentId;

//  GOOD — extract identity from verified JWT and check ownership
const patientId = req.user.sub; 
const appointment = await AppointmentRepository.findById(req.body.appointmentId);

if (appointment.patientId !== patientId && req.user.role !== 'ADMIN') {
    throw new UnauthorizedError("You do not own this appointment");
}
Mandatory Rules:
•Middleware: Every protected endpoint (e.g., /api/turnos, /api/profile) MUST use the authMiddleware.
•RBAC: Verify permissions in the Use Case layer. Accessing /api/admin requires the ADMIN role.
•Resource Scope: Queries for list data (e.g., getMyTurnos) must implicitly filter by the authenticated userId.

A02: Cryptographic Failures
To protect sensitive health data (PII) in transit and at rest:
Rules:
•
Passwords: Use bcrypt with a cost factor of 12. Never use MD5 or plain SHA-1.
•
JWT: Sign with RS256 (Asymmetric). Access tokens expire in 1 hour.
•
Transit: HTTPS is mandatory for all Retrofit calls. Cleartext traffic is disabled in network_security_config.xml.
•
Logs: Redact password, token, and cvv from all backend and Logcat logs.
A03: Injection
To prevent malicious data from compromising the database or the app:
Backend (Node.js):

//  BAD — raw concatenation
const user = await db.query(`SELECT * FROM users WHERE email = '${req.body.email}'`);

//  GOOD — parameterized query (pg-promise or similar)
const user = await db.query('SELECT * FROM users WHERE email = $1', [req.body.email]);

Android (Room):
//  GOOD — Room uses bind parameters by default
@Query("SELECT * FROM turnos WHERE id = :appointmentId")
suspend fun getTurnoById(appointmentId: String): Turno?



A04: Insecure Design
•
UUIDs: Use UUID v4 for all public identifiers (userId, appointmentId). Never expose sequential primary keys from the DB.
•
Pagination: All listing endpoints (e.g., available slots, patient history) must implement mandatory pagination (max 50 records per page).
A05: Security Misconfiguration
Backend Checklist:
•
[ ] Helmet.js: Configured to set secure HTTP headers.
•
[ ] Stack Traces: Disabled in production (show a generic "Internal Error").
•
[ ] CORS: Whitelist only the production mobile app origin and the admin dashboard URL.
A06: Vulnerable Components
•
Audit: Run npm audit and gradle dependencyCheck before every major release.
•
Pinning: Use exact versions in package.json for security-critical libraries (e.g., jsonwebtoken, bcrypt).
•
Updates: Critical/High vulnerabilities found by Dependabot MUST be patched in the current sprint.
A07: Identification and Authentication Failures
•
Rate Limiting: Maximum 5 failed login attempts per IP per minute using express-rate-limit.
•
Lockout: Temporarily lock accounts after 10 consecutive failed attempts.
•
Token Rotation: Refresh tokens are valid for 7 days and are rotated upon every use to prevent replay attacks.
A08: Software and Data Integrity Failures
•
CI/CD: GitHub Actions must verify the integrity of the APK/Bundle before deployment.
•
FCM Webhooks: If receiving data from external services, verify the cryptographic signature of the provider.
A09: Security Logging and Monitoring Failures
•
Retention: Security logs (Logins, Failed access, PII changes) are retained for 90 days in AWS CloudWatch.
•
Alerts: Automated alerts are triggered if:
◦
401/403 errors spike (>20 in 1 minute).
◦
A SUPER_ADMIN login occurs from an unrecognized IP.
A10: Server-Side Request Forgery (SSRF)
•
Allowlist: If the backend needs to fetch external resources (e.g., medical lab PDFs), the target URL must match a predefined allowlist of trusted domains.
 Secure Input Handling (Validation)
We use Zod (Backend) and Kotlin Validation (Android) to ensure data is clean before it reaches the domain logic.

const CreateAppointmentSchema = z.object({
  doctorId: z.string().uuid(),
  appointmentDate: z.string().datetime(), // Validates ISO 8601
  symptoms: z.string().max(1000).trim(),
});

const validatedData = CreateAppointmentSchema.parse(req.body);

Correlations
•
Infrastructure: terraform_aws/
•
Auth Implementation:   backend/routes/auth.js
•
Network Module:   app/src/main/java/com/example/gestionturnosapp/di/NetworkModule.kt
