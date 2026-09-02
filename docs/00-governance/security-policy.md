# Security Policy

> Security is not a feature — it is a system property built from day one. This document
> defines the mandatory practices.
> Any deviation must be explicitly approved by the Tech Lead.

---

## Security principles


The following principles guide all architectural and implementation decisions for the Salud Activa ecosystem:

1. **Defense in Depth:** We implement multiple security layers. If one fails (e.g., a firewall is bypassed), other independent layers (e.g., encryption, MFA, or IAM policies) must contain the damage.

2. **Least Privilege:** Every component (microservice, database user, or developer account) is granted only the absolute minimum permissions necessary to perform its specific task.

3. **Fail Secure:** In the event of an error, crash, or unexpected state, the system must default to a secure state by denying access rather than inadvertently allowing it.

4. **Security by Design:** Security controls are treated as foundational architectural requirements. They are designed and integrated into the system from the start of a sprint, never "bolted on" as a post-development afterthought.

5. **Zero Trust:** We operate under the assumption that the network is always hostile. We verify every request and every user continuously, never granting implicit trust based solely on being within the internal network.

---

## Authentication

This document defines the security requirements for JSON Web Tokens (JWT) used for authentication between the Android application and the Node.js backend.
Technical Specifications
•
Signing Algorithm: RS256 (asymmetric) or HS256 with a secret of at least 256 bits.
•
Access Token Expiration: 1 hour (exp claim).
•
Refresh Token Expiration: 7 days.
•
Required Claims: sub (userId), iat (issued at), exp (expiration), and jti (unique token ID).
•
Android Storage: Tokens must be stored using EncryptedSharedPreferences or the Android Keystore.
•
Web Storage: Use httpOnly secure cookies.
Prohibited Payload Data
To maintain system integrity and privacy, the following information must never be included in the JWT payload:
•
Passwords (regardless of encryption or hashing).
•
Credit card or banking information.
•
Full Personally Identifiable Information (PII) such as full names or addresses. Use only the unique userId in the sub claim.
Implementation Rules
•
Encryption at Rest: On Android, tokens must be protected to prevent access on compromised or rooted devices.
•
Stateless Validation: The backend must verify tokens using the signature and expiration timestamp without requiring a database query for every request.
•
Secure Transmission: HTTPS (TLS 1.2 or higher) is mandatory for all authentication traffic.
•
Token Rotation: Refresh tokens should be rotated upon use to mitigate the risk of replay attacks.
References
•
Backend Auth Logic:   auth.js
•
Security Policy:   security-standards.md

### Refresh Token

This document outlines the security strategy for managing Refresh Tokens within the Salud Activa ecosystem to ensure long-term session security and breach detection.
Storage and Security
•
Secure Persistence: All refresh tokens must be stored in the database (MongoDB) protected with a bcrypt hash. Never store tokens in plain text.
•
Encryption at Rest: Ensure the database volume is encrypted to protect token data in the event of physical storage compromise.
Lifecycle and Rotation
•
Mandatory Rotation: We implement a "One-time use" policy. Every time a refresh token is used to generate a new access token, the existing refresh token must be invalidated and replaced by a new one.
•
Session Limits: Refresh tokens have a maximum lifespan of 7 days, after which the user must re-authenticate.
Invalidation Events
Refresh tokens are immediately revoked under the following conditions:
•
Explicit Logout: When the user signs out from the app or web interface.
•
Password Change: All active sessions must be terminated to ensure the security of the account.
•
Account Lock: If the administrative team suspends a user account.
Breach Detection (Automatic Revocation)
To prevent session hijacking and token theft, the system includes a proactive invalidation mechanism:
•
Reuse Detection: If the backend detects an attempt to use a refresh token that has already been revoked or replaced, ALL active tokens for that specific user will be immediately invalidated.
•
Forced Re-authentication: The user will be required to log in again using their primary credentials (email/password) to establish a new secure session.
Related Components
•
Auth Service:   backend/routes/auth.js
•
Security Principles:   docs/00-governance/security-principles.md

---

## Authorization

### RBAC (Role-Based Access Control)

# Role-Based Access Control (RBAC) - Salud Activa

This document defines the access control levels for the **Salud Activa** ecosystem. We use a granular permission model based on the principle of **Least Privilege**.

---

##  Role Definitions

| Role | Description | Core Permissions |
| :--- | :--- | :--- |
| `SUPER_ADMIN` | Technical system owner. | **ALL**: Full access to infrastructure, database, and logs. |
| `ADMIN` | Medical center administrator. | Manage staff, clinical schedules, and institutional settings. |
| `OPERATOR` | Staff member / Receptionist. | Manage appointments, check-in patients, and view schedules. |
| `DOCTOR` | Healthcare professional. | View assigned appointments and update medical records. |
| `PATIENT` | End-user (Client). | Create, view, and cancel personal appointments. |
| `VIEWER` | Read-only auditor. | View reports and history without modification rights. |

---

##  Permission Model

Permissions are defined following the standard: `[resource]:[action]`

### Examples:
- `appointments:create`
- `appointments:read`
- `appointments:update`
- `appointments:cancel`
- `medical_records:write`
- `users:block`
- `reports:export`

---

##  Validation Logic

1. **Token Claim:** The user's roles are embedded in the JWT under the `roles` claim (e.g., `roles: ["OPERATOR", "VIEWER"]`).
2. **Gateway:** Validates the signature and expiration of the JWT.
3. **Service Validation:** Each microservice/endpoint verifies if the roles present in the token are authorized to perform the requested `[resource]:[action]` before processing the data.

---

##  Correlations
- **Security Principles:** [security-principles.md](file:///docs/00-governance/security-principles.md)
- **Auth Service:** [auth.js](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/backend/routes/auth.js)

**Permission model:**

# Authorization: Permission Model & Validation - Salud Activa

This document defines the syntax and flow for authorization across the **Salud Activa** (GestionTurnosApp) ecosystem, ensuring that every request is validated against the user's specific roles and permissions.

---

## Permission Model

We use a granular syntax to define access rights, following the standard: `[resource]:[action]`.

### Examples for Salud Activa:
- **Appointments:**
    - `appointments:create` (Request a new turn)
    - `appointments:read` (View appointment details)
    - **Note:** In the mobile app, a `PATIENT` can only `read` their own appointments, whereas an `ADMIN` can read all.
- **Medical Records:**
    - `records:read` (View clinical history)
    - `records:update` (Doctor adding notes to a record)
- **User Management:**
    - `users:read` (View staff or patient lists)
    - `users:block` (Admin action for security)
- **Analytics:**
    - `reports:export` (Exporting clinic performance data)

---

##  Validation Flow

The system employs a multi-layered validation strategy to ensure that unauthorized requests are blocked as early as possible.

### 1. API Gateway Layer
The **API Gateway** acts as the first line of defense.
- It validates the **JWT signature** to ensure the token hasn't been tampered with.
- It checks the **expiration claim** (`exp`) to ensure the session is still active.
- If the token is invalid or expired, the Gateway returns a `401 Unauthorized` immediately.

### 2. JWT Payload (Identity)
Roles are embedded directly within the JWT for fast, stateless checking.
- **Claim:** `roles`
- **Example:** `roles: ["DOCTOR", "VIEWER"]`
- This allows services to know the user's authority without a separate database call.

### 3. Service Level Validation
Each microservice (e.g., the Appointment Service or Auth Service) is responsible for enforcing its specific business logic.
- The service retrieves the `roles` from the decoded JWT.
- It checks if the current role is authorized for the specific `[resource]:[action]` requested.
- **Contextual Validation:** The service also ensures ownership (e.g., *“Is this patient trying to read someone else’s medical record?”*). If validation fails, it returns a `403 Forbidden`.

---

##  Related Documentation
- **Security Standards:** [security-standards.md](file:///docs/00-governance/security-standards.md)
- **Definition of Done (Security):** [definition-of-done.md](file:///docs/00-governance/definition-of-done.md)
- **Auth Implementation:** [`backend/routes/auth.js`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/backend/routes/auth.js)

---

# Secure Communication & Secret Management - Salud Activa

This document defines the protocols for data transmission and the handling of sensitive credentials within the **Salud Activa** ecosystem, covering the Android app, Node.js backend, and AWS infrastructure.

---

## Secure Communication

### Data Transmission (Client-to-Server)
To protect patient privacy and medical records, all external communication is strictly encrypted.

- **HTTPS Mandatory:** All traffic between the Android App and the Backend must use HTTPS. Plain HTTP is strictly prohibited in all environments (including Staging/Production).
- **TLS Standards:** **TLS 1.2** is the minimum required version; **TLS 1.3** is recommended for better performance and security.
- **Certificates:**
    - **Production:** Managed via **AWS Certificate Manager (ACM)** for the Load Balancer (ALB).
    - **Staging:** Managed via **ACM** or Let's Encrypt.
- **HSTS (HTTP Strict Transport Security):** Enabled on the server-side to ensure the client only ever connects via a secure channel.

### Internal Service-to-Service Communication
For communication within the AWS Virtual Private Cloud (VPC):

- **mTLS:** Preferred if using a Service Mesh (e.g., AWS App Mesh).
- **Internal API Keys:** For services not using mTLS, a unique **Internal-API-Key** must be passed in the headers to authenticate service-to-service requests.

---

## Secret Management

Secrets such as API keys, database credentials, and private keys must never be exposed or stored insecurely.

### Prohibited (NEVER DO THIS)
- **✗ No Secrets in Source Code:** Never hardcode keys in `.kt`, `.java`, or `.js` files.
- **✗ No Secrets in Git:** Never commit `.env`, `local.properties`, or `google-services.json` to the repository.
- **✗ No Secrets in Logs:** Ensure that logging middleware redacts sensitive fields like `password` or `token`.
- **✗ No Secrets in Error Messages:** Backend errors must not leak connection strings or stack traces.

### Mandatory (ALWAYS DO THIS)
- **Environment Variables:** Inject secrets as environment variables into the **AWS ECS/Fargate** containers.
- **AWS Secrets Manager:** Use **AWS Secrets Manager** or **AWS Parameter Store (SecureString)** to store and rotate credentials (e.g., MongoDB URI, JWT Secret).
- **Android App:** Use `local.properties` for local development. For production builds, inject secrets via **GitHub Actions Secrets** during the CI/CD pipeline.
- **Encryption:** All secrets in AWS must be encrypted at rest using **AWS KMS**.

---

##  Correlations
- **Infrastructure Code:** [`terraform_aws/`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/terraform_aws/)
- **Security Principles:** [security-principles.md](file:///docs/00-governance/security-principles.md)
- **Android Build Config:** [app/build.gradle.kts](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/app/build.gradle.kts)
```

This document defines the lifecycle of credentials and the mandatory rules for handling user-provided data within the Salud Activa (GestionTurnosApp) ecosystem.
1. Secret Rotation Policy
To minimize the impact of a potential credential leak, the following rotation schedule is mandatory for all environments (AWS, Backend, and Mobile App Services):
•API Keys: Rotated every 90 days.
•TLS/SSL Certificates: Renewed 60 days before expiration (Automated via AWS ACM).
•Database Passwords (RDS/MongoDB): Rotated every 6 months, or immediately if a compromise is suspected.
•Service Accounts: Reviewed every quarter and rotated if the owner leaves the team.
•JWT Signing Secret: Rotated every 90 days (requires a brief overlap period to allow existing tokens to remain valid).
2. Input Validation & Sanitization
General Rules
1.Never Trust User Input: Validate all data at the entry point (Controller in the Backend, ViewModel in Android) before processing.
2.Whitelist, not Blacklist: Define exactly what is allowed (e.g., alphanumeric only for codes) rather than trying to filter out what is prohibited.
3.Reject Early: If the input is invalid (e.g., an invalid appointment date), respond with a 400 Bad Request immediately and stop execution.
 3. SQL Injection Prevention
Since the infrastructure uses AWS RDS (PostgreSQL) and the Android app uses Room, we must ensure no raw queries are vulnerable to injection.

// VULNERABLE - String concatenation
const query = `SELECT * FROM users WHERE email = '${userInput}'`;

// SAFE - Always use parameterized queries
const query = 'SELECT * FROM users WHERE email = $1';
const result = await db.query(query, [userInput]);
// ✓ SAFE — always use prepared parameters
const result = await db.query('SELECT * FROM users WHERE email = $1', [userInput]);
```

Prevention - Salud Activa
This document defines the mandatory security standards for preventing malicious script injection when rendering user-provided content within the Salud Activa (GestionTurnosApp) ecosystem. This applies to medical reports, patient comments, professional profiles, and chat messages.
 The Core Principle
Never trust and never render raw HTML from an untrusted source. All user-provided data must be escaped before being displayed in the UI, sent in emails, or generated as PDF reports.
🖥️ Node.js Backend & Web Views
When handling dynamic content that will be rendered in a browser or an internal WebView:
 VULNERABLE
Rendering HTML directly allows attackers to execute arbitrary scripts in the context of the user's session, leading to token theft or session hijacking.

// DANGEROUS: If 'userInput' contains <script>alert(1)</script>, it will execute.
element.innerHTML = userInput; 

 SAFE (Automatic Escaping)
Use properties that treat the input strictly as plain text. The browser will render the characters literally rather than interpreting them as code.

// SAFE: Renders HTML tags as literal text strings.
element.textContent = userInput; 

Android App (Kotlin/Compose)
While native Android components are generally more resilient to XSS than web environments, specific precautions are necessary.
SAFE (Native Components)
Standard Jetpack Compose Text components and XML TextView elements do not execute HTML or JavaScript by default.

// SAFE: Jetpack Compose treats strings as literal data.
Text(text = patientComment) 

WARNING (WebViews)
If the app uses a WebView to display external clinical results or formatted reports:
1.
Disable JavaScript: Ensure JS is disabled unless absolutely critical: settings.javaScriptEnabled = false.
2.
Sanitize Data: Always pass user-provided strings through a sanitizer before calling webView.loadDataWithBaseURL().
3.
Encrypted Storage: Ensure any tokens displayed or used by the WebView are handled securely via the Android Keystore.
🔗 Related Documentation
•
Security Principles:   docs/00-governance/security-principles.md
•
Data Persistence (Room): app/src/main/java/com/example/gestionturnosapp/data/local/
•
API Contracts: docs/07-api/contracts/

### Validation with Zod / Joi

```typescript
// Explicit validation schema in the controller
const CreateOrderSchema = z.object({
  clientId: z.string().uuid(),
  items: z.array(z.object({
    productId: z.string().uuid(),
    quantity: z.number().int().positive().max(1000),
    price: z.object({
      amount: z.number().positive(),
      currency: z.enum(['COP', 'USD']),
    }),
  })).min(1).max(50),
});
```

---

## OWASP Top 10 — Review checklist

| Vulnerability | Implemented control |
|---------------|-------------------|
| A01: Broken Access Control | RBAC + permission validation in each service |
| A02: Cryptographic Failures | TLS 1.2+, bcrypt for passwords, secrets in vault |
| A03: Injection | Prepared parameters in SQL, schema validation |
| A04: Insecure Design | Threat modeling in design, Security review |
| A05: Security Misconfiguration | IaC for configuration, review of defaults |
| A06: Vulnerable Components | Dependabot / Snyk for automatic updates |
| A07: Authentication Failures | JWT with rotation, brute-force protection |
| A08: Software Integrity Failures | Verify dependency checksums, SBOM |
| A09: Logging Failures | Logs without PII, centralized, with alerts |
| A10: SSRF | Whitelist of external URLs, do not follow redirects automatically |

---

## Audit and security logs

### Events that are ALWAYS recorded

```typescript
// Security events — store in a separate log, with retention > 1 year
const SECURITY_EVENTS = [
  'auth.login.success',
  'auth.login.failure',
  'auth.login.brute_force_detected',
  'auth.password.changed',
  'auth.token.revoked',
  'auth.unauthorized_access_attempt',
  'data.pii.accessed',
  'admin.role.changed',
  'admin.user.deleted',
];
```

**Required fields in security logs:**
- `userId` (or `ANONYMOUS` if not authenticated)
- `sourceIp`
- `action`
- `resource`
- `result` (SUCCESS / FAILURE)
- `timestamp`

---

## Vulnerability process

### What to do if you find a vulnerability

1. **Do not commit it to the public repo** or discuss it in open channels
2. Immediately notify the Tech Lead via a private channel
3. Create a private issue or a restricted repository issue
4. Severity is assigned (CVSS score or internal classification)
5. Remediated in the current sprint if critical, in the next sprint if high

### Remediation SLAs

| Severity | Remediation time |
|----------|----------------|
| Critical (CVSS 9-10) | 24 hours |
| High (CVSS 7-8.9) | 1 week |
| Medium (CVSS 4-6.9) | 1 month |
| Low (CVSS < 4) | Next security review |

---

## Correlations

- Security non-functional requirements → `04-requirements/non-functional.md`
- ADR on authentication → `05-architecture/decisions/`
- Security event observability → `13-operations/observability.md`
- RBAC implemented in → `09-microservices/services/XX-auth-service/`
