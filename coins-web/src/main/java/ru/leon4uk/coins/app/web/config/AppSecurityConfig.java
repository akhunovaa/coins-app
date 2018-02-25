package ru.leon4uk.coins.app.web.config;

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
        auth.inMemoryAuthentication().withUser("test2").password("test2").roles("USER");
        auth.inMemoryAuthentication().withUser("test").password("test").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("leon4uk").password("leon4uk").roles("SUPERADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/index*").access("hasRole('ROLE_USER')")
                .antMatchers("/index*").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/index*").access("hasRole('ROLE_SUPERADMIN')")
                .antMatchers("/add").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/add").access("hasRole('ROLE_SUPERADMIN')")
                .antMatchers("/delete/*").access("hasRole('ROLE_SUPERADMIN')")
                .and().formLogin().defaultSuccessUrl("/login", false);
    }
}