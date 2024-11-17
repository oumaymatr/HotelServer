package com.example.HotelServer.configs;

import com.example.HotelServer.services.jwt.UserService;
import com.example.HotelServer.util.JwtUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest; import jakarta.servlet.http.HttpServletResponse;
import lombok. RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;  // JWT utility class
  private final UserService userService;  // Service to load user details

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
    this.jwtUtil = jwtUtil;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    // Extract the Authorization header from the request
    final String authHeader = request.getHeader("Authorization");

    // Check if the header exists and if it starts with "Bearer "
    if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response); // Proceed if no valid JWT token
      return;
    }

    // Extract the JWT from the header (skip "Bearer " prefix)
    final String jwt = authHeader.substring(7);
    final String userEmail = jwtUtil.extractUserName(jwt); // Extract user email from JWT

    // If the userEmail is valid and the authentication context is null, process the JWT
    if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
      // Load the user details based on the email
      UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

      // Check if the token is still valid
      if (jwtUtil.isTokenValid(jwt, userDetails)) {
        // Create an empty authentication context
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Create an authentication token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null, // No credentials, as it's based on JWT
          userDetails.getAuthorities()
        );

        // Set the details of the authentication (e.g., the request details)
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Set the authentication in the SecurityContext
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
      }
    }

    // Proceed with the filter chain (next filter or the actual handler)
    filterChain.doFilter(request, response);
  }
}
