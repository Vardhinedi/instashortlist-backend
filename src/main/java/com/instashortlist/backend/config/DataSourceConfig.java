package com.instashortlist.backend.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @QuartzDataSource
    public DataSource quartzDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://mysql:3306/mydb");
        ds.setUsername("root");
        ds.setPassword("pass");
        ds.setMaximumPoolSize(5);
        return ds;
    }
}
