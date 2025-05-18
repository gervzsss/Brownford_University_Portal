package com.brownford.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/error", "/forgot-password")
                        .permitAll()
                        .requestMatchers("/admin-dashboard/**", "/admin-users/**", "/api/admin/users/**").hasAuthority("admin")
                        .requestMatchers("/faculty-dashboard/**", "/faculty-users/**").hasAuthority("faculty")
                        .requestMatchers("/student-home/**").hasAuthority("student")
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customSuccessHandler())
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                for (GrantedAuthority auth : authentication.getAuthorities()) {
                    String role = auth.getAuthority();
                    if ("admin".equalsIgnoreCase(role)) {
                        response.sendRedirect("/admin-dashboard");
                        return;
                    } else if ("faculty".equalsIgnoreCase(role)) {
                        response.sendRedirect("/faculty-dashboard");
                        return;
                    } else if ("student".equalsIgnoreCase(role)) {
                        response.sendRedirect("/student-home");
                        return;
                    }
                }
                response.sendRedirect("/"); // fallback
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // For demo only! Use BCryptPasswordEncoder in production.
    }
}