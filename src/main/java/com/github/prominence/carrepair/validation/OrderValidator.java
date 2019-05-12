package com.github.prominence.carrepair.validation;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class OrderValidator implements Validator {
    private static final Logger logger = LogManager.getLogger(OrderValidator.class);

    @Override
    public boolean supports(Class<?> aClass) {
        return Order.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Order order = (Order) o;
        LocalDateTime finishedOn = order.getFinishedOn();
        if (finishedOn != null) {
            LocalDateTime createdOn = order.getCreatedOn();
            if (createdOn != null && order.getFinishedOn().isBefore(order.getCreatedOn())) {
                logger.debug("[{}] validation error: \"finishedOn\" value[{}] is before \"createdOn\"[{}].",
                        () -> order, () -> finishedOn, () -> createdOn);
                errors.rejectValue("finishedOn", "error.finishedOn.finishedBeforeStarted");
            }

            if (order.getOrderStatus() != OrderStatus.ACCEPTED) {
                logger.debug("[{}] validation error: \"finishedOn\" cannot be set for order in status differ from {}.",
                        () -> order, () -> OrderStatus.ACCEPTED);
                errors.rejectValue("finishedOn", "error.finishedOn.incompatibleStatus");
            }
        }
    }
}
