package com.github.prominence.carrepair.formatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
public class CustomDateTimeFormatterUnitTest {

    private CustomDateTimeFormatter customDateTimeFormatter = new CustomDateTimeFormatter();

    @Test
    public void whenParseNull_thenReturnNull() {
        assertThat(customDateTimeFormatter.parse(null, null)).isNull();
    }

    @Test
    public void whenPrintNull_thenReturnNUll() {
        assertThat(customDateTimeFormatter.print(null, null)).isNull();
    }

    @Test
    public void whenParseCorrectValue_thenReturnLocalDateTime() {
        LocalDateTime expectedLocalDateTime = LocalDateTime.of(1996, 2, 7, 14, 12, 12);
        assertThat(customDateTimeFormatter.parse("1996-02-07 14:12:12", null)).isEqualTo(expectedLocalDateTime);
    }

    @Test(expected = DateTimeParseException.class)
    public void whenParseIncorrectValue_thenThrowAnException() {
        // expected exception
        customDateTimeFormatter.parse("qwerty", null);
    }

    @Test
    public void whenPrintCorrectValue_thenReturnCorrectString() {
        assertThat(customDateTimeFormatter.print(LocalDateTime.of(1996, 2, 7, 14, 12, 12), null)).isEqualTo("1996-02-07 14:12:12");
    }
}
