package ru.leon4uk.coins.web.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.leon4uk.app.bot.BotApplication;
import ru.leon4uk.app.bot.BotManager;

import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
@ComponentScan({"ru.leon4uk.coins.web", "ru.leon4uk.app"})
@PropertySource("classpath:application.properties")
@Import({BotManager.class})
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
//
//    @Bean
//    public ThreadPoolTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
//        pool.setCorePoolSize(5);
//        pool.setMaxPoolSize(10);
//        pool.setWaitForTasksToCompleteOnShutdown(true);
//        return pool;
//    }

    @Bean
    public ScheduledThreadPoolExecutor scheduledExecutorService() {
        ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(6);
        sch.setRemoveOnCancelPolicy(true);
        return sch;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}