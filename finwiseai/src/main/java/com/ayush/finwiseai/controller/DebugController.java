package com.ayush.finwiseai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @GetMapping("/debug-env")
    public String checkEnv() {
        String dbUrl = System.getenv("DB_URL");
        String dbUsername = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");

        StringBuilder result = new StringBuilder();

        result.append("DB_URL: ");
        if (dbUrl == null) {
            result.append("NULL (not set)");
        } else {
            result.append("[").append(dbUrl).append("] Length: ").append(dbUrl.length());
        }

        result.append(" | DB_USERNAME: ");
        result.append(dbUsername == null ? "NULL" : "[" + dbUsername + "]");

        result.append(" | DB_PASSWORD: ");
        result.append(dbPassword == null ? "NULL" : "SET (length: " + dbPassword.length() + ")");

        return result.toString();
    }
}