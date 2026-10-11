package com.example.repairflow.workorder;

import java.time.Instant;

public record WorkOrderResponse(
        long id,
        String equipmentName,
        String equipmentLocation,
        String description,
        long reporterId,
        String status,
        Instant createdAt
) {}