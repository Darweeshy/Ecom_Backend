package org.example.springecom.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springecom.service.JwtService;
import org.example.springecom.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // This list defines all URL patterns that the JWT filter should completely
        // ignore.
        final List<String> publicPaths = Arrays.asList(
                "/api/register",
                "/api/login",
                "/api/forgot-password",
                "/api/reset-password",
                "/api/webhooks/**",
                "/images/**",
                "/api/products/**",
                "/api/categories/**",
                "/api/pages/**",
                "/api/orders/track/**",
                "/api/coupons/validate",
                "/error");

        AntPathMatcher pathMatcher = new AntPathMatcher();
        return publicPaths.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userName = jwtService.extractUserName(token);
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            MyUserDetailsService userDetailsService = context.getBean(MyUserDetailsService.class);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}