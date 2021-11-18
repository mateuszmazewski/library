package com.github.mateuszmazewski.library.security;

import com.github.mateuszmazewski.library.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        setLoginView(http, LoginView.class);

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/authors").hasAnyRole("USER, ADMIN")
                .antMatchers("/books").hasAnyRole("USER, ADMIN")
                .antMatchers("/categories").hasAnyRole("USER, ADMIN")
                .antMatchers("/genres").hasAnyRole("USER, ADMIN")
                .antMatchers("/publishers").hasAnyRole("USER, ADMIN")
                .antMatchers("/readers").hasAnyRole("USER, ADMIN")
                .antMatchers("/").permitAll()
                .and().formLogin();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
