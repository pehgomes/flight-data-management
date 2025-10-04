# Flight Data Management System

This project implements a **Flight Data Management System** using a **Hexagonal Architecture** (Ports and Adapters) to ensure clean separation of concerns, high testability, and maintainability.

---

## Key Technologies

* **Java 17**: Core language.
* **Spring Boot 3.5.6**: Application framework.
* **Spring WebFlux**: Used for **non-blocking, reactive HTTP calls** to external APIs. This includes built-in mechanisms for controlling **timeouts** to ensure the application remains responsive.
* **Swagger/OpenAPI**: Provides interactive documentation for the application's REST APIs.

---

## Hexagonal Architecture Structure

The project is structured into three main layers, following the Ports and Adapters pattern, visible under `src/main/java/com/flightdatamanagement`:

### 1. `domain` (The Core / Business Logic)

This is the **Application Core**. It holds all the business rules and entities.

* **Ports**: Defines the interfaces (contracts) that the `infra` layer must implement or the `application` layer will call.
* **Entities/Services**: The actual business logic.
* **Crucially, this layer has no dependencies on Spring, databases, or external APIs.**

### 2. `application` (The API / Use Cases)

This layer orchestrates the flow and exposes the domain functionality.

* **Controllers**: Handles incoming HTTP requests (input adapter).
* **Use Cases**: Calls the necessary services defined in the `domain`.

### 3. `infra` (Adapters / Infrastructure Details)

This layer implements the ports to interact with the external world.

* **API Adapters**: Code for consuming external services (e.g., calling the `crazySupplier` API using WebFlux).
* **Persistence Adapters**: Code for accessing data sources (e.g., in-memory databases for testing purposes).

---

## External API Simulation (CrazySupplierAPI) (WireMock with Docker Compose)

The project uses **WireMock** to simulate the external `crazySupplier` API. This ensures isolated and predictable testing without relying on the actual external service.

### WireMock Configuration Details

The WireMock server, run via **Docker Compose**, is configured to simulate the search endpoint of the external flight supplier:

1.  **Endpoint:** A **POST** request is expected at `/flights`.
2.  **Request Body:** The request must contain parameters typically used for a flight search, such as `from`, `to`, `outboundDate`, and `inboundDate`.
3.  **Mode Details:** Access `/wiremock/mappings/flights.json` **located in projects source**

### Simulating Realistic Latency

To simulate real-world network latency and test the resilience of the **Spring WebFlux** client, the API responses include a variable delay configured as a **lognormal distribution**:

```json
"delayDistribution": {
  "type": "lognormal",
  "median": 1000,
  "sigma": 0.3
}
```



The project uses **WireMock** to simulate the external `crazySupplier` API. This ensures isolated and predictable testing without relying on the actual external service.

### Setup Instructions

The WireMock server is packaged and managed via **Docker Compose**.

1.  **Start the Mock Server:**
    ```bash
    docker compose up -d
    ```
    This command reads the **`docker-compose.yml`** file and launches the mock service.
2.  **Stop the Mock Server:**
    ```bash
    docker compose down
    ```

---

## Building and Running

1.  **Build the project** using Maven:
    ```bash
    ./mvnw clean install
    ```
2.  **Start the WireMock server** (required for external flight API calls):
    ```bash
    docker compose up -d
    ```
3.  **Run the application** (assuming a Spring Boot executable JAR is created):
    ```bash
    java -jar target/flight-data-management-app.jar
    ```
    *Alternatively, run the main class `FlightManagementApplication` from your IDE.*

---
## ⚙️ Continuous Integration (CI) Pipeline

The project uses **GitHub Actions** for Continuous Integration. The pipeline is responsible for maintaining code quality and ensuring the system is functional upon every code change.

The CI workflow is defined in: **`.github/workflows/ci.yml`**

### Pipeline Responsibilities:

1.  **Code Checkout:** Fetches the latest code from the repository.
2.  **Environment Setup:** Configures **Java 17** and enables **Maven Caching** to speed up subsequent builds.
3.  **Build & Test:** Executes `mvn clean verify`, which includes compiling the code and running all unit and integration tests.
4.  **Coverage Report:** Generates a code coverage report using **JaCoCo** (`mvn jacoco:report`).
5.  **Artifact Upload:** Uploads the JaCoCo coverage report as an artifact (`jacoco-report`) for easy review.

### Status and Monitoring

The status of the pipeline can be monitored directly on the **Actions** tab of the GitHub repository.