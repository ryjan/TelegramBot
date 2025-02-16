package org.ryjan.telegram.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization header not found");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("Received token: " + token);

        try {
            if(!jwtService.validateToken(token)) {
                System.out.println("Invalid token");
                filterChain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            System.out.println("Error validating token " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        String userId = jwtService.extractUserId(token);
        System.out.println("Extracted user ID: " + userId);
        List<String> roles = jwtService.extractRoles(token);

        Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, roles.stream().map(
                SimpleGrantedAuthority::new).toList()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
