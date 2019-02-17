package com.github.prominence.carrepair.controller.advice;

import com.github.prominence.carrepair.service.CarRepairService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalInfoAdvice {

    private final CarRepairService carRepairService;

    public GlobalInfoAdvice(CarRepairService carRepairService) {
        this.carRepairService = carRepairService;
    }

    @ModelAttribute("globalInfo")
    public void addBadgeInfo(Model model) {
        model.addAttribute("clientsCount", carRepairService.getClientCount());
        model.addAttribute("mechanicsCount", carRepairService.getMechanicCount());
        model.addAttribute("ordersCount", carRepairService.getOrderCount());
    }
}
