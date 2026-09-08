/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author svetik
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                        "/login",
                        "/register")
                    .permitAll()

                .requestMatchers("/admin/**")
                    .hasRole("ADMIN")

                .requestMatchers("/members/**")
                    .hasRole("ADMIN")

                .requestMatchers("/trainers/**")
                    .hasRole("ADMIN")
                .requestMatchers("/fitness-classes/**")
                    .hasRole("ADMIN")

                .requestMatchers("/profile")
                    .hasRole("MEMBER")

                .requestMatchers("/member/**")
                    .hasRole("MEMBER")

                .requestMatchers("/renter/**")
                    .hasRole("MEMBER")

                .requestMatchers("/trainer/**")
                    .hasRole("TRAINER")

                .requestMatchers("/dashboard")
                    .authenticated()

                .anyRequest()
                    .authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
