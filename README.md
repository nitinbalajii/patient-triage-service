# 🏥 Patient Triage Service

[![Java](https://img.shields.io/badge/Java-17%20%7C%2021-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Data JPA](https://img.shields.io/badge/Persistence-Spring%20Data%20JPA-blue?logo=hibernate)](https://spring.io/projects/spring-data-jpa)
[![Database](https://img.shields.io/badge/Database-H2%20In--Memory-lightblue?logo=databricks)](https://www.h2database.com/)
[![Tests](https://img.shields.io/badge/Tests-JUnit%205%20%7C%20Mockito-red?logo=junit5)](https://junit.org/junit5/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

An enterprise-grade, domain-driven **Clinical Triage & Emergency Acuity Assessment Microservice** built with **Java 17**, **Spring Boot 3**, and **Spring Data JPA**. Designed for real-time patient intake at hospital emergency departments, it evaluates physiological vital signs and symptoms dynamically using the **Strategy Design Pattern**.

---

## 🏛️ Architectural Overview & Design Patterns

The service is structured according to strict **Layered Architecture** and OOP principles, cleanly decoupling clinical domain rules from HTTP transport and persistence concerns:

```
                      ┌────────────────────────────┐
                      │    HTTP / REST Clients     │
                      └─────────────┬──────────────┘
                                    │  JSON Request
                                    ▼
                      ┌────────────────────────────┐
                      │      TriageController      │  (Bean Validation: @Valid)
                      └─────────────┬──────────────┘
                                    │  Validated DTO
                                    ▼
                      ┌────────────────────────────┐
                      │     TriageServiceImpl      │  (@Transactional Service Layer)
                      └─────────────┬──────────────┘
                                    │
             ┌──────────────────────┴──────────────────────┐
             ▼                                             ▼
  ┌───────────────────────┐                     ┌───────────────────────┐
  │ TriageStrategyFactory │                     │TriageRecordRepository │
  └──────────┬────────────┘                     └──────────┬────────────┘
             │ Dynamic Resolution                          │
     ┌───────┼───────┐                                     │
     ▼       ▼       ▼                                     │
   Adult  Pediatric Geriatric                              │ Spring Data JPA
  Strategy Strategy Strategy                               │
     │       │       │                                     ▼
     └───────┬───────┘                          ┌───────────────────────┐
             │ Acuity Score + Clinical Ward     │   H2 In-Memory RDBMS  │
             └─────────────────────────────────►│   (triage_records)    │
                                                └───────────────────────┘
```

### Key Patterns & Best Practices
- **Strategy Pattern (`TriageStrategy`)**: Enforces Open/Closed Principle (OCP). Adding new triage demographic rules (e.g., Neonatal or Trauma) requires zero modification to existing business code.
- **Factory Pattern (`TriageStrategyFactory`)**: Automatically binds all strategy implementations via Spring Dependency Injection into an `EnumMap` for $O(1)$ dispatch.
- **Fail-Fast Bean Validation**: Inputs are validated at the edge using Jakarta constraints (`@NotNull`, `@Min`, `@Max`, `@NotBlank`).
- **Global Error Handling**: Standardized RFC-compliant error envelopes via `@RestControllerAdvice`.
- **Zero-Setup Database**: Powered by in-memory H2 with automatic schema initialization and an interactive console.

---

## 🎯 Clinical Acuity Scoring Model (ESI Tiers)

| Acuity Level | Tier Name | Typical Indication | Target Ward |
|:---|:---|:---|:---|
| **Level 1** | `LEVEL_1_RESUSCITATION` | Cardiac arrest, apnea, severe trauma | Red Trauma Bay |
| **Level 2** | `LEVEL_2_EMERGENT` | Hypoxia ($SpO_2 < 90\%$), arrhythmia, severe distress | Critical Care Area |
| **Level 3** | `LEVEL_3_URGENT` | Moderate vitals deviation, multi-resource workup | Acute Care Wing |
| **Level 4** | `LEVEL_4_LESS_URGENT` | Single diagnostic procedure required, stable | Fast Track Clinic |
| **Level 5** | `LEVEL_5_NON_URGENT` | Routine checkup, prescription renewal | Outpatient Ward |

---

## 🚀 Getting Started

### Prerequisites
- **JDK 17** or higher
- **Maven 3.8+** (or use included wrapper)

### Build & Run
```bash
# Clone the repository
git clone https://github.com/nitinbalajii/patient-triage-service.git
cd patient-triage-service

# Build and execute unit tests
mvn clean test

# Run application locally (port 8081)
mvn spring-boot:run
```

The service will start on `http://localhost:8081`.  
Access the H2 Database Console at: `http://localhost:8081/h2-console` (JDBC URL: `jdbc:h2:mem:triagedb`, user: `sa`, password: empty).

---

## 📡 REST API Reference

### 1. Evaluate and Triage Patient
**`POST /api/v1/triage/assess`**

**Request Body:**
```json
{
  "patientIdentifier": "PAT-78219",
  "demographicGroup": "ADULT",
  "age": 52,
  "heartRate": 138,
  "systolicBp": 185,
  "oxygenSaturation": 89,
  "chiefComplaint": "Crushing chest pain radiating to left shoulder"
}
```

**Response (`201 Created`):**
```json
{
  "recordId": 1,
  "patientIdentifier": "PAT-78219",
  "demographicGroup": "ADULT",
  "acuityLevel": "LEVEL_2_EMERGENT",
  "acuityDescription": "High risk / confused / severe pain / vital sign danger",
  "calculatedScore": 95,
  "clinicalRationale": "Adult assessment: Severe hypoxemia (SpO2 < 90%). Severe hemodynamic rate instability (HR: 138). Elevated blood pressure deviation. High-acuity chief symptom reported.",
  "assignedWard": "Critical Care Area",
  "assessedAt": "2026-10-12T20:45:00.000"
}
```

### 2. Fetch Recent Assessments
**`GET /api/v1/triage/recent`**

### 3. Filter by Acuity Tier
**`GET /api/v1/triage/acuity/LEVEL_1_RESUSCITATION`**

### 4. Health Check
**`GET /api/v1/triage/health`**

---

## 🧪 Testing Suite

The repository includes comprehensive unit and integration tests using **JUnit 5** and **Mockito**:
- **Strategy Unit Tests (`TriageStrategyTest`)**: Validates adult, pediatric, and geriatric threshold calculations and edge cases.
- **Service Isolation Tests (`TriageServiceTest`)**: Verifies repository interactions, transaction boundaries, and exception handling using Mockito mocks.

Run tests via command line:
```bash
mvn test
```

---

## 👤 Author
- **Nitin Balajee**
- GitHub: [@nitinbalajii](https://github.com/nitinbalajii)
- Portfolio: [nitinbalajee.vercel.app](https://nitin-balajee-portfolio.vercel.app/)
- LinkedIn: [Nitin Balajee](https://www.linkedin.com/in/nitin-balajee-b283a3312/)

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
