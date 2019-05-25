package com.github.prominence.carrepair.service;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.model.domain.Order;
import com.github.prominence.carrepair.model.dto.OrderDto;
import com.github.prominence.carrepair.model.mapper.ClientMapper;
import com.github.prominence.carrepair.model.mapper.MechanicMapper;
import com.github.prominence.carrepair.model.mapper.OrderMapper;
import com.github.prominence.carrepair.repository.spec.OrderSpecifications;
import com.github.prominence.carrepair.validation.OrderValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({OrderService.class, ClientService.class, MechanicService.class})
@MockBeans({@MockBean(ClientMapper.class), @MockBean(MechanicMapper.class)})
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MechanicService mechanicService;

    @MockBean
    private OrderValidator orderValidator;

    @MockBean
    private OrderMapper orderMapper;

    private Order testOrder;

    @Before
    public void setup() {
        Client testClient = new Client("1", "1", "1", "1");
        clientService.save(testClient);

        Mechanic testMechanic = new Mechanic("1", "1", "1", BigDecimal.ONE);
        mechanicService.save(testMechanic);

        testOrder = new Order("1", testClient, testMechanic, LocalDateTime.now(), null, BigDecimal.ONE, OrderStatus.SCHEDULED.toString());
        Arrays.asList(
                testOrder,
                new Order("2", testClient, testMechanic, LocalDateTime.now(), LocalDateTime.now().plusDays(1), BigDecimal.TEN, OrderStatus.ACCEPTED.toString()),
                new Order("3", testClient, testMechanic, LocalDateTime.now(), LocalDateTime.now().plusHours(2), BigDecimal.TEN, OrderStatus.DONE.toString())
        ).forEach(orderService::save);
    }

    @Test
    public void whenSaveOrder_thenHeWillBePersisted() {
        // given
        Order testOrder = this.testOrder;

        // when
        testOrder = orderService.save(testOrder);

        // then
        assertThat(testOrder.getId()).isPositive();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void whenSaveInvalidOrder_thenThrowAnException() {
        // given
        Order testOrder = new Order();

        // expected exception
        orderService.save(testOrder);
    }

    @Test
    public void whenSaveOrderDto_thenConvertToOrderAndSave() {
        // given
        OrderDto orderDto = getTestOrderDto();

        when(orderMapper.orderDtoToOrder(orderDto))
                .thenReturn(new Order("123", this.testOrder.getClient(), this.testOrder.getMechanic(), LocalDateTime.now(), null, BigDecimal.TEN, OrderStatus.DONE.toString()));

        // when
        orderService.save(orderDto);

        // then
        verify(orderMapper, times(1)).orderDtoToOrder(orderDto);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void whenSaveInvalidOrderDto_thenThrowAnException() {
        // given
        OrderDto orderDto = new OrderDto();

        when(orderMapper.orderDtoToOrder(orderDto))
                .thenReturn(new Order());

        // expected exception
        orderService.save(orderDto);
    }

    @Test
    public void whenFindAllWithoutSearchParameters_thenReturnAllSavedOrders() {
        assertThat(orderService.findAll(OrderSpecifications.empty(), PageRequest.of(0, 10))).isNotEmpty();
    }

    @Test
    public void whenFindAllWithSearchParameters_thenReturnAllMatches() {
        // variables
        final PageRequest pageable = PageRequest.of(0, 10);

        // when
        Page<Order> foundOrders = orderService.findAll(OrderSpecifications.search(null, null, OrderStatus.DONE), pageable);

        // then
        assertThat(foundOrders.getTotalElements()).isEqualTo(1);

        // when
        foundOrders = orderService.findAll(OrderSpecifications.search("1", null, null), pageable);

        // then
        assertThat(foundOrders.getTotalElements()).isEqualTo(3);

        // when
        foundOrders = orderService.findAll(OrderSpecifications.search("444", null, null), pageable);

        // then
        assertThat(foundOrders.getTotalElements()).isZero();

        // when
        foundOrders = orderService.findAll(OrderSpecifications.search("1", "1", OrderStatus.SCHEDULED), pageable);

        // then
        assertThat(foundOrders.getTotalElements()).isEqualTo(1);

        // when
        foundOrders = orderService.findAll(OrderSpecifications.search("1", "1", OrderStatus.DONE), pageable);

        // then
        assertThat(foundOrders.getTotalElements()).isZero();
    }

    @Test
    public void whenFindById_thenReturnMechanicWithThisId() {
        // given
        Order testOrder = this.testOrder;

        // when
        Long testOrderId = orderService.save(testOrder).getId();
        Order returnedOrder = orderService.findById(testOrderId).get();

        // then
        assertThat(returnedOrder).isNotNull();
        assertThat(returnedOrder.getId()).isEqualTo(testOrderId);
    }

    @Test
    public void whenGetOrderCount_thenReturnPositiveOrderCount() {
        // when
        Long orderCount = orderService.getOrderCount();

        // then
        assertThat(orderCount).isPositive();
    }

    @Test
    public void whenNoSavedOrdersAndGetOrderCount_thenReturnZero() {
        // given
        orderService.deleteAll();

        // when
        Long orderCount = orderService.getOrderCount();

        // then
        assertThat(orderCount).isZero();
    }

    @Test
    public void whenDeleteOrderById_thenItWillBeDeleted() {
        // given
        Order testOrder = new Order("123", this.testOrder.getClient(), this.testOrder.getMechanic(), LocalDateTime.now(), null, BigDecimal.TEN, OrderStatus.DONE.toString());

        // when
        Long savedOrderId = orderService.save(testOrder).getId();
        boolean wasDeleted = orderService.deleteOrderById(savedOrderId);

        // then
        assertThat(wasDeleted).isTrue();
        assertThat(orderService.findById(savedOrderId).isPresent()).isFalse();
    }

    @Test
    public void whenConvertToDto_thenMapperIsParticipating() {
        // given
        Order testOrder = this.testOrder;

        // when
        orderService.convertToDto(testOrder);

        // then
        verify(orderMapper, times(1)).orderToOrderDto(testOrder);
    }

    @Test
    public void whenConvertOrderPageToDtoPage_thenMapperIsParticipating() {
        // given
        Page<Order> orderPage = orderService.findAll(OrderSpecifications.empty(), PageRequest.of(0, 10));

        // when
        orderService.convertToDtoPage(orderPage);

        // then
        verify(orderMapper, times(1)).ordersToOrderDtoList(orderPage.getContent());
    }

    @Test
    public void whenFetchNestedObjectsAndValidate_thenValidationMustBePerformed() {
        // given
        OrderDto orderDto = getTestOrderDto();
        final BindingResult bindingResult = mock(BindingResult.class);

        // when
        orderService.fetchNestedObjectsAndValidate(orderDto, orderDto.getClientId(), orderDto.getMechanicId(), bindingResult);

        // then
        verify(orderValidator, times(1)).validate(orderDto, bindingResult);
    }

    private OrderDto getTestOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setFinishedOnDate(null);
        orderDto.setCreatedOnDate(LocalDateTime.now().toString());
        orderDto.setMechanicId(1L);
        orderDto.setMechanicFirstName("1");
        orderDto.setMechanicMiddleName("1");
        orderDto.setMechanicLastName("1");
        orderDto.setClientId(1L);
        orderDto.setClientFirstName("1");
        orderDto.setClientMiddleName("1");
        orderDto.setClientLastName("1");
        orderDto.setDescription("123");
        orderDto.setOrderStatus(OrderStatus.DONE.toString());
        orderDto.setTotalPrice(BigDecimal.TEN);
        return orderDto;
    }
}
