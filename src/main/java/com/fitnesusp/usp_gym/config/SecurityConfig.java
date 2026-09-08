/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

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
/*
    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
*/
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            /*.authorizeHttpRequests(auth -> auth

                // Login page can be opened without authentication
                .requestMatchers("/login").permitAll()

                // Only ADMIN can access Member management
                .requestMatchers("/members/**").hasRole("ADMIN")

                // Everything else requires login
                .anyRequest().authenticated()
            )*/
            .authorizeHttpRequests(auth -> auth

                    .requestMatchers(
                            "/",
                            "/login",
                            "/register",
                            "/css/**",
                            "/js/**",
                            "/webjars/**"
                    )
                    .permitAll()

                .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/members/**")
                    .hasRole("ADMIN")
                    
                .requestMatchers("/trainers/**")
                    .hasRole("ADMIN")    
                .requestMatchers("/fitness-classes/**")
                    .hasRole("ADMIN")    
                .requestMatchers("/admin/facilities/**")
                    .hasRole("ADMIN")

                .requestMatchers("/admin/rentals/**")
                    .hasRole("ADMIN")    

                .requestMatchers("/profile")
                    .hasRole("MEMBER")
                    
                .requestMatchers("/renter/**")
                    .hasRole("MEMBER")    
                    
                .requestMatchers("/trainer/**")
                    .hasRole("TRAINER")   
                .requestMatchers("/profile")
                    .hasRole("MEMBER")    

                .requestMatchers("/dashboard")
                    .authenticated()

                .anyRequest()
                    .authenticated()
            )
                
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl(/*"/members"*/"/dashboard", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
