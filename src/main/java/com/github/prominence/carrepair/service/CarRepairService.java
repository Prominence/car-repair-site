package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.repository.ClientRepository;
import com.github.prominence.carrepair.repository.MechanicRepository;
import com.github.prominence.carrepair.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarRepairService {

    private ClientRepository clientRepository;
    private MechanicRepository mechanicRepository;
    private OrderRepository orderRepository;

    @Autowired
    public CarRepairService(ClientRepository clientRepository, MechanicRepository mechanicRepository, OrderRepository orderRepository) {
        this.clientRepository = clientRepository;
        this.mechanicRepository = mechanicRepository;
        this.orderRepository = orderRepository;
    }

    public long getClientCount() {
        return clientRepository.count();
    }

    public long getMechanicCount() {
        return mechanicRepository.count();
    }

    public long getOrderCount() {
        return orderRepository.count();
    }
}
