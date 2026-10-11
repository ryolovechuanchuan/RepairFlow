package com.example.repairflow.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("api/health")
    public HealthResponse Health(){
        return new HealthResponse("RepairFlow","UP");
    }

    public record HealthResponse(
            String application,
            String status
    ){}
}
