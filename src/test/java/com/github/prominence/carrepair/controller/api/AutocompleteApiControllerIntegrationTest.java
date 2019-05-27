package com.github.prominence.carrepair.controller.api;

import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class AutocompleteApiControllerIntegrationTest {

    private static final String CLIENT_URI = "/api/autocomplete/client";
    private static final String MECHANIC_URI = "/api/autocomplete/mechanic";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MechanicService mechanicService;

    @Before
    public void setup() {
        Arrays.asList(
                new Client("testCase1", "1", "1", "1"),
                new Client("1", "1", "1", "1"),
                new Client("2", "2", "2", "2"),
                new Client("3", "3", "3", "3"),
                new Client("4", "4", "4", "4"),
                new Client("5", "5", "5", "5"),
                new Client("6", "6", "6", "6"),
                new Client("7", "7", "7", "7"),
                new Client("8", "8", "8", "8"),
                new Client("9", "9", "9", "9")
        ).forEach(clientService::save);

        Arrays.asList(
                new Mechanic("testCase1", "1", "1", BigDecimal.ONE),
                new Mechanic("1", "1", "1", BigDecimal.ONE),
                new Mechanic("2", "2", "2", BigDecimal.ONE),
                new Mechanic("3", "3", "3", BigDecimal.ONE),
                new Mechanic("4", "4", "4", BigDecimal.ONE),
                new Mechanic("5", "5", "5", BigDecimal.ONE),
                new Mechanic("6", "6", "6", BigDecimal.ONE),
                new Mechanic("7", "7", "7", BigDecimal.ONE),
                new Mechanic("8", "8", "8", BigDecimal.ONE),
                new Mechanic("9", "9", "9", BigDecimal.ONE)
        ).forEach(mechanicService::save);
    }

    @Test
    public void whenWithoutQueryForClient_thenReturnBadRequestResponse() throws Exception {
        mockMvc.perform(
                get(CLIENT_URI)
        ).
                andDo(print()).
                andExpect(status().isBadRequest());
    }

    @Test
    public void whenWithQueryForClient_thenReturnResult() throws Exception {
        mockMvc.perform(
                get(CLIENT_URI).param("query", "testCase")
        ).
                andDo(print()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andExpect(status().isOk());
    }

    @Test
    public void whenQueryNotExistingClient_thenReturnEmpty() throws Exception {
        mockMvc.perform(
                get(CLIENT_URI).param("query", "nonExistingClient")
        ).
                andDo(print()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andExpect(content().json("[]", true)).
                andExpect(status().isOk());
    }

    @Test
    public void whenWithoutQueryForMechanic_thenReturnBadRequestResponse() throws Exception {
        mockMvc.perform(
                get(MECHANIC_URI)
        ).
                andDo(print()).
                andExpect(status().isBadRequest());
    }

    @Test
    public void whenWithQueryForMechanic_thenReturnResult() throws Exception {
        mockMvc.perform(
                get(MECHANIC_URI).param("query", "testCase")
        ).
                andDo(print()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andExpect(status().isOk());
    }

    @Test
    public void whenQueryNotExistingMechanic_thenReturnEmpty() throws Exception {
        mockMvc.perform(
                get(MECHANIC_URI).param("query", "nonExistingClient")
        ).
                andDo(print()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andExpect(content().json("[]", true)).
                andExpect(status().isOk());
    }
}
