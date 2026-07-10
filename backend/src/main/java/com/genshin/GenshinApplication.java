package com.genshin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class GenshinApplication {

    private final Environment env;

    public GenshinApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(GenshinApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        String port = env.getProperty("server.port", "8090");
        System.out.println("===================================");
        System.out.println("  原神查看器后端已启动");
        System.out.println("  端口: " + port);
        System.out.println("  H2控制台: http://localhost:" + port + "/h2-console");
        System.out.println("  健康检查: http://localhost:" + port + "/health");
        System.out.println("===================================");
    }
}
