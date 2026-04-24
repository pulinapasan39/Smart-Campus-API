# Smart Campus API

A RESTful API for managing rooms and sensors in a smart campus environment, built with JAX-RS (Jersey) and Apache Tomcat.

- Name: Imiyage Don Pulina Pasan
- IIT ID: 20240767
- UoW ID: W2120108


---

## Table of Contents
- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [How to Build and Run](#how-to-build-and-run)
- [Sample curl Commands](#sample-curl-commands)
- [Report - Answers to Questions](#report---answers-to-questions)

---

## Overview

The Smart Campus API is a RESTful web service designed to manage rooms and sensors across a university campus. It provides endpoints for:
- Managing rooms (create, read, delete)
- Managing sensors (create, read, filter by type)
- Recording and retrieving sensor readings
- Advanced error handling with meaningful HTTP status codes
- Request and response logging

The API follows RESTful principles including resource-based URLs, appropriate HTTP methods, and meaningful status codes.

---

## Technology Stack

| Technology | Version |
|---|---|
| Java | JDK 21 |
| JAX-RS Implementation | Jersey 3.1.3 |
| JSON Support | Jackson (via Jersey) |
| Server | Apache Tomcat 10.1.54 |
| Build Tool | Maven |
| IDE | NetBeans 24 |

---

## How to Build and Run

### Prerequisites
- Java JDK 21 installed
- Apache Tomcat 10.1.54 installed
- NetBeans 24 IDE installed
- Maven (bundled with NetBeans)

### Steps
#### 1. Clone the Repository
git clone https://github.com/pulinapasan39/Smart-Campus-API.git

#### 2. Open the Project in NetBeans
- Open NetBeans
- Go to **File >> Open Project**
- Navigate to the cloned folder and select it
- Click **Open Project**

#### 3. Register Tomcat in NetBeans
- Go to **Services tab >> Servers >> Add Server**
- Select **Apache Tomcat or TomEE**
- Point to your Tomcat installation folder
- Enter your Tomcat credentials
- Click **Finish**

#### 4. Build the Project
- Right-click on the project >> **Clean and Build**
- Wait for **BUILD SUCCESS**

#### 5. Run the Project
- Right-click on the project >> **Run**
- NetBeans will deploy the app to Tomcat automatically
- The API will be available at:
  http://localhost:8080/SmartCampusAPI/api/v1

  ---

## Sample curl Commands

### 1. Get API Discovery
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1
```

### 2. Create a Room
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms 
-H "Content-Type: application/json" 
-d "{\"id\": \"LIB-301\", \"name\": \"Library Quiet Study\", \"capacity\": 50}"
```

### 3. Create a Sensor
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors 
-H "Content-Type: application/json" 
-d "{\"id\": \"TEMP-001\", \"type\": \"Temperature\", \"status\": \"ACTIVE\", \"currentValue\": 22.5, \"roomId\": \"LIB-301\"}"
```

### 4. Get Sensors Filtered by Type
```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Temperature"
```

### 5. Add a Sensor Reading
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings 
-H "Content-Type: application/json" 
-d "{\"value\": 25.5}"
```

### 6. Get All Readings for a Sensor
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings
```

### 7. Delete a Room
```bash
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301
```

---

## Report - Answers to Questions

### Part 1.1 - JAX-RS Resource Class Lifecycle
By default JAX-RS will create an entirely new instance of a resource class every time a request comes into the web application via an HTTP request. This is known as the “per-request” life cycle.

This architectural decision makes a great impact when it comes to managing the data in memory. Since each request gets a fresh resource instance, any data stored as instance variables would be lost after the request completes.

In this implementation, the Singelton pattern is used in the `DataStore` class,providing a shared static instance for all resources. It ensures persistence of rooms, sensors and readings throughout the application’s lifecycle. Thread safety is crucial due to concurrent access, and a `ConcurrentHashMap` would be preferred over `HashMap` in production to avoid race conditions.


---

### Part 1.2 - HATEOAS
Hypermedia as the Engine of Application State(HATEOS) improves the functionality of RESTful APIs by making them self-explanatory, whereby the client can  be allowed access to related resources through links within the API’s response instead of having to depend on any external documentation to achieve the same. 

---

### Part 2.1 - Returning IDs vs Full Room Objects
Returning only room IDs is more bandwidth efficient but requires additional requests to get details, increasing round trips. On the other hand, when room objects are sent back, gathering room information becomes easier, but more space is taken since a large object is being sent to the client. This application prefers using room objects since there are not too many rooms.

---

### Part 2.2 - Idempotency of DELETE
The DELETE operation described is idempotent. First, the method deletes the resource and  gives a **204 No Content** reply. On subsequent attempts, the client receives a **404 Not Found** reply, because the resource is gone. Repeating a DELETE request results in the same final system, even if the HTTP response changes. Therefore, it is idempotent.

---

### Part 3.1 - @Consumes Annotation
In the JAX-RS framework, the `@Consumes(MediaType.APPLICATION_JSON)` notation implies that the API only allows requests in `application/json` format. If there is any other content type in the request body, for instance, `text/plain` or `application/xml`, the JAX-RS framework will not allow it, leading to a **415 Unsupported Media Type** error.

---

### Part 3.2 - @QueryParam vs Path Parameter for Filtering
Using `@QueryParam` for filtering is better than placing filter values directly within the URL path as it provides the facility of conditional filtering while keeping the API structure neat and avoiding the need for separate end points. Embedding types within the URL path is a violation of semantics since it implies a relationship that does not exist.

---

### Part 4.1 - Sub-Resource Locator Pattern
The Sub-Resource Locator pattern enhances architectural design by assigning nested path handling to specific classes, leading to better separation of concerns. This makes it easier to maintain and test the code while reducing complexity and improving maintainability. The Sub-Resource Locator pattern helps to break the API into smaller classes. For example, sensor-related paths can be handled by `SensorResource`, while reading-related paths can be handled by `SensorReadingResource`.

---

### Part 5.2 - HTTP 422 vs 404
The term **HTTP 422 Unprocessable Entity** better fits the situation where the client sends a JSON object but tries to access a non-existing resource, not the **HTTP 404 Not Found**, which suggests the requested URL is entirely missing. **HTTP 422** means that although the server understands the request, it could not process it because of the mistake in the semantics, and the problem is caused by the non-existing room ID.

---

### Part 5.4 - Security Risks of Exposing Stack Traces
Exposing internal Java stack traces to external API consumers poses several cybersecurity risks as these stack traces reveal the internal structure of the application including class names, method names, and file paths. This information may help attackers identify specific vulnerabilities in the code. 
Furthermore, error messages in stack traces may expose database queries, file paths, or configuration details that can be exploited. To mitigate these risks, implementing a Global Exception Mapper is recommended, as it captures unexpected exceptions and returns only a generic error to the client while preserving detailed logs internally.

---

### Part 5.5 - JAX-RS Filters for Cross-Cutting Concerns
Using JAX-RS filters for logging and other cross-cutting concerns enhances code maintainability by following the separation of concern approach. There will be no redundant code when implementing the logging feature within every method of resources, as it makes the process harder. Filters centralize logging logic, automatically applying it to all requests and responses, resulting in cleaner and more manageable code.
