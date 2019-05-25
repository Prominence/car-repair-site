package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.model.domain.Order;
import com.github.prominence.carrepair.model.dto.OrderDto;
import com.github.prominence.carrepair.model.mapper.OrderMapper;
import com.github.prominence.carrepair.repository.OrderRepository;
import com.github.prominence.carrepair.validation.OrderValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private OrderValidator orderValidator;
    private OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, ClientService clientService, MechanicService mechanicService,
                        OrderValidator orderValidator, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.clientService = clientService;
        this.mechanicService = mechanicService;
        this.orderValidator = orderValidator;
        this.orderMapper = orderMapper;
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

    public Order save(OrderDto orderDto) {
        return save(orderMapper.orderDtoToOrder(orderDto));
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

    public void deleteAll() {
        orderRepository.deleteAll();
    }

    public long getOrderCount() {
        final long orderCount = orderRepository.count();
        logger.trace("Found {} orders.", () -> orderCount);
        return orderCount;
    }

    // TODO: check possible useless parameters
    public void fetchNestedObjectsAndValidate(OrderDto order, Long clientId, Long mechanicId, BindingResult bindingResult) {
        if (clientId != null) {
            logger.trace("Fetching Client[{}] for {}.", () -> clientId, () -> order);
            clientService.findById(clientId).ifPresent(client -> {
                order.setClientId(client.getId());
                order.setClientFirstName(client.getFirstName());
                order.setClientMiddleName(client.getMiddleName());
                order.setClientLastName(client.getLastName());
            });
        }
        if (mechanicId != null) {
            logger.trace("Fetching Mechanic[{}] for {}.", () -> clientId, () -> order);
            mechanicService.findById(mechanicId).ifPresent(mechanic -> {
                order.setMechanicId(mechanic.getId());
                order.setMechanicFirstName(mechanic.getFirstName());
                order.setClientMiddleName(mechanic.getMiddleName());
                order.setMechanicLastName(mechanic.getLastName());
            });
        }
        orderValidator.validate(order, bindingResult);
    }

    public Page<OrderDto> convertToDtoPage(Page<Order> orderPage) {
        final Page<OrderDto> orderDtoPage = new PageImpl<>(orderMapper.ordersToOrderDtoList(orderPage.getContent()),
                orderPage.getPageable(), orderPage.getTotalElements());
        logger.trace("Dto page: {}.", () -> orderDtoPage);
        return orderDtoPage;
    }

    public OrderDto convertToDto(Order order) {
        return orderMapper.orderToOrderDto(order);
    }
}
