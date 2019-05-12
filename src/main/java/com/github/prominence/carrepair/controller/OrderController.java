package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.Order;
import com.github.prominence.carrepair.repository.spec.OrderSpecifications;
import com.github.prominence.carrepair.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/order")
public class OrderController {
    private static final Logger logger = LogManager.getLogger(OrderController.class);

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String index(@PageableDefault Pageable pageable, @RequestParam(required = false) String client, @RequestParam(required = false) String description,
                        @RequestParam(required = false) OrderStatus orderStatus, ModelMap modelMap) {
        Page<Order> orderList = orderService.findAll(OrderSpecifications.search(client, description, orderStatus), pageable);
        logger.trace("Request to open list page for Orders. Returning {} page.", () -> pageable.getPageNumber() + 1);

        modelMap.addAttribute("orderList", orderList.getContent());
        modelMap.addAttribute("totalPages", orderList.getTotalPages());
        modelMap.addAttribute("orderStatuses", OrderStatus.values());

        return "order/index";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {
        logger.trace("Request to open create page for Order.");
        model.addAttribute("order", new Order());
        model.addAttribute("orderStatuses", OrderStatus.values());

        return "order/edit";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        logger.trace("Request to open edit page for Order[{}].", () -> id);
        Optional<Order> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            model.addAttribute("order", orderOptional.get());
            model.addAttribute("orderStatuses", OrderStatus.values());
            return "order/edit";
        } else {
            return "redirect:/order";
        }
    }

    @PostMapping(value = "/update/{id}")
    public String update(Order order, BindingResult bindingResult, @PathVariable long id, Long clientId, Long mechanicId, Model model) {
        logger.trace("Request to save {}.", () -> order);
        orderService.fetchNestedObjectsAndValidate(order, clientId, mechanicId, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.trace("{} has validation {} errors and won't be saved.", () -> order, bindingResult::getErrorCount);
            order.setId(id); // why should we do this?
            model.addAttribute("orderStatuses", OrderStatus.values());
            return "order/edit";
        }

        orderService.save(order);
        return "redirect:/order";
    }

    @PostMapping(value = "/create")
    public String save(Order order, BindingResult bindingResult, Long clientId, Long mechanicId, Model model) {
        logger.trace("Request to create {}.", () -> order);
        orderService.fetchNestedObjectsAndValidate(order, clientId, mechanicId, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.trace("{} has validation {} errors and won't be created.", () -> order, bindingResult::getErrorCount);
            model.addAttribute("orderStatuses", OrderStatus.values());
            return "order/edit";
        }

        orderService.save(order);
        return "redirect:/order";
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        logger.trace("Request to delete Order[{}].", () -> id);
        boolean deleteSuccess = orderService.deleteOrderById(id);
        if (deleteSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
