package com.brownford.config;

import com.brownford.model.User;
import com.brownford.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // Update last login time
        String username = authentication.getName();
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });

        // Handle role-based redirection
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
}