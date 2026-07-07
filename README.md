# Load-Balancer
---

                 Browser
                     │
          http://localhost/api/home
                     │
                     ▼
             Docker Port Mapping
             Host 80 → Container 80
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
                  
---
                  
