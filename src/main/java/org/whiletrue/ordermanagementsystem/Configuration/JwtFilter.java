package org.whiletrue.ordermanagementsystem.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.whiletrue.ordermanagementsystem.Services.Security.JwtService;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

/**
 * Filter responsible for handling JWT authentication for each incoming HTTP request.
 * <p>
 * This filter checks the "Authorization" header for a valid Bearer token.
 * If the token is present and valid, the user is authenticated and the security context is updated.
 * <p>
 * The filter runs once per request by extending {@link OncePerRequestFilter}.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructs the JwtFilter with required dependencies.
     *
     * @param jwtService          Service for extracting and validating JWT tokens.
     * @param userDetailsService  Spring Security service for loading user details from username.
     */
    @Autowired
    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests to extract and validate the JWT token from the Authorization header.
     * <p>
     * If a valid token is found and the user is not yet authenticated, this method sets the authentication
     * context for the current request. Otherwise, the request continues without authentication.
     *
     * @param request     The incoming HTTP request.
     * @param response    The outgoing HTTP response.
     * @param filterChain The filter chain to pass the request to the next filter.
     * @throws ServletException In case of a servlet error.
     * @throws IOException      In case of an I/O error.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Check for Bearer token in Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token and validate
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        // Authenticate user if token is valid and not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
