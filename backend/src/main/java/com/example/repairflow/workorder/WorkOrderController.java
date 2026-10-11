package com.example.repairflow.workorder;

import java.util.List;
import java.net.URI;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderRepository repository;

    public WorkOrderController(WorkOrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<WorkOrderResponse> list() {
        return repository.findLatest();
    }

    @GetMapping("/{id}")
    public WorkOrderResponse getById(@PathVariable("id") long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Work order not found"
                ));
    }

    @PostMapping
    public ResponseEntity<WorkOrderResponse> create(
            @Valid @RequestBody CreateWorkOrderRequest request
    ) {
        // 本機練習暫用使用者 1，之後改成從登入身分取得。
        long reporterId = 1L;

        WorkOrderResponse created = repository.create(
                request,
                reporterId
        );

        URI location = URI.create(
                "/api/work-orders/" + created.id()
        );

        return ResponseEntity
                .created(location)
                .body(created);
    }
}