package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.Mechanic;
import com.github.prominence.carrepair.model.Order;
import com.github.prominence.carrepair.repository.MechanicRepository;
import com.github.prominence.carrepair.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MechanicService {

    private MechanicRepository mechanicRepository;
    private OrderRepository orderRepository;

    public MechanicService(MechanicRepository mechanicRepository, OrderRepository orderRepository) {
        this.mechanicRepository = mechanicRepository;
        this.orderRepository = orderRepository;
    }

    public Page<Mechanic> findAll(Pageable pageable) {
        return mechanicRepository.findAll(pageable);
    }

    public Optional<Mechanic> findById(Long id) {
        return mechanicRepository.findById(id);
    }

    public Mechanic save(Mechanic client) {
        return mechanicRepository.save(client);
    }

    public boolean deleteMechanicById(Long id) {
        try {
            mechanicRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<OrderStatus, Integer> getOrderStatistics(Long mechanicId) {
        Map<OrderStatus, Integer> statistics = new HashMap<>();
        statistics.put(OrderStatus.SCHEDULED, 0);
        statistics.put(OrderStatus.ACCEPTED, 0);
        statistics.put(OrderStatus.DONE, 0);

        List<Order> mechanicOrders = orderRepository.findAllByMechanic_Id(mechanicId);

        mechanicOrders.forEach(order -> statistics.merge(order.getOrderStatus(), 1, Integer::sum));

        return statistics;
    }
}
