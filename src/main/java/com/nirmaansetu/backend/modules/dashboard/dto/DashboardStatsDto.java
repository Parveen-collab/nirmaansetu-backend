package com.nirmaansetu.backend.modules.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDto {
    private long totalUsers;
    private long totalEnquiries;
    private long totalProjects;
    private long totalOrders;
    private double totalRevenue;
}
