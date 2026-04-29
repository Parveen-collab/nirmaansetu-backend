package com.nirmaansetu.backend.modules.dashboard.service;

import com.nirmaansetu.backend.modules.dashboard.dto.DashboardStatsDto;
import com.nirmaansetu.backend.modules.enquiries.repository.EnquiryRepository;
import com.nirmaansetu.backend.modules.orders.repository.OrderRepository;
import com.nirmaansetu.backend.modules.projects.repository.ProjectRepository;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final EnquiryRepository enquiryRepository;
    private final ProjectRepository projectRepository;
    private final OrderRepository orderRepository;

    public DashboardStatsDto getGlobalStats() {
        Double revenue = orderRepository.calculateTotalRevenue();
        return DashboardStatsDto.builder()
                .totalUsers(userRepository.count())
                .totalEnquiries(enquiryRepository.count())
                .totalProjects(projectRepository.count())
                .totalOrders(orderRepository.count())
                .totalRevenue(revenue != null ? revenue : 0.0)
                .build();
    }
}
