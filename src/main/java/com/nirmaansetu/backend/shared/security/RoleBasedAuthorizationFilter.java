package com.nirmaansetu.backend.shared.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nirmaansetu.backend.modules.users.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class RoleBasedAuthorizationFilter extends OncePerRequestFilter {

    private final ApiAccessPolicy apiAccessPolicy;
    private final ObjectMapper objectMapper;

    public RoleBasedAuthorizationFilter(ApiAccessPolicy apiAccessPolicy, ObjectMapper objectMapper) {
        this.apiAccessPolicy = apiAccessPolicy;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (apiAccessPolicy.isPublic(method, path)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken
                || !(authentication.getPrincipal() instanceof User user)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!apiAccessPolicy.isAllowed(user.getRole(), method, path)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), Map.of(
                    "error", "Forbidden",
                    "message", "You do not have permission to access this resource. Complete registration to unlock more features."
            ));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
