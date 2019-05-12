package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.model.Order;
import com.github.prominence.carrepair.repository.OrderRepository;
import com.github.prominence.carrepair.validation.OrderValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

import java.util.Optional;

@Service
public class OrderService {
    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private OrderRepository orderRepository;

    private ClientService clientService;
    private MechanicService mechanicService;
    private SmartValidator smartValidator;
    private OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, ClientService clientService, MechanicService mechanicService, SmartValidator smartValidator, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.clientService = clientService;
        this.mechanicService = mechanicService;
        this.smartValidator = smartValidator;
        this.orderValidator = orderValidator;
    }

    public Page<Order> findAll(Specification<Order> specification, Pageable pageable) {
        final Page<Order> orderPage = orderRepository.findAll(specification, pageable);
        logger.trace(orderPage);
        return orderPage;
    }

    public Optional<Order> findById(Long id) {
        final Optional<Order> orderOptional = orderRepository.findById(id);
        logger.debug("{} found by id={}", () -> orderOptional, () -> id);
        return orderOptional;
    }

    public Order save(Order order) {
        final Order orderToSave = orderRepository.save(order);
        logger.trace("[{}] was saved.", () -> orderToSave);
        return orderToSave;
    }

    public boolean deleteOrderById(Long id) {
        try {
            orderRepository.deleteById(id);
            logger.debug("Order[id={}] was deleted.", () -> id);
            return true;
        } catch (Exception e) {
            logger.error("Order[id={}] wasn't deleted. Exception: {}", () -> id, e::getMessage);
            return false;
        }
    }

    public long getOrderCount() {
        final long orderCount = orderRepository.count();
        logger.trace("Found {} orders.", () -> orderCount);
        return orderCount;
    }

    public void fetchNestedObjectsAndValidate(Order order, Long clientId, Long mechanicId, BindingResult bindingResult) {
        if (clientId != null) {
            logger.trace("Fetching Client[{}] for {}.", () -> clientId, () -> order);
            clientService.findById(clientId).ifPresent(order::setClient);
        }
        if (mechanicId != null) {
            logger.trace("Fetching Mechanic[{}] for {}.", () -> clientId, () -> order);
            mechanicService.findById(mechanicId).ifPresent(order::setMechanic);
        }
        smartValidator.validate(order, bindingResult);
        orderValidator.validate(order, bindingResult);
    }
}
