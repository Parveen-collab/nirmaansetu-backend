package com.nirmaansetu.backend.modules.orders.mapper;

import com.nirmaansetu.backend.modules.orders.dto.OrderItemResponseDto;
import com.nirmaansetu.backend.modules.orders.dto.OrderResponseDto;
import com.nirmaansetu.backend.modules.orders.entity.Order;
import com.nirmaansetu.backend.modules.orders.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    OrderResponseDto toResponseDto(Order order);

    @Mapping(target = "materialId", source = "material.id")
    @Mapping(target = "materialName", source = "material.name")
    OrderItemResponseDto toItemResponseDto(OrderItem item);

    List<OrderResponseDto> toResponseDtoList(List<Order> orders);
}
