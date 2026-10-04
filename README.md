# Load Balancer

A simple **Load Balancer** built using **Nginx** and **Spring Boot**.

This project demonstrates how Nginx distributes incoming HTTP requests across multiple Spring Boot backend instances using the default **Round Robin** load-balancing algorithm.

---

## Architecture

```text
                         Client
                           │
                           │
                 http://localhost/api/home
                           │
                           ▼
                  ┌─────────────────┐
                  │      Nginx      │
                  │      :80        │
                  │  Load Balancer  │
                  └────────┬────────┘
                           │
                    Round Robin
                           │
              ┌────────────┼────────────┐
              ▼            ▼            ▼
        ┌──────────┐ ┌──────────┐ ┌──────────┐
        │ Spring 1 │ │ Spring 2 │ │ Spring 3 │
        │  :8081   │ │  :8082   │ │  :8083   │
        │ ID = 1   │ │ ID = 2   │ │ ID = 3   │
        └──────────┘ └──────────┘ └──────────┘
              │            │            │
              └────────────┼────────────┘
                           ▼
                        Response
                           │
                           ▼
                         Client
```

---

## Load Balancing Algorithm

- **Algorithm:** Round Robin
- **Load Balancer:** Nginx
- **Backend:** Spring Boot
- **Backend Instances:** 3
- **Nginx Port:** `80`
- **Spring Boot Ports:** `8081`, `8082`, `8083`

Nginx uses its default Round Robin behavior to distribute requests across the configured backend servers.

---

## Tech Stack

- Java 21
- Spring Boot
- Maven
- Nginx
- Docker
- Docker Compose

---

## Project Structure

```text
LoadBalancer/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── manish/
│                   └── LoadBalancer/
│                       ├── LoadBalancerApplication.java
│                       └── controller/
│                           └── LoadBalancerController.java
│
├── nginx.conf
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

# Getting Started

## Prerequisites

Make sure you have the following installed:

- [Java 21](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
- Docker Compose

Verify the installations:

```bash
java -version
mvn -version
docker --version
docker compose version
```

---

# Running the Project

## 1. Clone the Repository

```bash
git clone <your-repository-url>
cd LoadBalancer
```

---

## 2. Build the Application

Run:

```bash
mvn clean package
```

### Why `mvn clean package`?

`mvn clean package` performs the build process required to create the executable Spring Boot JAR.

### `mvn clean`

Removes the previous `target/` directory and old build artifacts.

This ensures that the application is rebuilt from a clean state rather than accidentally using files from a previous build.

### `mvn package`

Compiles the Java source code, runs the Maven build lifecycle, and packages the application into a `.jar` file.

After a successful build, the generated JAR will be located inside:

```text
target/
```

For example:

```text
target/
└── LoadBalancer-0.0.1-SNAPSHOT.jar
```

You should see:

```text
BUILD SUCCESS
```

---

## 3. Check the Generated JAR

Run:

### Windows

```powershell
dir target
```

### Linux / macOS

```bash
ls target
```

Find the generated `.jar` file.

For example:

```text
LoadBalancer-0.0.1-SNAPSHOT.jar
```

---

# Running Multiple Backend Instances

The project uses the same Spring Boot application as **three independent backend instances**.

Each instance runs on a different port and receives a different server ID.

```text
Instance 1 → Port 8081 → Server ID 1
Instance 2 → Port 8082 → Server ID 2
Instance 3 → Port 8083 → Server ID 3
```

This simulates multiple backend servers behind a load balancer.

---

## 4. Start Backend Instance 1

Open a terminal and run:

```bash
java -jar target/LoadBalancer-0.0.1-SNAPSHOT.jar --server.port=8081 --app.serverId=1
```

Keep this terminal running.

---

## 5. Start Backend Instance 2

Open a **second terminal** and run:

```bash
java -jar target/LoadBalancer-0.0.1-SNAPSHOT.jar --server.port=8082 --app.serverId=2
```

Keep this terminal running.

---

## 6. Start Backend Instance 3

Open a **third terminal** and run:

```bash
java -jar target/LoadBalancer-0.0.1-SNAPSHOT.jar --server.port=8083 --app.serverId=3
```

Keep this terminal running.

---

## Why Run the JAR Three Times?

The project contains a single Spring Boot application, but the JAR is started three times.

Each execution creates a separate Java process:

```text
                   Spring Boot JAR
                         │
             ┌───────────┼───────────┐
             ▼           ▼           ▼
          Process 1   Process 2   Process 3
            :8081       :8082       :8083
            ID = 1      ID = 2      ID = 3
```

This allows Nginx to treat the three processes as separate backend servers.

The `serverId` parameter makes it possible to identify which backend processed a request.

---

# Testing the Backend Servers

Before starting Nginx, verify that all three Spring Boot instances are working independently.

## Server 1

```bash
curl http://localhost:8081/api/home
```

Expected response:

```text
Hello Wrold 1
```

## Server 2

```bash
curl http://localhost:8082/api/home
```

Expected response:

```text
Hello Wrold 2
```

## Server 3

```bash
curl http://localhost:8083/api/home
```

Expected response:

```text
Hello Wrold 3
```

If all three endpoints respond successfully, the backend layer is ready.

---

# Starting Nginx

Once all three Spring Boot instances are running, start Nginx using Docker Compose.

Open another terminal in the project directory and run:

```bash
docker compose up -d
```

The `-d` flag runs the container in detached mode, allowing the terminal to be used for other commands.

Check the running containers:

```bash
docker ps
```

You should see the Nginx container running.

---

# How Docker Connects Nginx

Docker Compose maps:

```text
Host Port 80
      │
      ▼
Container Port 80
      │
      ▼
Nginx
```

Therefore, requests sent to:

```text
http://localhost
```

are received by Nginx inside the Docker container.

---

# Nginx Configuration

The Nginx configuration defines the three Spring Boot instances as an upstream group:

```nginx
upstream springboot {
    server host.docker.internal:8081;
    server host.docker.internal:8082;
    server host.docker.internal:8083;
}
```

Nginx receives incoming requests and forwards them to the `springboot` upstream:

```nginx
location / {
    proxy_pass http://springboot;
}
```

The overall flow is:

```text
Client
  │
  ▼
Nginx :80
  │
  ├──→ Spring Boot :8081
  │
  ├──→ Spring Boot :8082
  │
  └──→ Spring Boot :8083
```

---

# Testing the Load Balancer

Now send requests through Nginx instead of directly accessing the Spring Boot instances.

Run:

```bash
curl http://localhost/api/home
```

Run the request multiple times:

```bash
curl http://localhost/api/home
curl http://localhost/api/home
curl http://localhost/api/home
curl http://localhost/api/home
curl http://localhost/api/home
curl http://localhost/api/home
```

The responses should come from the different backend instances, for example:

```text
Hello Wrold 1
Hello Wrold 2
Hello Wrold 3
Hello Wrold 1
Hello Wrold 2
Hello Wrold 3
```

This demonstrates that the request is entering through Nginx and being distributed across the backend instances.

---

# Docker Compose

The Nginx container is managed using Docker Compose.

The configuration:

```yaml
services:
  nginx:
    image: nginx:latest
    container_name: nginx_loader

    ports:
      - "80:80"

    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro

    restart: always
```

The local `nginx.conf` is mounted into the Nginx container as a read-only configuration file.

---

# Stopping the Project

Stop each Spring Boot instance using:

```text
Ctrl + C
```

Then stop Nginx:

```bash
docker compose down
```

---


# Key Concepts Demonstrated

This project demonstrates:

- Reverse proxying
- Load balancing
- Round Robin load balancing
- Multiple Spring Boot instances
- Docker containerization
- Docker Compose
- Nginx upstream configuration
- HTTP request routing
- Running multiple instances of the same backend application
---

## Author

**Manish Kumar**

Built to understand the fundamentals of **load balancing, reverse proxies, Nginx, and distributed backend services**.
