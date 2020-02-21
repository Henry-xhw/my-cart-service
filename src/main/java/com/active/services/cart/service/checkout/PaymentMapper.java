package com.active.services.cart.service.checkout;

import com.active.services.cart.domain.Payment;
import com.active.services.order.management.api.v3.types.PaymentDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PaymentMapper {

    PaymentMapper MAPPER = Mappers.getMapper(PaymentMapper.class);

    @Mappings({@Mapping(target = "txId", source = "identifier"),
            @Mapping(target = "billingContact", ignore = true)
    })
    PaymentDTO convert(Payment payment);

    List<PaymentDTO> convert(List<Payment> payments);
}
