package com.github.prominence.carrepair.controller.advice;

import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import com.github.prominence.carrepair.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(GlobalInfoAdvice.class)
public class GlobalInfoAdviceUnitTest {

    @Autowired
    private GlobalInfoAdvice globalInfoAdvice;

    @MockBean
    private ClientService clientService;

    @MockBean
    private MechanicService mechanicService;

    @MockBean
    private OrderService orderService;

    @Test
    public void whenAdviceIsFinished_thenModelIsFilled() {
        // given
        when(clientService.getClientCount()).thenReturn(5L);
        when(mechanicService.getMechanicCount()).thenReturn(2L);
        when(orderService.getOrderCount()).thenReturn(6L);
        Model model = new ExtendedModelMap();

        // when
        globalInfoAdvice.addBadgeInfo(model);

        // then
        final Map<String, Object> modelMap = model.asMap();
        assertThat(modelMap.get("clientsCount")).isEqualTo(5L);
        assertThat(modelMap.get("mechanicsCount")).isEqualTo(2L);
        assertThat(modelMap.get("ordersCount")).isEqualTo(6L);
    }
}
