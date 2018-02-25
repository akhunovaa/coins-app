package ru.leon4uk.coins.app.web.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
@Import(AppConfig.class)
@ComponentScan("ru.leon4uk.coins.app.web")
public class PersistenceConfig {

    @Autowired
    AppConfig appConfig;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(appConfig.dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setPackagesToScan("ru.leon4uk.coins.app.domain");
        return sessionFactory;
    }

    private Properties hibernateProperties() {

        return new Properties() {
            {
                setProperty("hibernate.show_sql", String.valueOf(Boolean.TRUE));
                setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
                setProperty("hibernate.connection.charSet", "UTF-8");
            }
        };
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}


