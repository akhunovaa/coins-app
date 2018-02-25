package ru.leon4uk.coins.app.web.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    @Autowired
    Environment env;

    @Autowired
    private DriverManagerDataSource dataSource;

    @Bean
    public LocalSessionFactoryBean sessionFactory(){
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setPackagesToScan("ru.leon4uk.coins.app.domain");
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory().getObject());
        return txManager;
    }

    private Properties hibernateProperties() {

        return new Properties() {{
                //setProperty("hibernate.show_sql", "true");
                setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
                setProperty("hibernate.connection.charSet", "UTF-8");
                setProperty("hibernate.format_sql", "true");
                setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
                //setProperty("hibernate.hbm2ddl.auto", "update");
            }
        };
    }

}
