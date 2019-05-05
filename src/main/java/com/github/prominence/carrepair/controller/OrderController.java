package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.Order;
import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import com.github.prominence.carrepair.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "/order")
public class OrderController {

    private OrderService orderService;
    private ClientService clientService;
    private MechanicService mechanicService;

    public OrderController(OrderService orderService, ClientService clientService, MechanicService mechanicService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public String index(ModelMap modelMap) {
        Page<Order> orderList = orderService.findAll((Pageable) modelMap.get("pagination"));

        modelMap.addAttribute("orderList", orderList.getContent());
        modelMap.addAttribute("totalPages", orderList.getTotalPages());

        return "order/index";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("clientIdsList", clientService.getAllClientIds());
        model.addAttribute("mechanicIdsList", mechanicService.getAllMechanicIds());
        model.addAttribute("orderStatuses", new OrderStatus[] { OrderStatus.SCHEDULED, OrderStatus.DONE, OrderStatus.ACCEPTED });

        return "order/edit";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Order> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            model.addAttribute("order", orderOptional.get());
            model.addAttribute("clientIdsList", clientService.getAllClientIds());
            model.addAttribute("mechanicIdsList", mechanicService.getAllMechanicIds());
            model.addAttribute("orderStatuses", new OrderStatus[] { OrderStatus.SCHEDULED, OrderStatus.DONE, OrderStatus.ACCEPTED });
            return "order/edit";
        } else {
            // TODO: need to show warning
            return "redirect:/order";
        }
    }

    @PostMapping(value = "/update/{id}")
    public String update(@Valid Order order, BindingResult bindingResult, @PathVariable long id, Model model) {
        if (bindingResult.hasErrors()) {
            order.setId(id); // why should we do this?
            model.addAttribute("clientIdsList", clientService.getAllClientIds());
            model.addAttribute("mechanicIdsList", mechanicService.getAllMechanicIds());
            model.addAttribute("orderStatuses", new OrderStatus[] { OrderStatus.SCHEDULED, OrderStatus.DONE, OrderStatus.ACCEPTED });
            return "order/edit";
        }

        orderService.save(order);
        return "redirect:/order";
    }

    @PostMapping(value = "/create")
    public String save(@Valid Order order, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("clientIdsList", clientService.getAllClientIds());
            model.addAttribute("mechanicIdsList", mechanicService.getAllMechanicIds());
            model.addAttribute("orderStatuses", new OrderStatus[] { OrderStatus.SCHEDULED, OrderStatus.DONE, OrderStatus.ACCEPTED });
            return "order/edit";
        }

        orderService.save(order);
        return "redirect:/order";
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        boolean deleteSuccess = orderService.deleteOrderById(id);

        if (deleteSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
