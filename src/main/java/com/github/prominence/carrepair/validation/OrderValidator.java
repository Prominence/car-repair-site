package com.github.prominence.carrepair.validation;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.formatter.CustomDateTimeFormatter;
import com.github.prominence.carrepair.formatter.OrderStatusFormatter;
import com.github.prominence.carrepair.model.dto.OrderDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class OrderValidator implements Validator {
    private static final Logger logger = LogManager.getLogger(OrderValidator.class);

    private CustomDateTimeFormatter customDateTimeFormatter;
    private OrderStatusFormatter orderStatusFormatter;
    private SmartValidator smartValidator;

    public OrderValidator(CustomDateTimeFormatter customDateTimeFormatter, OrderStatusFormatter orderStatusFormatter, SmartValidator smartValidator) {
        this.customDateTimeFormatter = customDateTimeFormatter;
        this.orderStatusFormatter = orderStatusFormatter;
        this.smartValidator = smartValidator;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return OrderDto.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        OrderDto order = (OrderDto) o;

        smartValidator.validate(order, errors);

        Locale locale = LocaleContextHolder.getLocale();
        OrderStatus orderStatus = orderStatusFormatter.parse(order.getOrderStatus(), locale);

        if (StringUtils.isNotBlank(order.getFinishedOnDate())) {
            LocalDateTime finishedOn = customDateTimeFormatter.parse(order.getFinishedOnDate(), locale);
            LocalDateTime createdOn = customDateTimeFormatter.parse(order.getCreatedOnDate(), locale);
            if (createdOn != null && finishedOn != null && finishedOn.isBefore(createdOn)) {
                logger.debug("[{}] validation error: \"finishedOnDate\" value[{}] is before \"createdOn\"[{}].",
                        () -> order, () -> finishedOn, () -> createdOn);
                errors.rejectValue("finishedOnDate", "error.finishedOn.finishedBeforeStarted");
            }


            if (finishedOn != null && orderStatus != OrderStatus.ACCEPTED) {
                logger.debug("[{}] validation error: \"finishedOn\" cannot be set for order in status differ from {}.",
                        () -> order, () -> OrderStatus.ACCEPTED);
                errors.rejectValue("finishedOnDate", "error.finishedOn.incompatibleStatus");
            }
        } else {
            if (orderStatus == OrderStatus.ACCEPTED) {
                logger.debug("[{}] validation error: \"orderStatus\" cannot be set to {} until \"finishedOn\" isn't set.",
                        () -> order, () -> OrderStatus.ACCEPTED);
                errors.rejectValue("orderStatus", "error.orderStatus.acceptedButNotFinished");
            }
        }
    }
}
