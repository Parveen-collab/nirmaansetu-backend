package com.nirmaansetu.backend.modules.dashboard.service;

import com.nirmaansetu.backend.modules.dashboard.dto.DashboardStatsDto;
import com.nirmaansetu.backend.modules.enquiries.repository.EnquiryRepository;
import com.nirmaansetu.backend.modules.orders.repository.OrderRepository;
import com.nirmaansetu.backend.modules.projects.repository.ProjectRepository;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for providing dashboard statistics.
 * Aggregates data from various repositories to provide a high-level overview of the system.
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final EnquiryRepository enquiryRepository;
    private final ProjectRepository projectRepository;
    private final OrderRepository orderRepository;

    /**
     * Retrieves global statistics including total counts of users, enquiries, projects, orders, and total revenue.
     * 
     * @return DashboardStatsDto containing the aggregated statistics
     */
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
