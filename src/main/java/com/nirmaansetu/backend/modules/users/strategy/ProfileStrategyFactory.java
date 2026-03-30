package com.nirmaansetu.backend.modules.users.strategy;

import com.nirmaansetu.backend.modules.users.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProfileStrategyFactory {

    private final Map<Role, ProfileStrategy> strategies;

    @Autowired
    public ProfileStrategyFactory(List<ProfileStrategy> strategyList) {
        strategies = strategyList.stream()
                .collect(Collectors.toMap(ProfileStrategy::getRole, strategy -> strategy));
    }

    public ProfileStrategy getStrategy(Role role) {
        ProfileStrategy strategy = strategies.get(role);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for role: " + role);
        }
        return strategy;
    }
}
