package com.example.repairflow.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.repairflow.workorder.WorkOrderNotFoundException;
import org.springframework.http.HttpStatus;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        for (var error : exception.getBindingResult().getFieldErrors()) {
            String message = error.getDefaultMessage();

            fieldErrors.putIfAbsent(
                    error.getField(),
                    message != null ? message : "欄位內容不正確"
            );
        }

        ApiErrorResponse response = new ApiErrorResponse(
                "VALIDATION_ERROR",
                "輸入資料有誤",
                fieldErrors
        );

        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(WorkOrderNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleWorkOrderNotFound(
            WorkOrderNotFoundException exception
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                "WORK_ORDER_NOT_FOUND",
                exception.getMessage(),
                Map.of()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
}