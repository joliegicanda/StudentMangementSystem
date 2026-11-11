package com.example.StudentRegister_and_Management_system.DatabaseConfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

// A configuration class creates beans manually using @Bean methods.
// Here we create the DataSource bean: it knows how to connect to PostgreSQL.
// The values come from application.properties, which reads environment variables,
// so the password is never written in the code.
@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource(
            @Value("${app.db.url}") String url,
            @Value("${app.db.username}") String username,
            @Value("${app.db.password}") String password) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}