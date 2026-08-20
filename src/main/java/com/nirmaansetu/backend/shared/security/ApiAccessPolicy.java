package com.nirmaansetu.backend.shared.security;

import com.nirmaansetu.backend.modules.users.entity.Role;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Component
public class ApiAccessPolicy {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final List<String> PUBLIC_PATTERNS = List.of(
            "/api/v1/auth/send-otp",
            "/api/v1/auth/verify-otp",
            "/api/v1/auth/login",
            "/api/v1/auth/send-otp-forgot",
            "/api/v1/auth/reset-password",
            "/api/v1/auth/refresh",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/**"
    );

    private static final List<String> GUEST_PATTERNS = List.of(
            "/api/v1/enquiries",
            "/api/v1/user/register"
    );

    private static final List<String> SHARED_REGISTERED_PATTERNS = List.of(
            "/api/v1/user/**",
            "/api/v1/projects/**",
            "/api/v1/applications/**",
            "/api/v1/orders/**",
            "/api/v1/payments/**",
            "/api/v1/notifications/**"
    );

    private static final List<String> ADMIN_ONLY_PATTERNS = List.of(
            "/api/v1/admin/**",
            "/api/v1/dashboard/**"
    );

    public boolean isPublic(String method, String path) {
        return matchesAny(PUBLIC_PATTERNS, path);
    }

    public boolean isAllowed(Role role, String method, String path) {
        if (role == Role.SUPER_ADMIN || role == Role.ADMIN) {
            return true;
        }

        if (matchesGuestPattern(method, path)) {
            return true;
        }

        if (role == Role.GUEST) {
            return false;
        }

        if (matchesAny(ADMIN_ONLY_PATTERNS, path)) {
            return false;
        }

        if (isAdminOnlyUserEndpoint(method, path)) {
            return false;
        }

        if (matchesAny(SHARED_REGISTERED_PATTERNS, path)) {
            return true;
        }

        return matchesRolePrefix(role, path);
    }

    private boolean matchesGuestPattern(String method, String path) {
        if (HttpMethod.POST.matches(method) && PATH_MATCHER.match("/api/v1/enquiries", path)) {
            return true;
        }
        if (HttpMethod.POST.matches(method) && PATH_MATCHER.match("/api/v1/user/register", path)) {
            return true;
        }
        return false;
    }

    private boolean isAdminOnlyUserEndpoint(String method, String path) {
        return HttpMethod.GET.matches(method) && PATH_MATCHER.match("/api/v1/user/all", path);
    }

    private boolean matchesRolePrefix(Role role, String path) {
        return switch (role) {
            case EMPLOYEE -> matchesAny(List.of("/api/v1/employees/**"), path);
            case EMPLOYER -> matchesAny(List.of("/api/v1/employers/**"), path);
            case SUPPLIER -> matchesAny(List.of("/api/v1/shops/**"), path);
            default -> false;
        };
    }

    private boolean matchesAny(List<String> patterns, String path) {
        return patterns.stream().anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    public Set<Role> registeredRoles() {
        return EnumSet.of(Role.EMPLOYEE, Role.EMPLOYER, Role.SUPPLIER, Role.ADMIN, Role.SUPER_ADMIN);
    }
}
