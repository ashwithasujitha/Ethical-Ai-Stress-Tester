package com.example.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.CustomUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

 @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/api/users/register")
            || path.equals("/api/users/login");
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {
           
                  String path = request.getServletPath();

//     // ✅ Skip JWT check for public endpoints
//     if (path.equals("/api/users/register") || path.equals("/api/users/login")) {
//         chain.doFilter(request, response);
//         return;
//     }

        String header = request.getHeader("Authorization");

//         System.out.println("JWT FILTER HIT");
// System.out.println("HEADER = " + header);
        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);
            try{

            if (jwtUtil.validateToken(token)) {

                String email = jwtUtil.extractEmail(token);

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                auth.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }
        }
        catch (Exception e) {
           // Invalid token → do nothing
       }
        }

        chain.doFilter(request, response);
    }
}
