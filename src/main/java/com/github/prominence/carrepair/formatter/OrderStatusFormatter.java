package com.github.prominence.carrepair.formatter;

import com.github.prominence.carrepair.enums.OrderStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class OrderStatusFormatter implements Formatter<OrderStatus> {
    private static final Logger logger = LogManager.getLogger(OrderStatusFormatter.class);

    @Override
    public OrderStatus parse(String text, Locale locale) {
        if (text == null) return null;
        final OrderStatus parsedOrderStatus = OrderStatus.valueOf(text.toUpperCase());
        logger.trace("Parsing String[{}] to OrderStatus instance: {}.", () -> text, () -> parsedOrderStatus);
        return parsedOrderStatus;
    }

    @Override
    public String print(OrderStatus object, Locale locale) {
        if (object == null) return null;
        final String formattedOrderStatus = StringUtils.capitalize(object.toString().toLowerCase());
        logger.trace("Formatting OrderStatus[{}] to String instance: {}.", () -> object, () -> formattedOrderStatus);
        return formattedOrderStatus;
    }
}
