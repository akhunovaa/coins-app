package ru.leon4uk.coins.app.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.Resource;


@Configuration
@ComponentScan({"ru.leon4uk.coins.app.domain", "ru.leon4uk.coins.app.dao", "ru.leon4uk.coins.app.service", "ru.leon4uk.coins.app.web"})
@PropertySource("classpath:application.properties")
public class AppConfig {

    private static final String PROPERTY_NAME_DATABASE_DRIVER = "app.db.worker.driver";
    private static final String PROPERTY_NAME_DATABASE_URL = "app.db.worker.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "app.db.worker.login";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "app.db.worker.password";

    @Resource
    private Environment env;

    @Bean
    public DriverManagerDataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty(PROPERTY_NAME_DATABASE_DRIVER).trim());
        dataSource.setUrl(env.getProperty(PROPERTY_NAME_DATABASE_URL).trim());
        dataSource.setUsername(env.getProperty(PROPERTY_NAME_DATABASE_USERNAME).trim());
        dataSource.setPassword(env.getProperty(PROPERTY_NAME_DATABASE_PASSWORD).trim());
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(
                dataSource());
    }


}