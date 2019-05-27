package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.service.ClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Test
    public void whenRequestIndexPage_thenReturnFirstPageOfIndex() throws Exception {
        // given
        Client testClient = new Client("testFirstName", "testMiddleName", "testLastName", "testPhone");
        clientService.save(testClient);

        // expected
        mockMvc.perform(
                get("/client")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("clientList", "totalPages")).
                andExpect(model().attribute("totalPages", 11)). // 100 demo clients + 1 from setup
                andExpect(model().attribute("clientList", hasSize(10))).
                andExpect(view().name("client/index"));
    }

    @Test
    public void whenRequestIndexLastPage_thenReturnLastPageOfIndex() throws Exception {
        // given
        Client testClient = new Client("testFirstName", "testMiddleName", "testLastName", "testPhone");
        clientService.save(testClient);

        // expected
        mockMvc.perform(
                get("/client").param("page", "10")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("clientList", "totalPages")).
                andExpect(model().attribute("totalPages", 11)). // 100 demo clients + 1 from setup
                andExpect(model().attribute("clientList", hasSize(lessThan(10)))).
                andExpect(view().name("client/index"));

    }

    @Test
    public void whenRequestCreateClientPage_thenReturnCreatePageWithEmptyDto() throws Exception {
        mockMvc.perform(
                get("/client/create")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("clientDto")).
                andExpect(view().name("client/edit"));
    }

    @Test
    public void whenRequestEditWithoutId_thenReturnNotFount() throws Exception {
        mockMvc.perform(
                get("/client/edit")
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestEditWithIncorrectId_thenRedirectToIndex() throws Exception {
        mockMvc.perform(
                get("/client/edit/{id}", -34)
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestEditWithCorrectId_thenReturnEditPage() throws Exception {
        mockMvc.perform(
                get("/client/edit/{id}", 1)
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("clientDto")).
                andExpect(view().name("client/edit"));
    }

    @Test
    public void whenSubmitCorrectClientFromEditPage_thenRedirectToIndex() throws Exception {
        mockMvc.perform(
                post("/client/update/{id}", 100).
                        param("id", "100").
                        param("firstName", "changedTestFirstName").
                        param("middleName", "changedTestMiddleName").
                        param("lastName", "changedTestLastName").
                        param("phone", "changedTestPhone")
        ).
                andDo(print()).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/client"));
    }

    @Test
    public void whenSubmitIncorrectClientFromEditPage_thenStayOnPage() throws Exception {
        mockMvc.perform(
                post("/client/update/{id}", 100).
                        param("id", "100").
                        param("firstName", "").
                        param("middleName", "changedTestMiddleName").
                        param("lastName", "changedTestLastName").
                        param("phone", "changedTestPhone")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(view().name("client/edit"));
    }

    @Test
    public void whenSubmitCorrectClientFromCreatePage_thenRedirectToIndex() throws Exception {
        mockMvc.perform(
                post("/client/create").
                        param("firstName", "changedTestFirstName").
                        param("middleName", "changedTestMiddleName").
                        param("lastName", "changedTestLastName").
                        param("phone", "changedTestPhone")
        ).
                andDo(print()).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/client"));
    }

    @Test
    public void whenSubmitIncorrectClientFromCreatePage_thenStayOnPage() throws Exception {
        mockMvc.perform(
                post("/client/create").
                        param("firstName", "").
                        param("middleName", "changedTestMiddleName").
                        param("lastName", "changedTestLastName").
                        param("phone", "changedTestPhone")
        ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(view().name("client/edit"));
    }

    @Test
    public void whenDeleteCorrectClient_thenReturnOk() throws Exception {
        // given
        Client testClient = new Client("testFirstName", "testMiddleName", "testLastName", "testPhone");
        Long id = clientService.save(testClient).getId();

        // expected
        mockMvc.perform(
                delete("/client/delete/{id}", id)
        ).
                andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    public void whenDeleteIncorrectClient_thenReturnNotFount() throws Exception {
        mockMvc.perform(
                delete("/client/delete/{id}", -23)
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteWithoutId_thenReturnNotFound() throws Exception {
        mockMvc.perform(
                delete("/client/delete")
        ).
                andDo(print()).
                andExpect(status().isNotFound());
    }
}
