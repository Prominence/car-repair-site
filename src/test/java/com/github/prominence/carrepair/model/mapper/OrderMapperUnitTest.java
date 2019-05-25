package com.github.prominence.carrepair.model.mapper;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.formatter.CustomDateTimeFormatter;
import com.github.prominence.carrepair.formatter.OrderStatusFormatter;
import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.model.domain.Order;
import com.github.prominence.carrepair.model.dto.OrderDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration
@Import({OrderStatusFormatter.class, CustomDateTimeFormatter.class})
public class OrderMapperUnitTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusFormatter orderStatusFormatter;

    @Autowired
    private CustomDateTimeFormatter customDateTimeFormatter;

    @Configuration
    public static class Config {
        @Bean
        public OrderMapper orderMapper() {
            return Mappers.getMapper(OrderMapper.class);
        }
    }

    @Test
    public void whenConvertOrderToDto_thenReturnAppropriateOrderDto() {
        // given
        Order order = new Order("", new Client(), new Mechanic(), LocalDateTime.now(), null, BigDecimal.TEN, OrderStatus.DONE.toString());
        order.setId(5L);

        // when
        OrderDto orderDto = orderMapper.orderToOrderDto(order);

        // then
        checkConformity(order, orderDto);
    }

    @Test
    public void whenConvertOrderWithNullsToDto_thenReturnedDtoWillContainNulls() {
        // given
        Order order = new Order(null, null, null, LocalDateTime.now(), null, BigDecimal.TEN, OrderStatus.SCHEDULED.toString());

        // when
        OrderDto orderDto = orderMapper.orderToOrderDto(order);

        // then
        checkConformity(order, orderDto);
    }

    @Test
    public void whenConvertOrderDtoToOrder_thenReturnAppropriateOrder() {
        //given
        OrderDto orderDto = new OrderDto();
        orderDto.setId(4L);
        orderDto.setMechanicId(3L);
        orderDto.setMechanicFirstName("213");
        orderDto.setMechanicMiddleName("1231");
        orderDto.setMechanicLastName("qewqwe");
        orderDto.setClientId(4L);
        orderDto.setClientFirstName("adsas");
        orderDto.setClientMiddleName("Sdas");
        orderDto.setClientLastName("asdasd");
        orderDto.setDescription("asdasdasdsad");
        orderDto.setTotalPrice(BigDecimal.TEN);
        orderDto.setOrderStatus(OrderStatus.DONE.toString());
        orderDto.setCreatedOnDate(customDateTimeFormatter.print(LocalDateTime.now(), null));
        orderDto.setFinishedOnDate(customDateTimeFormatter.print(LocalDateTime.now().plusDays(1), null));

        // when
        Order order = orderMapper.orderDtoToOrder(orderDto);

        // then
        checkConformity(order, orderDto);
    }

    @Test
    public void whenConvertOrderDtoWithNullsToOrder_thenReturnedOrderWillContainNulls() {
        //given
        OrderDto orderDto = new OrderDto();
        orderDto.setDescription("12");
        orderDto.setTotalPrice(BigDecimal.TEN);
        orderDto.setOrderStatus(OrderStatus.SCHEDULED.toString());

        // when
        Order order = orderMapper.orderDtoToOrder(orderDto);

        // then
        checkConformity(order, orderDto);
    }

    @Test
    public void whenConvertOrderListToOrderDtoList_thenReturnDtoList() {
        // given
        List<Order> orderList = Arrays.asList(
                new Order("asdsa", new Client(), new Mechanic(), LocalDateTime.now(), LocalDateTime.now().plusHours(12), BigDecimal.TEN, OrderStatus.SCHEDULED.toString()),
                new Order("asdsa", new Client(), new Mechanic(), LocalDateTime.now(), LocalDateTime.now().plusHours(12), BigDecimal.TEN, OrderStatus.SCHEDULED.toString()),
                new Order("asdsa", new Client(), new Mechanic(), LocalDateTime.now(), LocalDateTime.now().plusHours(12), BigDecimal.TEN, OrderStatus.SCHEDULED.toString()),
                new Order("asdsa", new Client(), new Mechanic(), LocalDateTime.now(), LocalDateTime.now().plusHours(12), BigDecimal.TEN, OrderStatus.SCHEDULED.toString()),
                new Order("asdsa", new Client(), new Mechanic(), LocalDateTime.now(), LocalDateTime.now().plusHours(12), BigDecimal.TEN, OrderStatus.SCHEDULED.toString()),
                new Order("asdsa", new Client(), new Mechanic(), LocalDateTime.now(), LocalDateTime.now().plusHours(12), BigDecimal.TEN, OrderStatus.SCHEDULED.toString()),
                new Order("asdsa", new Client(), new Mechanic(), LocalDateTime.now(), LocalDateTime.now().plusHours(12), BigDecimal.TEN, OrderStatus.SCHEDULED.toString())
        );

        // when
        List<OrderDto> orderDtoList = orderMapper.ordersToOrderDtoList(orderList);

        // then
        for (int i = 0; i < orderList.size(); i++) {
            checkConformity(orderList.get(i), orderDtoList.get(i));
        }
    }

    @Test
    public void whenConvertNullOrder_thenReturnNullDto() {
        assertThat(orderMapper.orderToOrderDto(null)).isNull();
    }

    @Test
    public void whenConvertNullOrderDto_thenReturnNullOrder() {
        assertThat(orderMapper.orderDtoToOrder(null)).isNull();
    }

    @Test
    public void whenConvertNullOrderList_thenReturnNullDtoList() {
        assertThat(orderMapper.ordersToOrderDtoList(null)).isNull();
    }

    private void checkConformity(Order order, OrderDto orderDto) {
        final Client client = order.getClient();
        final Mechanic mechanic = order.getMechanic();
        assertThat(order.getId()).isEqualTo(orderDto.getId());
        if (client == null) {
            assertThat(orderDto.getClientId()).isNull();
            assertThat(orderDto.getClientFirstName()).isNull();
            assertThat(orderDto.getClientMiddleName()).isNull();
            assertThat(orderDto.getClientLastName()).isNull();
        } else {
            assertThat(client.getId()).isEqualTo(orderDto.getClientId());
            assertThat(client.getFirstName()).isEqualTo(orderDto.getClientFirstName());
            assertThat(client.getMiddleName()).isEqualTo(orderDto.getClientMiddleName());
            assertThat(client.getLastName()).isEqualTo(orderDto.getClientLastName());
        }
        if (mechanic == null) {
            assertThat(orderDto.getMechanicId()).isNull();
            assertThat(orderDto.getMechanicFirstName()).isNull();
            assertThat(orderDto.getMechanicMiddleName()).isNull();
            assertThat(orderDto.getMechanicLastName()).isNull();
        } else {
            assertThat(mechanic.getId()).isEqualTo(orderDto.getMechanicId());
            assertThat(mechanic.getFirstName()).isEqualTo(orderDto.getMechanicFirstName());
            assertThat(mechanic.getMiddleName()).isEqualTo(orderDto.getMechanicMiddleName());
            assertThat(mechanic.getLastName()).isEqualTo(orderDto.getMechanicLastName());
        }
        final Locale locale = LocaleContextHolder.getLocale();
        assertThat(customDateTimeFormatter.print(order.getFinishedOn(), null)).isEqualTo(orderDto.getFinishedOnDate());
        assertThat(customDateTimeFormatter.print(order.getCreatedOn(), null)).isEqualTo(orderDto.getCreatedOnDate());
        assertThat(order.getDescription()).isEqualTo(orderDto.getDescription());
        assertThat(order.getOrderStatus()).isEqualTo(orderStatusFormatter.parse(orderDto.getOrderStatus(), locale));
        assertThat(order.getTotalPrice()).isEqualTo(orderDto.getTotalPrice());
    }
}
