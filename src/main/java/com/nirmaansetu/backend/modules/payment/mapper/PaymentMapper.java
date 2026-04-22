package com.nirmaansetu.backend.modules.payment.mapper;

import com.nirmaansetu.backend.modules.payment.dto.PaymentDto;
import com.nirmaansetu.backend.modules.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    @Mapping(target = "userId", source = "user.id")
    PaymentDto toDto(Payment payment);

    @Mapping(target = "user.id", source = "userId")
    Payment toEntity(PaymentDto dto);

    List<PaymentDto> toDtoList(List<Payment> payments);
}
