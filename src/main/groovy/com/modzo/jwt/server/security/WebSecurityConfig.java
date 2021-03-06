package com.modzo.jwt.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 2)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    void globalUserDetails(AuthenticationManagerBuilder auth,
                           LocalUserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll()
                .and().csrf().disable();
    }
}