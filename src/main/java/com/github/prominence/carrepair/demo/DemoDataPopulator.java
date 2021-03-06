package com.github.prominence.carrepair.demo;

import com.github.javafaker.Faker;
import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.domain.Client;
import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.model.domain.Order;
import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import com.github.prominence.carrepair.service.OrderService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DemoDataPopulator {
    private static final int DEMO_ENTRIES_COUNT = 100;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private final Logger logger = LogManager.getLogger(DemoDataPopulator.class);

    private final Faker faker = new Faker();

    @Bean
    public CommandLineRunner clientDemoData(ClientService clientService) {
        Stream<Client> demoClientList = Stream.generate(() -> {
            Client client = new Client();
            client.setFirstName(faker.name().firstName());
            client.setLastName(faker.name().lastName());
            client.setMiddleName(faker.name().username());
            client.setPhoneNo(faker.phoneNumber().phoneNumber());
            return client;
        }).parallel().limit(DEMO_ENTRIES_COUNT);

        return args ->  {
            demoClientList.forEach(clientService::save);
            logger.info("[Demo Data] Populated {} clients.", DEMO_ENTRIES_COUNT);
        };
    }

    @Bean
    public CommandLineRunner mechanicDemoData(MechanicService mechanicService) {
        Stream<Mechanic> demoMechanicList = Stream.generate(() -> {
            Mechanic mechanic = new Mechanic();
            mechanic.setFirstName(faker.name().firstName());
            mechanic.setLastName(faker.name().lastName());
            mechanic.setMiddleName(faker.name().username());
            mechanic.setHourlyPayment(BigDecimal.valueOf(faker.number().randomDouble(3, 100, 999)));
            return mechanic;
        }).parallel().limit(DEMO_ENTRIES_COUNT);

        return args -> {
            demoMechanicList.forEach(mechanicService::save);
            logger.info("[Demo Data] Populated {} mechanics.", DEMO_ENTRIES_COUNT);
        };
    }

    @Bean
    public CommandLineRunner orderDemoData(OrderService orderService, ClientService clientService, MechanicService mechanicService) {
        final OrderStatus[] orderStatuses = OrderStatus.values();
        long mechanicsCount = mechanicService.getMechanicCount();
        long clientsCount = clientService.getClientCount();

        if (mechanicsCount == 0 || clientsCount == 0) return args -> {};

        Stream<Order> demoOrderList = Stream.generate(() -> {
            Order order = new Order();
            order.setOrderStatus(orderStatuses[RandomUtils.nextInt(0, 3)]);
            order.setDescription(faker.lorem().characters(10, 1024));
            order.setTotalPrice(BigDecimal.valueOf(faker.number().randomDouble(4, 100, 9999)));
            order.setCreatedOn(LocalDateTime.ofInstant(faker.date().past(5, TimeUnit.DAYS).toInstant(), ZoneId.systemDefault()));

            if (order.getOrderStatus() == OrderStatus.ACCEPTED) {
                order.setFinishedOn(LocalDateTime.ofInstant(faker.date().future(10, TimeUnit.DAYS).toInstant(), ZoneId.systemDefault()));
            }

            final List<Client> clientListSlice = clientService.findAll(getRandomPageable(clientsCount)).getContent();
            final Client randomClient = clientListSlice.get(RandomUtils.nextInt(0, clientListSlice.size()));
            order.setClient(randomClient);

            final List<Mechanic> mechanicListSlice = mechanicService.findAll(getRandomPageable(mechanicsCount)).getContent();
            final Mechanic randomMechanic = mechanicListSlice.get(RandomUtils.nextInt(0, mechanicListSlice.size()));
            order.setMechanic(randomMechanic);

            return order;
        }).parallel().limit(DEMO_ENTRIES_COUNT);

        return args -> {
            demoOrderList.forEach(orderService::save);
            logger.info("[Demo Data] Populated {} orders.", DEMO_ENTRIES_COUNT);
        };
    }

    private Pageable getRandomPageable(long totalRecords) {
        final int totalPages = (int) (totalRecords / DEFAULT_PAGE_SIZE);
        final PageRequest pageRequest = PageRequest.of(RandomUtils.nextInt(0, totalPages), DEFAULT_PAGE_SIZE);
        logger.trace("[Demo Data] Random page: {}", () -> pageRequest);
        return pageRequest;
    }

}
