package com.example.repairflow.workorder;

import java.util.Optional;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WorkOrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public WorkOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<WorkOrderResponse> findLatest() {
        String sql = """
                SELECT TOP (50)
                    Id,
                    EquipmentName,
                    EquipmentLocation,
                    Description,
                    ReporterId,
                    Status,
                    CreatedAt
                FROM dbo.WorkOrders
                ORDER BY Id DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new WorkOrderResponse(
                        rs.getLong("Id"),
                        rs.getString("EquipmentName"),
                        rs.getString("EquipmentLocation"),
                        rs.getString("Description"),
                        rs.getLong("ReporterId"),
                        rs.getString("Status"),
                        rs.getTimestamp("CreatedAt")
                                .toLocalDateTime()
                                .toInstant(ZoneOffset.UTC)
                )
        );
    }
    public Optional<WorkOrderResponse> findById(long id) {
        String sql = """
        SELECT
            Id,
            EquipmentName,
            EquipmentLocation,
            Description,
            ReporterId,
            Status,
            CreatedAt
        FROM dbo.WorkOrders
        WHERE Id = ?
        """;

        List<WorkOrderResponse> results = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new WorkOrderResponse(
                        rs.getLong("Id"),
                        rs.getString("EquipmentName"),
                        rs.getString("EquipmentLocation"),
                        rs.getString("Description"),
                        rs.getLong("ReporterId"),
                        rs.getString("Status"),
                        rs.getTimestamp("CreatedAt")
                                .toLocalDateTime()
                                .toInstant(ZoneOffset.UTC)
                ),
                id
        );

        return results.stream().findFirst();
    }

    public WorkOrderResponse create(
            CreateWorkOrderRequest request,
            long reporterId
    ) {
        String sql = """
        INSERT INTO dbo.WorkOrders (
            EquipmentName,
            EquipmentLocation,
            Description,
            ReporterId,
            Status
        )
        OUTPUT
            INSERTED.Id,
            INSERTED.EquipmentName,
            INSERTED.EquipmentLocation,
            INSERTED.Description,
            INSERTED.ReporterId,
            INSERTED.Status,
            INSERTED.CreatedAt
        VALUES (?, ?, ?, ?, 'PENDING')
        """;

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new WorkOrderResponse(
                        rs.getLong("Id"),
                        rs.getString("EquipmentName"),
                        rs.getString("EquipmentLocation"),
                        rs.getString("Description"),
                        rs.getLong("ReporterId"),
                        rs.getString("Status"),
                        rs.getTimestamp("CreatedAt")
                                .toLocalDateTime()
                                .toInstant(ZoneOffset.UTC)
                ),
                request.equipmentName(),
                request.equipmentLocation(),
                request.description(),
                reporterId
        );
    }
}