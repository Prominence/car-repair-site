package com.github.prominence.carrepair.repository;

import com.github.prominence.carrepair.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findAllByMechanic_Id(Long id);
}
