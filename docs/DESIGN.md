# Microservice Auth API
A lightweight, stateless user authentication microservice built with Spring Boot 3.1 and Spring Security 6.1+. Designed to be modular and easily pluggable into distributed systems.
## 🔐 Why JWT?
Traditional session-based authentication doesn’t scale well across distributed architectures. Maintaining server-side sessions becomes complex when dealing with multiple instances or regions.

**JWT (JSON Web Token)** offers a scalable alternative:
1. Stateless — no session storage required
2. Lightweight and easy to verify
3. Ideal for microservices and APIs

With JWT, the server issues a signed token on login. Clients include this token in each request’s header to access protected resources.

## ⚙️ Tech Stack
- Spring Boot 3.1
- Spring Security 6.1+
- Java 17
- JJWT 0.12.6
- H2 Database (in-memory)

## 🧩 Key Components
1. User Registration
- Encrypted passwords with `BCryptPasswordEncoder`
- In-memory H2 storage
- `POST /api/auth/register`

2. Login Endpoint
- Validates credentials and issues JWT
- `POST /api/auth/login`

3. JWT Generation
- HS512 signing algorithm
- Uses secure 512-bit base64-encoded secret key
- Includes claims like username, issue time, and expiration

4. Protected Endpoint
- `GET /api/auth/profile`
- Requires a valid token in the header:<br/>`Authorization: Bearer <your-token>`
- Verified via custom JwtFilter

## 🔧 JWT & Security Configuration
**JJWT 0.12.6 Enhancements**
- Enforces strong key sizes
- Cleaner, builder-style API
- No deprecated classes (e.g., DatatypeConverter)

**Spring Security 6.1 Highlights**
- Functional, lambda-style configuration
- Stateless setup with no session management

## ⚠️ Challenges Faced
**Stricter Token Security (JJWT 0.12.6)**
- Requires keys generated using Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
- Simple strings no longer valid

**New Parsing API**
- Shift from Jwts.parser() to Jwts.parserBuilder().setSigningKey(...)
- Errors like MalformedJwtException appear if token is not securely created
