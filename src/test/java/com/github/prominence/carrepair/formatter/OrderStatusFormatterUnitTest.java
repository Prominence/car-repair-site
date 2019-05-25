package com.github.prominence.carrepair.formatter;

import com.github.prominence.carrepair.enums.OrderStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
public class OrderStatusFormatterUnitTest {

    private OrderStatusFormatter orderStatusFormatter = new OrderStatusFormatter();

    @Test
    public void whenParseNull_thenReturnNull() {
        assertThat(orderStatusFormatter.parse(null, null)).isNull();
    }

    @Test
    public void whenPrintNull_thenReturnNUll() {
        assertThat(orderStatusFormatter.print(null, null)).isNull();
    }

    @Test
    public void whenParseCorrectValue_thenReturnOrderStatus() {
        assertThat(orderStatusFormatter.parse("Done", null)).isEqualTo(OrderStatus.DONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenParseIncorrectValue_thenThrowAnException() {
        // expected exception
        orderStatusFormatter.parse("Incorrect", null);
    }

    @Test
    public void whenPrintCorrectValue_thenReturnCorrectString() {
        assertThat(orderStatusFormatter.print(OrderStatus.SCHEDULED, null)).isEqualTo("Scheduled");
    }
}
