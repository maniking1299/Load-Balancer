package com.manish.LoadBalancer.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HealthResponse {

    private String status;
    private String version;
}
