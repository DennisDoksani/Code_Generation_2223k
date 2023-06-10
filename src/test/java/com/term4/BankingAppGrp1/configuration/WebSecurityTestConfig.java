package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.jwtFilter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@TestConfiguration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityTestConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Value("${debug.mode.disable.jwt}")
    private boolean jwtDisabled;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //Allow post requests
        httpSecurity.csrf().disable();
        // enabling CORS to allow requests from all origins
        httpSecurity.cors();

        //Disable security headers
        httpSecurity.headers().frameOptions().disable();
        //Disable session creations
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if(!jwtDisabled){
            //Config authorisation for request paths
            httpSecurity.authorizeHttpRequests()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/auth/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/users")).permitAll()
                    .anyRequest().authenticated();
        }

        return httpSecurity.build();
    }
}