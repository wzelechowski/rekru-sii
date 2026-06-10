# Build application
Run the following command in root directory

```
./mvnw clean package
```

# Run application
Execute the Spring Boot application by following command:
```
./mvnw spring-boot:run
```

# REST API
### Gyms
Create a Gym
```
URL: POST http://localhost:8080/api/gyms
```
Query:

```
curl -X POST http://localhost:8080/api/gyms \
  -H "Content-Type: application/json" \
  -d '{"name": "FitLife Center", "address": "Prosta 51, Warszawa", "phoneNumber": "122333444"}'
```
List All Gyms
```
URL: GET http://localhost:8080/api/gyms
```

Query:

```
curl -X GET http://localhost:8080/api/gyms
```

### Membership Plans
Create a Membership Plan for a Gym
```
URL: POST http://localhost:8080/api/gyms/{gymId}/membershipPlans
```
Query: (Replace {gymId} with a valid UUID)

```
curl -X POST http://localhost:8080/api/gyms/1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d/membershipPlans \
  -H "Content-Type: application/json" \
  -d '{"name": "FitLife Premium", "type": "PREMIUM", "monthlyPrice": 150.00, "currency": "PLN", "durationInMonths": 12, "maxMembers": 50}'
```
List All Membership Plans for a Gym

```
URL: GET http://localhost:8080/api/gyms/{gymId}/membershipPlans
```
Query:

```
curl -X GET http://localhost:8080/api/gyms/1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d/member
```

### Members
Register a New Member
```
URL: POST http://localhost:8080/api/membershipPlans/{membershipPlanId}/members
```
Query: (Replace {membershipPlanId} with a valid UUID)

```
curl -X POST http://localhost:8080/api/membershipPlans/9a8b7c6d-5e4f-3a2b-1c0d-9e8f7a6b5c4d/members \
  -H "Content-Type: application/json" \
  -d '{"fullName": "Jan Kowalski", "email": "jan.kowalski@test.com"}'
```
List All Members
```
URL: GET http://localhost:8080/api/members
```
Query:

```
curl -X GET http://localhost:8080/api/members
Cancel a Membership
```

URL: PATCH http://localhost:8080/api/members/{id}/cancel

Query: (Replace {id} with a valid member UUID)

```
curl -X PATCH http://localhost:8080/api/members/5f4e3d2c-1b0a-9f8e-7d6c-5b4a3f2e1d0c/cancel
```
### Revenue Report
Get Monthly Revenue Report
```
URL: GET http://localhost:8080/api/reports
```
Query:

```
curl -X GET http://localhost:8080/api/reports
```
