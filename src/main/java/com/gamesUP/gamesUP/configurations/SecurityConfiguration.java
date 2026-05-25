package com.gamesUP.gamesUP.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfiguration {

    private JwtTokenFilter jwtTokenFilter;

    public SecurityConfiguration(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    SecurityFilterChain filterchain (HttpSecurity http) throws Exception {
        // définit les patterns des routes qui seront publiques et privées
        return http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/public/**").permitAll()
                            .requestMatchers("/api/private/user").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/api/private/admin").hasRole("ADMIN")
                            .requestMatchers("/api/private/clients").hasAnyRole("USER", "ADMIN")
                            .anyRequest().authenticated();
                    // ajout du filtre custom + de l'élément qui va lui succéder
                }).addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class )
                .httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
