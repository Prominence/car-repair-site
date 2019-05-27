package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.controller.advice.GlobalInfoAdvice;
import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import com.github.prominence.carrepair.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({HomeController.class, GlobalInfoAdvice.class})
@MockBeans({@MockBean(ClientService.class), @MockBean(MechanicService.class), @MockBean(OrderService.class)})
public class HomeControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenCallHomePage_thenReturnHomePage() throws Exception {
        mockMvc.perform(
                get("/")
        ).
                andDo(print()).
                andExpect(status().isOk()
                );
    }
}
