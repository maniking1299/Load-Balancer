package com.manish.LoadBalancer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class LoadBalancerController {

    @Value("${app.serverId}")
    private String serverId;

        @GetMapping("/home")
        public ResponseEntity<String> test(){
            System.out.println("Server ID = " + serverId);
            return ResponseEntity.ok("Hello Wrold "+serverId);
        }
}
