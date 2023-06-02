package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.jwtFilter.JwtTokenFilter;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration 
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {
    
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${debug.mode.disable.jwt}")
    private boolean jwtDisabled;


    public WebSecurityConfiguration(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }
    
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
        //Enable cross-origin requests
        httpSecurity.cors();

        if(!jwtDisabled){
            //Config authorisation for request paths
            httpSecurity.authorizeHttpRequests()
            .requestMatchers(AntPathRequestMatcher.antMatcher("/auth/**")).permitAll()
            .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
            .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/users")).permitAll()
            .anyRequest().authenticated();
        }
        
            
        //Make sure own JWT filter is executed
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();
    }
}
