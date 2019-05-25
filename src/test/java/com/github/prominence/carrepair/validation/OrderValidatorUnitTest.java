package com.github.prominence.carrepair.validation;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.formatter.CustomDateTimeFormatter;
import com.github.prominence.carrepair.formatter.OrderStatusFormatter;
import com.github.prominence.carrepair.model.dto.OrderDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration
@Import({OrderValidator.class, CustomDateTimeFormatter.class, OrderStatusFormatter.class})
public class OrderValidatorUnitTest {

    @Configuration
    public static class Config {
        @Bean
        public SmartValidator smartValidator() {
            return new LocalValidatorFactoryBean();
        }
    }

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private CustomDateTimeFormatter customDateTimeFormatter;

    @Autowired
    private OrderStatusFormatter orderStatusFormatter;

    @Test
    public void whenValidateEmptyOrderDto_thenPushErrors() {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getObjectName()).thenReturn("mock");
        OrderDto orderDto = new OrderDto();

        // when
        orderValidator.validate(orderDto, bindingResult);

        // then
        verify(bindingResult, atLeastOnce()).addError(any());
    }

    @Test
    public void whenFinishedOnIsSetButNotAccepted_thenPushError() {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getObjectName()).thenReturn("mock");
        OrderDto orderDto = getTestOrderDto();

        // when
        orderValidator.validate(orderDto, bindingResult);

        // then
        verify(bindingResult, times(1)).rejectValue(eq("finishedOnDate"), anyString());
    }

    @Test
    public void whenFinishedOnIsNotSetAndAccepted_thenPushError() {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getObjectName()).thenReturn("mock");
        OrderDto orderDto = getTestOrderDto();
        orderDto.setFinishedOnDate(null);
        orderDto.setOrderStatus(orderStatusFormatter.print(OrderStatus.ACCEPTED, null));

        // when
        orderValidator.validate(orderDto, bindingResult);

        // then
        verify(bindingResult, times(1)).rejectValue(eq("orderStatus"), anyString());
    }

    @Test
    public void whenFinishedOnIsNotSetAndNotAccepted_thenPushError() {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getObjectName()).thenReturn("mock");
        OrderDto orderDto = getTestOrderDto();
        orderDto.setFinishedOnDate(null);

        // when
        orderValidator.validate(orderDto, bindingResult);

        // then
        verify(bindingResult, never()).rejectValue(eq("orderStatus"), anyString());
    }

    @Test
    public void whenFinishedOnIsBeforeCreatedOn_thenPushError() {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getObjectName()).thenReturn("mock");
        OrderDto orderDto = getTestOrderDto();
        orderDto.setOrderStatus(orderStatusFormatter.print(OrderStatus.ACCEPTED, null));
        orderDto.setCreatedOnDate(customDateTimeFormatter.print(LocalDateTime.now().plusDays(2), null));
        orderDto.setFinishedOnDate(customDateTimeFormatter.print(LocalDateTime.now(), null));

        // when
        orderValidator.validate(orderDto, bindingResult);

        // then
        verify(bindingResult, times(1)).rejectValue(eq("finishedOnDate"), anyString());
    }

    @Test
    public void whenAllIsCorrect_thenNoErrors() {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getObjectName()).thenReturn("mock");
        OrderDto orderDto = getTestOrderDto();
        orderDto.setOrderStatus(orderStatusFormatter.print(OrderStatus.ACCEPTED, null));

        // when
        orderValidator.validate(orderDto, bindingResult);

        // then
        verify(bindingResult, never()).rejectValue(anyString(), anyString());
    }

    private OrderDto getTestOrderDto() {
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
        orderDto.setOrderStatus(orderStatusFormatter.print(OrderStatus.DONE, null));
        orderDto.setCreatedOnDate(customDateTimeFormatter.print(LocalDateTime.now(), null));
        orderDto.setFinishedOnDate(customDateTimeFormatter.print(LocalDateTime.now().plusDays(1), null));

        return orderDto;
    }
}
