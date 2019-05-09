package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.Order;
import com.github.prominence.carrepair.repository.spec.OrderSpecifications;
import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import com.github.prominence.carrepair.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/order")
public class OrderController {

    private OrderService orderService;
    private ClientService clientService;
    private MechanicService mechanicService;
    private SmartValidator smartValidator;

    public OrderController(OrderService orderService, ClientService clientService, MechanicService mechanicService, SmartValidator smartValidator) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.mechanicService = mechanicService;
        this.smartValidator = smartValidator;
    }

    @GetMapping
    public String index(@PageableDefault Pageable pageable, @RequestParam(required = false) String client, @RequestParam(required = false) String description,
                        @RequestParam(required = false) OrderStatus orderStatus, ModelMap modelMap) {
        Page<Order> orderList = orderService.findAll(OrderSpecifications.search(client, description, orderStatus), pageable);

        modelMap.addAttribute("orderList", orderList.getContent());
        modelMap.addAttribute("totalPages", orderList.getTotalPages());
        modelMap.addAttribute("orderStatuses", OrderStatus.values());

        return "order/index";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("clientIdsList", clientService.getAllClientIds());
        model.addAttribute("mechanicIdsList", mechanicService.getAllMechanicIds());
        model.addAttribute("orderStatuses", OrderStatus.values());

        return "order/edit";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Order> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            model.addAttribute("order", orderOptional.get());
            model.addAttribute("orderStatuses", OrderStatus.values());
            return "order/edit";
        } else {
            // TODO: need to show warning
            return "redirect:/order";
        }
    }

    @PostMapping(value = "/update/{id}")
    public String update(Order order, BindingResult bindingResult, @PathVariable long id, Long clientId, Long mechanicId, Model model) {
        if (clientId != null) {
            clientService.findById(clientId).ifPresent(order::setClient);
        }
        if (mechanicId != null) {
            mechanicService.findById(mechanicId).ifPresent(order::setMechanic);
        }
        smartValidator.validate(order, bindingResult);
        if (bindingResult.hasErrors()) {
            order.setId(id); // why should we do this?
            model.addAttribute("orderStatuses", OrderStatus.values());
            return "order/edit";
        }

        orderService.save(order);
        return "redirect:/order";
    }

    @PostMapping(value = "/create")
    public String save(Order order, BindingResult bindingResult, Long clientId, Long mechanicId, Model model) {
        if (clientId != null) {
            clientService.findById(clientId).ifPresent(order::setClient);
        }
        if (mechanicId != null) {
            mechanicService.findById(mechanicId).ifPresent(order::setMechanic);
        }
        smartValidator.validate(order, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("orderStatuses", OrderStatus.values());
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
