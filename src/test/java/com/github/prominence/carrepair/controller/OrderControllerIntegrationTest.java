package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.model.domain.Order;
import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import com.github.prominence.carrepair.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MechanicService mechanicService;

    @Test
    public void whenRequestIndexPage_thenReturnFirstPageOfIndex() throws Exception {
        // given
        Order testOrder = getTestOrder();
        orderService.save(testOrder);

        // expected
        mockMvc.perform(
                get("/order")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("orderList", "totalPages")).
                andExpect(model().attribute("totalPages", 1)).
                andExpect(model().attribute("orderList", hasSize(greaterThanOrEqualTo(1)))).
                andExpect(view().name("order/index"));
    }

    @Test
    public void whenRequestIndexSecondPage_thenReturnSecondPageOfIndex() throws Exception {
        // given
        Stream.generate(this::getTestOrder).limit(11).forEach(orderService::save);

        // expected
        mockMvc.perform(
                get("/order").param("page", "1")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("orderList", "totalPages")).
                andExpect(model().attribute("totalPages", 2)).
                andExpect(model().attribute("orderList", hasSize(lessThan(10)))).
                andExpect(view().name("order/index"));

    }

    @Test
    public void whenRequestCreateOrderPage_thenReturnCreatePageWithEmptyDto() throws Exception {
        mockMvc.perform(
                get("/order/create")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("orderDto")).
                andExpect(view().name("order/edit"));
    }

    @Test
    public void whenRequestEditWithoutId_thenReturnNotFount() throws Exception {
        mockMvc.perform(
                get("/order/edit")
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestEditWithIncorrectId_thenRedirectToIndex() throws Exception {
        mockMvc.perform(
                get("/order/edit/{id}", -34)
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestEditWithCorrectId_thenReturnEditPage() throws Exception {
        // given
        Order testOrder = getTestOrder();
        Long id = orderService.save(testOrder).getId();

        // expected
        mockMvc.perform(
                get("/order/edit/{id}", id)
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("orderDto")).
                andExpect(view().name("order/edit"));
    }

    @Test
    public void whenSubmitCorrectOrderFromEditPage_thenRedirectToIndex() throws Exception {
        mockMvc.perform(
                post("/order/update/{id}", 100).
                        param("id", "100").
                        param("description", "testDescription").
                        param("clientId", "1").
                        param("mechanicId", "1").
                        param("createdOnDate", "1996-02-07 14:12:12").
                        param("totalPrice", "123").
                        param("orderStatus", "Scheduled")
        ).
                andDo(print()).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/order"));
    }

    @Test
    public void whenSubmitIncorrectOrderFromEditPage_thenStayOnPage() throws Exception {
        mockMvc.perform(
                post("/order/update/{id}", 100).
                        param("id", "100").
                        param("description", "testDescription").
                        param("clientId", "1")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(view().name("order/edit"));
    }

    @Test
    public void whenSubmitCorrectOrderFromCreatePage_thenRedirectToIndex() throws Exception {
        mockMvc.perform(
                post("/order/create").
                        param("id", "100").
                        param("description", "testDescription").
                        param("clientId", "1").
                        param("mechanicId", "1").
                        param("createdOnDate", "1996-02-07 14:12:12").
                        param("totalPrice", "123").
                        param("orderStatus", "Scheduled")
        ).
                andDo(print()).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/order"));
    }

    @Test
    public void whenSubmitIncorrectOrderFromCreatePage_thenStayOnPage() throws Exception {
        mockMvc.perform(
                post("/order/create").
                        param("id", "100").
                        param("description", "testDescription").
                        param("clientId", "1").
                        param("totalPrice", "123").
                        param("orderStatus", "Scheduled")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(view().name("order/edit"));
    }

    @Test
    public void whenDeleteCorrectOrder_thenReturnOk() throws Exception {
        // given
        Order testOrder = getTestOrder();
        Long id = orderService.save(testOrder).getId();

        // expected
        mockMvc.perform(
                delete("/order/delete/{id}", id)
        ).
                andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    public void whenDeleteIncorrectOrder_thenReturnNotFount() throws Exception {
        mockMvc.perform(
                delete("/order/delete/{id}", -23)
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteWithoutId_thenReturnNotFound() throws Exception {
        mockMvc.perform(
                delete("/order/delete")
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    private Order getTestOrder() {
        Client testClient = new Client("1", "1", "1", "1");
        clientService.save(testClient);
        Mechanic testMechanic = new Mechanic("1", "1", "1", BigDecimal.ONE);
        mechanicService.save(testMechanic);
        return new Order("11", testClient, testMechanic, LocalDateTime.now(), null, BigDecimal.TEN, OrderStatus.SCHEDULED.toString());
    }
}
