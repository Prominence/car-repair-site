package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.model.domain.Order;
import com.github.prominence.carrepair.model.dto.MechanicDto;
import com.github.prominence.carrepair.model.mapper.MechanicMapper;
import com.github.prominence.carrepair.repository.MechanicRepository;
import com.github.prominence.carrepair.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MechanicService {
    private static final Logger logger = LogManager.getLogger(MechanicService.class);

    private MechanicRepository mechanicRepository;
    private OrderRepository orderRepository;
    private MechanicMapper mechanicMapper;

    public MechanicService(MechanicRepository mechanicRepository, OrderRepository orderRepository, MechanicMapper mechanicMapper) {
        this.mechanicRepository = mechanicRepository;
        this.orderRepository = orderRepository;
        this.mechanicMapper = mechanicMapper;
    }

    public Page<Mechanic> findAll(Pageable pageable) {
        final Page<Mechanic> mechanicPage = mechanicRepository.findAll(pageable);
        logger.trace(mechanicPage);
        return mechanicPage;
    }

    public Optional<Mechanic> findById(Long id) {
        final Optional<Mechanic> mechanicOptional = mechanicRepository.findById(id);
        logger.debug("{} found by id={}", () -> mechanicOptional, () -> id);
        return mechanicOptional;
    }

    public Mechanic save(Mechanic client) {
        final Mechanic mechanicToSave = mechanicRepository.save(client);
        logger.trace("[{}] was saved.", () -> mechanicToSave);
        return mechanicToSave;
    }

    public Mechanic save(MechanicDto mechanicDto) {
        return save(mechanicMapper.mechanicDtoToMechanic(mechanicDto));
    }

    public boolean deleteMechanicById(Long id) {
        try {
            mechanicRepository.deleteById(id);
            logger.debug("Mechanic[id={}] was deleted.", () -> id);
            return true;
        } catch (Exception e) {
            logger.error("Mechanic[id={}] wasn't deleted. Exception: {}", () -> id, e::getMessage);
            return false;
        }
    }

    public void deleteAll() {
        mechanicRepository.deleteAll();
    }

    public Map<OrderStatus, Integer> getOrderStatistics(Long mechanicId) {
        Map<OrderStatus, Integer> statistics = new HashMap<>();
        statistics.put(OrderStatus.SCHEDULED, 0);
        statistics.put(OrderStatus.ACCEPTED, 0);
        statistics.put(OrderStatus.DONE, 0);

        List<Order> mechanicOrders = orderRepository.findAllByMechanic_Id(mechanicId);

        mechanicOrders.forEach(order -> statistics.merge(order.getOrderStatus(), 1, Integer::sum));
        logger.trace("Mechanic statistics by ID={}: {}", () -> mechanicId, () -> statistics);

        return statistics;
    }

    public long getMechanicCount() {
        final long mechanicCount = mechanicRepository.count();
        logger.trace("Found {} mechanics.", () -> mechanicCount);
        return mechanicCount;
    }

    public List<Mechanic> searchByInitials(String query) {
        final List<Mechanic> allByInitials = mechanicRepository.findAllByInitials(query);
        logger.debug("Found {} mechanics by initials: {}.", allByInitials::size, () -> query);
        return allByInitials;
    }

    public Page<MechanicDto> convertToDtoPage(Page<Mechanic> mechanicPage) {
        final Page<MechanicDto> mechanicDtoPage = new PageImpl<>(mechanicMapper.mechanicsToMechanicDtoList(mechanicPage.getContent()),
                mechanicPage.getPageable(), mechanicPage.getTotalElements());
        logger.trace("Dto page: {}.", () -> mechanicDtoPage);
        return mechanicDtoPage;
    }

    public MechanicDto convertToDto(Mechanic mechanic) {
        return mechanicMapper.mechanicToMechanicDto(mechanic);
    }

    public List<MechanicDto> convertToDtoList(List<Mechanic> mechanicList) {
        return mechanicMapper.mechanicsToMechanicDtoList(mechanicList);
    }
}
