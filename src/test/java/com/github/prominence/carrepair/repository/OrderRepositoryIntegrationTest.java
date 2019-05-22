package com.github.prominence.carrepair.repository;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.model.domain.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class OrderRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Client testClient;
    private Mechanic testMechanic;

    @Before
    public void setup() {
        testClient = new Client("firstName", "middleName", "lastName", "123123123");
        testMechanic = new Mechanic("firstNameM", "middleNameM", "lastNameM", BigDecimal.valueOf(231));

        entityManager.persist(testClient);
        entityManager.persist(testMechanic);
        entityManager.flush();
    }

    @Test
    public void whenFindById_thenReturnOrder() {
        // given
        Order order = new Order("someDescription", testClient, testMechanic, LocalDateTime.now(), null, BigDecimal.valueOf(233), OrderStatus.SCHEDULED.toString());
        long orderKey = (Long) entityManager.persistAndGetId(order);
        entityManager.flush();

        // then
        Optional<Order> foundOrder = orderRepository.findById(orderKey);

        // then
        assertThat(foundOrder).isPresent().map(Order::getId).get().isEqualTo(orderKey);
    }

    @Test
    public void whenFindAllByMechanicId_thenReturnOrdersRelatedToProvidedMechanic() {
        // given
        List<Mechanic> tempMechanics = Arrays.asList(
                new Mechanic("1", "1", "1", BigDecimal.valueOf(1)),
                new Mechanic("2", "2", "2", BigDecimal.valueOf(2))
        );
        tempMechanics.forEach(entityManager::persist);

        Stream.of(
                new Order("testOrder", testClient, tempMechanics.get(0), LocalDateTime.now(), null, BigDecimal.valueOf(123), OrderStatus.DONE.toString()),
                new Order("testOrder", testClient, tempMechanics.get(1), LocalDateTime.now(), null, BigDecimal.valueOf(123), OrderStatus.DONE.toString()),
                new Order("testOrder", testClient, testMechanic, LocalDateTime.now(), null, BigDecimal.valueOf(123), OrderStatus.DONE.toString()),
                new Order("testOrder", testClient, testMechanic, LocalDateTime.now(), null, BigDecimal.valueOf(123), OrderStatus.DONE.toString()),
                new Order("testOrder", testClient, tempMechanics.get(0), LocalDateTime.now(), null, BigDecimal.valueOf(123), OrderStatus.DONE.toString()),
                new Order("testOrder", testClient, testMechanic, LocalDateTime.now(), null, BigDecimal.valueOf(123), OrderStatus.DONE.toString()),
                new Order("testOrder", testClient, tempMechanics.get(1), LocalDateTime.now(), null, BigDecimal.valueOf(123), OrderStatus.DONE.toString()),
                new Order("testOrder", testClient, tempMechanics.get(1), LocalDateTime.now(), null, BigDecimal.valueOf(123), OrderStatus.DONE.toString())
        ).forEach(entityManager::persist);
        entityManager.flush();

        // when
        List<Order> foundOrders = orderRepository.findAllByMechanic_Id(testMechanic.getId());

        // then
        assertThat(foundOrders).size().isEqualTo(3);
        assertThat(foundOrders).allMatch(order -> Objects.equals(order.getMechanic().getId(), testMechanic.getId()));
    }
}
