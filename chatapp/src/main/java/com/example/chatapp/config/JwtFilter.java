package com.example.chatapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ Allow login/register APIs without token
        if (path.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("🔍 Incoming JWT token: " + token);

            try {
                username = jwtUtil.extractUsername(token);
                System.out.println("👤 Extracted username from token: " + username);
            } catch (Exception e) {
                System.out.println("❌ Error extracting username from token: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ No Bearer token found in Authorization header.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("✅ Loaded user from DB: " + userDetails.getUsername());

                if (jwtUtil.validateToken(token, userDetails)) {
                    System.out.println("🔐 Token is valid. Setting authentication...");

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // changed . 2 to see the roles
                    System.out.println("🔒 User Roles: " + userDetails.getAuthorities());


                } else {
                    System.out.println("❌ Token is invalid or expired!");
                }

            } catch (Exception e) {
                System.out.println("❌ Error during token validation: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}
