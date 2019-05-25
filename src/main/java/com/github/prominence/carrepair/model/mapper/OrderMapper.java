package com.github.prominence.carrepair.model.mapper;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.formatter.CustomDateTimeFormatter;
import com.github.prominence.carrepair.formatter.OrderStatusFormatter;
import com.github.prominence.carrepair.model.domain.Order;
import com.github.prominence.carrepair.model.dto.OrderDto;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class OrderMapper {

    @Autowired
    protected OrderStatusFormatter orderStatusFormatter;

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
    abstract public OrderDto orderToOrderDto(Order order);

    @InheritInverseConfiguration
    abstract public Order orderDtoToOrder(OrderDto orderDto);

    abstract public List<OrderDto> ordersToOrderDtoList(List<Order> orders);

    public String orderStatusToString(OrderStatus orderStatus) {
        return orderStatusFormatter.print(orderStatus, null);
    }

    public  OrderStatus stringToOrderStatus(String status) {
        return orderStatusFormatter.parse(status, null);
    }

    @BeforeMapping
    protected void checkForEmptyStrings(OrderDto orderDto) {
        if (orderDto == null) return;
        if (StringUtils.isEmpty(orderDto.getCreatedOnDate())) {
            orderDto.setCreatedOnDate(null);
        }

        if (StringUtils.isEmpty(orderDto.getFinishedOnDate())) {
            orderDto.setFinishedOnDate(null);
        }
    }
}
