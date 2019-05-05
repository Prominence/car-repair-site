package com.github.prominence.carrepair.controller.advice;

import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import com.github.prominence.carrepair.service.OrderService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalInfoAdvice {

    private final ClientService clientService;
    private final MechanicService mechanicService;
    private final OrderService orderService;

    public GlobalInfoAdvice(ClientService clientService, MechanicService mechanicService, OrderService orderService) {
        this.clientService = clientService;
        this.mechanicService = mechanicService;
        this.orderService = orderService;
    }

    @ModelAttribute("globalInfo")
    public void addBadgeInfo(Model model) {
        model.addAttribute("clientsCount", clientService.getClientCount());
        model.addAttribute("mechanicsCount", mechanicService.getMechanicCount());
        model.addAttribute("ordersCount", orderService.getOrderCount());
    }
}
