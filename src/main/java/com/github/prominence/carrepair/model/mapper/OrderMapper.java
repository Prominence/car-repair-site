package com.github.prominence.carrepair.model.mapper;

import com.github.prominence.carrepair.formatter.CustomDateTimeFormatter;
import com.github.prominence.carrepair.model.domain.Order;
import com.github.prominence.carrepair.model.dto.OrderDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "client.id", target = "clientId"),
            @Mapping(source = "client.firstName", target = "clientFirstName"),
            @Mapping(source = "client.middleName", target = "clientMiddleName"),
            @Mapping(source = "client.lastName", target = "clientLastName"),
            @Mapping(source = "mechanic.id", target = "mechanicId"),
            @Mapping(source = "mechanic.firstName", target = "mechanicFirstName"),
            @Mapping(source = "mechanic.middleName", target = "mechanicMiddleName"),
            @Mapping(source = "mechanic.lastName", target = "mechanicLastName"),
            @Mapping(source = "createdOn", target = "createdOnDate", dateFormat = CustomDateTimeFormatter.DATETIME_PATTERN),
            @Mapping(source = "finishedOn", target = "finishedOnDate", dateFormat = CustomDateTimeFormatter.DATETIME_PATTERN)
    })
    OrderDto orderToOrderDto(Order order);

    @InheritInverseConfiguration
    Order orderDtoToOrder(OrderDto orderDto);

    List<OrderDto> ordersToOrderDtoList(List<Order> orders);
}
