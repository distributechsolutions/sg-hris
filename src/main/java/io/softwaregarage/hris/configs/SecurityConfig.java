package io.softwaregarage.hris.configs;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import io.softwaregarage.hris.commons.views.LoginView;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {
    @Bean
    public DataSource configureDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/sg-hris");
        dataSource.setUsername("postgres");
        dataSource.setPassword("p@$$w0rd");

        return dataSource;
    }

    @Bean
    public UserDetailsService jdbcUserDetailsService() {
        String usersByUsernameQuery = "SELECT username, password, is_account_active FROM sg_hris_user_account WHERE username = ? AND is_account_active = true";
        String rolesByUsernameQuery = "SELECT username, role FROM sg_hris_user_account WHERE username = ? AND is_account_active = true";

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(this.configureDataSource());
        jdbcUserDetailsManager.setUsersByUsernameQuery(usersByUsernameQuery);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(rolesByUsernameQuery);

        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.GET, "/images/**").permitAll();
        });

        http.headers(headersConfigurer -> headersConfigurer.frameOptions(frameOptions -> frameOptions.disable()));

        super.configure(http);
        this.setLoginView(http, LoginView.class, "/");

        http.formLogin(httpSecurityFormLoginConfigurer -> {
            httpSecurityFormLoginConfigurer.defaultSuccessUrl("/dashboard", true);
        });
    }
}
