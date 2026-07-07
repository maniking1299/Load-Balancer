package com.manish.LoadBalancer.controller;


import com.manish.LoadBalancer.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Object health(){
        return new HealthResponse("Up","1.0");
    }
}
