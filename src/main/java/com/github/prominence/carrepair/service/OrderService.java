package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.model.Order;
import com.github.prominence.carrepair.repository.OrderRepository;
import com.github.prominence.carrepair.validation.OrderValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

import java.util.Optional;

@Service
public class OrderService {

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
        return orderRepository.findAll(specification, pageable);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order save(Order client) {
        return orderRepository.save(client);
    }

    public boolean deleteOrderById(Long id) {
        try {
            orderRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getOrderCount() {
        return orderRepository.count();
    }

    public void fetchNestedObjectsAndValidate(Order order, Long clientId, Long mechanicId, BindingResult bindingResult) {
        if (clientId != null) {
            clientService.findById(clientId).ifPresent(order::setClient);
        }
        if (mechanicId != null) {
            mechanicService.findById(mechanicId).ifPresent(order::setMechanic);
        }
        smartValidator.validate(order, bindingResult);
        orderValidator.validate(order, bindingResult);
    }
}
