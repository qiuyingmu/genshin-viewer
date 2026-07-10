package com.genshin.config;

import com.genshin.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

@RestController
public class HealthController {

    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        return Result.success(Map.of(
                "status", "UP",
                "service", "genshin-server",
                "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/ready")
    public Result<Map<String, Object>> ready() {
        String dbStatus = "DOWN";
        try (Connection conn = dataSource.getConnection()) {
            dbStatus = conn.isValid(2) ? "UP" : "DOWN";
        } catch (Exception e) {
            dbStatus = "DOWN: " + e.getMessage();
        }
        boolean ok = "UP".equals(dbStatus);
        return Result.success(Map.of(
                "status", ok ? "UP" : "DEGRADED",
                "database", dbStatus
        ));
    }
}
