package com.github.prominence.carrepair.formatter;

import com.github.prominence.carrepair.enums.OrderStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class OrderStatusFormatter implements Formatter<OrderStatus> {
    @Override
    public OrderStatus parse(String text, Locale locale) throws ParseException {
        return OrderStatus.valueOf(text.toUpperCase());
    }

    @Override
    public String print(OrderStatus object, Locale locale) {
        return StringUtils.capitalize(object.toString().toLowerCase());
    }
}
