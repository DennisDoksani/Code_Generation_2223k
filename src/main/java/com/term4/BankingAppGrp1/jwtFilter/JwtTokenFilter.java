package com.term4.BankingAppGrp1.jwtFilter;

import io.jsonwebtoken.JwtException;
import java.io.IOException; 

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.term4.BankingAppGrp1.util.JwtTokenProvider;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Get the JWT token (returns null if no token is provided)
        String token = getToken(request);

        // If a token was provided, validate the token.
        if (token != null) {
            try {
                // We need a Spring Authentication object to set the Spring Security context
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
    
                // Set the context, at this point, the user is authenticated
                SecurityContextHolder.getContext().setAuthentication(authentication);
    
            } 
            // If the token is invalid, return an Unauthorised error response
            catch (JwtException ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token");
                response.getWriter().flush();
                return;
            } 
            // If something else went wrong, return an Internal Server Error response
            catch (Exception ex) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Something went wrong. Please try again later.");
                response.getWriter().flush();
                return;
            }
        }

        // Continue. If no token was provided and the request needed authentication, the request will be rejected.
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            //Returns the string that comes after "Bearer ": which is the token
            return authHeader.substring(7);
        }
        //Otherwise return null
        return null;
    }
}
