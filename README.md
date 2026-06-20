# MediTrack

Clinical Workflow & Audit Logger for managing patient workflows and vitals.

## Tech Stack
- Java 17, Spring Boot 3.x
- PostgreSQL
- Spring Security + JWT
- JUnit 5 + Mockito + JaCoCo
- Spring Boot Actuator
- Swagger/OpenAPI
- Maven
- Docker + Docker Compose

## How to run with Docker
1. Ensure Docker and Docker Compose are installed.
2. Build the application: `mvn clean package -DskipTests`
3. Run the stack: `docker-compose up --build -d`

## How to run tests
Run `mvn test` to execute the test suite and generate JaCoCo coverage reports.

## Sample API requests

### 1. Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"doctor", "password":"password123"}'
```

### 2. Register Patient (DOCTOR)
```bash
curl -X POST http://localhost:8080/patients \
  -H "Authorization: Bearer <YOUR_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe", "age":30, "gender":"MALE", "contact":"1234567890"}'
```

### 3. Advance Workflow (DOCTOR)
```bash
curl -X PUT http://localhost:8080/patients/1/advance \
  -H "Authorization: Bearer <YOUR_TOKEN>"
```

### 4. Submit Vitals (NURSE)
```bash
curl -X POST http://localhost:8080/patients/1/vitals \
  -H "Authorization: Bearer <YOUR_NURSE_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"heartRate":80, "spo2":98, "bpSystolic":120, "temperature":36.5}'
```
