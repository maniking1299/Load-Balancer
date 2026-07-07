# Load Balancer

A simple **Load Balancer** built using **Nginx** and **Spring Boot** running in Docker containers.

The project demonstrates how Nginx distributes incoming HTTP requests across multiple backend servers using the default **Round Robin** load-balancing algorithm.

## Load Balancing Algorithm

- **Default:** Round Robin

---

```text
                 Browser
                     │
        http://localhost/api/home
                     │
                     ▼
             Docker Port Mapping
             Host :80 → Container :80
                     │
                     ▼
                   Nginx
                     │
         proxy_pass http://springboot
                     │
         Round Robin Load Balancer
          ┌──────────┼──────────┐
          ▼          ▼          ▼
      Spring1     Spring2     Spring3
       :8081       :8082       :8083
          │          │          │
          └──────────┼──────────┘
                     ▼
             Response to Nginx
                     │
                     ▼
                  Browser
```

---

## Tech Stack

- Java 21
- Spring Boot
- Nginx
- Docker
- Docker Compose

## Features

- Multiple Spring Boot instances
- Nginx reverse proxy
- Round Robin request distribution
- Dockerized deployment
- Single entry point through Nginx
