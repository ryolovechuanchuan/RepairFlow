package com.example.repairflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionCheck implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionCheck.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseConnectionCheck(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        String databaseName = jdbcTemplate.queryForObject(
                "SELECT DB_NAME()",
                String.class
        );

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT_BIG(*) FROM dbo.WorkOrders",
                Long.class
        );

        log.info("Connected database: {}, work orders: {}",
                databaseName, count);
    }
}