package ru.leon4uk.coins.web.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("test").password("test").roles("USER");
        auth.inMemoryAuthentication().withUser("leon4uk").password("leon4uk").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/index*").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/bot").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/bot/*").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/order/*").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/prices").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/delete/*").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/add").access("hasRole('ROLE_ADMIN')")
                .and().formLogin().defaultSuccessUrl("/login", false);
    }
}