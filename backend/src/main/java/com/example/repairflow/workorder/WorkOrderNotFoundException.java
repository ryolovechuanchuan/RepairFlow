package com.example.repairflow.workorder;

public class WorkOrderNotFoundException extends RuntimeException {

    public WorkOrderNotFoundException() {
        super("找不到指定的工單");
    }
}