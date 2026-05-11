package com.fooddelivery.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

 @Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, IOException {

    String path = request.getServletPath();

    // ✅ 1. Skip auth endpoints
    if (path.startsWith("/auth")) {
        filterChain.doFilter(request, response);
        return;
    }

    String header = request.getHeader("Authorization");

    String token = null;
    String email = null;
    String role = null;

    // ✅ 2. Check header properly
    if (header != null && header.startsWith("Bearer ")) {
        token = header.substring(7);

        try {
            // ✅ 3. Validate token format before parsing
            if (token.split("\\.").length == 3) {
                email = jwtUtil.extractEmail(token);
                role = jwtUtil.extractRole(token);
            }
        } catch (Exception e) {
            // ✅ 4. Never crash app
            filterChain.doFilter(request, response);
            return;
        }
    }

    // ✅ 5. Set authentication only if valid
    if (email != null &&
            SecurityContextHolder.getContext().getAuthentication() == null &&
            jwtUtil.validateToken(token)) {

        var authorities = List.of(
                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role)
        );

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
}
}