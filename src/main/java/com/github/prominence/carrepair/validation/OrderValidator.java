package com.github.prominence.carrepair.validation;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class OrderValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Order.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Order order = (Order) o;
        LocalDateTime finishedOn = order.getFinishedOn();
        if (finishedOn != null) {
            LocalDateTime startedOn = order.getCreatedOn();
            if (startedOn != null && order.getFinishedOn().isBefore(order.getCreatedOn())) {
                errors.rejectValue("finishedOn", "error.finishedOn.finishedBeforeStarted");
            }

            if (order.getOrderStatus() != OrderStatus.ACCEPTED) {
                errors.rejectValue("finishedOn", "error.finishedOn.incompatibleStatus");
            }
        }
    }
}
