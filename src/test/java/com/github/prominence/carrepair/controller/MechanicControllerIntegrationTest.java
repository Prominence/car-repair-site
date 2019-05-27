package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.service.MechanicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.AllOf.allOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class MechanicControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MechanicService mechanicService;

    @Test
    public void whenRequestIndexPage_thenReturnFirstPageOfIndex() throws Exception {
        // given
        Mechanic testMechanic = new Mechanic("testFirstName", "testMiddleName", "testLastName", BigDecimal.ONE);
        mechanicService.save(testMechanic);

        // expected
        mockMvc.perform(
                get("/mechanic")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("mechanicList", "totalPages")).
                andExpect(model().attribute("totalPages", 11)). // 100 demo mechanics + 1 from setup
                andExpect(model().attribute("mechanicList", hasSize(10))).
                andExpect(view().name("mechanic/index"));
    }

    @Test
    public void whenRequestIndexLastPage_thenReturnLastPageOfIndex() throws Exception {
        // given
        Mechanic testMechanic = new Mechanic("testFirstName", "testMiddleName", "testLastName", BigDecimal.ONE);
        mechanicService.save(testMechanic);

        // expected
        mockMvc.perform(
                get("/mechanic").param("page", "10")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("mechanicList", "totalPages")).
                andExpect(model().attribute("totalPages", 11)). // 100 demo mechanics + 1 from setup
                andExpect(model().attribute("mechanicList", hasSize(lessThan(10)))).
                andExpect(view().name("mechanic/index"));

    }

    @Test
    public void whenRequestCreateMechanicPage_thenReturnCreatePageWithEmptyDto() throws Exception {
        mockMvc.perform(
                get("/mechanic/create")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("mechanicDto")).
                andExpect(view().name("mechanic/edit"));
    }

    @Test
    public void whenRequestEditWithoutId_thenReturnNotFount() throws Exception {
        mockMvc.perform(
                get("/mechanic/edit")
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestEditWithIncorrectId_thenRedirectToIndex() throws Exception {
        mockMvc.perform(
                get("/mechanic/edit/{id}", -34)
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestEditWithCorrectId_thenReturnEditPage() throws Exception {
        mockMvc.perform(
                get("/mechanic/edit/{id}", 1)
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("mechanicDto")).
                andExpect(view().name("mechanic/edit"));
    }

    @Test
    public void whenSubmitCorrectMechanicFromEditPage_thenRedirectToIndex() throws Exception {
        mockMvc.perform(
                post("/mechanic/update/{id}", 100).
                        param("id", "100").
                        param("firstName", "changedTestFirstName").
                        param("middleName", "changedTestMiddleName").
                        param("lastName", "changedTestLastName").
                        param("hourlyPayment", BigDecimal.TEN.toString())
        ).
                andDo(print()).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/mechanic"));
    }

    @Test
    public void whenSubmitIncorrectMechanicFromEditPage_thenStayOnPage() throws Exception {
        mockMvc.perform(
                post("/mechanic/update/{id}", 100).
                        param("id", "100").
                        param("firstName", "").
                        param("middleName", "changedTestMiddleName").
                        param("lastName", "changedTestLastName").
                        param("hourlyPayment", BigDecimal.TEN.toString())
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(view().name("mechanic/edit"));
    }

    @Test
    public void whenSubmitCorrectMechanicFromCreatePage_thenRedirectToIndex() throws Exception {
        mockMvc.perform(
                post("/mechanic/create").
                        param("firstName", "changedTestFirstName").
                        param("middleName", "changedTestMiddleName").
                        param("lastName", "changedTestLastName").
                        param("hourlyPayment", BigDecimal.TEN.toString())
        ).
                andDo(print()).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/mechanic"));
    }

    @Test
    public void whenSubmitIncorrectMechanicFromCreatePage_thenStayOnPage() throws Exception {
        mockMvc.perform(
                post("/mechanic/create").
                        param("firstName", "").
                        param("middleName", "changedTestMiddleName").
                        param("lastName", "changedTestLastName").
                        param("hourlyPayment", BigDecimal.TEN.toString())
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(view().name("mechanic/edit"));
    }

    @Test
    public void whenDeleteCorrectMechanic_thenReturnOk() throws Exception {
        // given
        Mechanic testMechanic = new Mechanic("testFirstName", "testMiddleName", "testLastName", BigDecimal.ONE);
        Long id = mechanicService.save(testMechanic).getId();

        // expected
        mockMvc.perform(
                delete("/mechanic/delete/{id}", id)
        ).
                andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    public void whenDeleteIncorrectMechanic_thenReturnNotFount() throws Exception {
        mockMvc.perform(
                delete("/mechanic/delete/{id}", -23)
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteWithoutId_thenReturnNotFound() throws Exception {
        mockMvc.perform(
                delete("/mechanic/delete")
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestStatisticsPage_thenReturnStatistics() throws Exception {
        mockMvc.perform(
                get("/mechanic/statistics/{id}", 1)
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attribute("statistics", allOf(
                        hasKey(OrderStatus.SCHEDULED),
                        hasKey(OrderStatus.DONE),
                        hasKey(OrderStatus.ACCEPTED)
                ))).
                andExpect(view().name("mechanic/statistics"));
    }

    @Test
    public void whenRequestStatisticsPageForWrongMechanic_thenReturnNotFound() throws Exception{
        mockMvc.perform(
                get("/mechanic/statistics/{id}", -123)
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestStatisticsPageWithoutId_thenReturnNotFound() throws Exception {
        mockMvc.perform(
                get("/mechanic/statistics")
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }
}
