package com.Bank.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class HikariConfigs {

	@Value("${spring.datasource.url}")
	String url;
	
	@Value("${spring.datasource.username}")
	String userName;
	@Value("${spring.datasource.password}")
	String password;
	@Value("${spring.datasource.hikari.maximum-pool-size}")
	int poolSize;
	
	@Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        
        config.setJdbcUrl(url);
        config.setUsername(userName);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize); 

        return new HikariDataSource(config);
        
	
}
}