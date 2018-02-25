package ru.leon4uk.coins.app.web.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.leon4uk.app.bot.BotApplication;

import javax.annotation.Resource;

@Configuration
@ComponentScan(basePackages = "ru.leon4uk.coins.app.web")
@PropertySource("classpath:application.properties")
public class AppConfig {

    private static final String PROPERTY_NAME_DATABASE_DRIVER = "app.db.worker.driver";
    private static final String PROPERTY_NAME_DATABASE_URL = "app.db.worker.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "app.db.worker.login";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "app.db.worker.password";

    @Resource
    private Environment env;

    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(
                dataSource());
    }

    @Bean
    public BotApplication botApplication() {
        return new BotApplication();
    }

}