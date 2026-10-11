package com.example.repairflow.workorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateWorkOrderRequest(

        @NotBlank(message = "設備名稱不可空白")
        @Size(max = 100, message = "設備名稱最多 100 字元")
        String equipmentName,

        @NotBlank(message = "設備位置不可空白")
        @Size(max = 200, message = "設備位置最多 200 字元")
        String equipmentLocation,

        @NotBlank(message = "問題描述不可空白")
        @Size(max = 2000, message = "問題描述最多 2000 字元")
        String description

) {
    public CreateWorkOrderRequest {
        equipmentName = normalize(equipmentName);
        equipmentLocation = normalize(equipmentLocation);
        description = normalize(description);
    }

    private static String normalize(String value) {
        return value == null ? null : value.strip();
    }
}