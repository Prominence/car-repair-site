package com.github.prominence.carrepair.demo;

import com.github.javafaker.Faker;
import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.Client;
import com.github.prominence.carrepair.model.Mechanic;
import com.github.prominence.carrepair.model.Order;
import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import com.github.prominence.carrepair.service.OrderService;
import org.apache.commons.lang3.RandomUtils;
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
    private static final int COUNT = 100;

    private Faker faker = new Faker();

    @Bean
    public CommandLineRunner clientDemoData(ClientService clientService) {
        List<Client> demoClientList = Stream.generate(() -> {
            Client client = new Client();
            client.setFirstName(faker.name().firstName());
            client.setLastName(faker.name().lastName());
            client.setMiddleName(faker.name().username());
            client.setPhoneNo(faker.phoneNumber().phoneNumber());
            System.out.println(client); // demo output
            return client;
        }).limit(COUNT).collect(Collectors.toList());

        return args -> demoClientList.forEach(clientService::save);
    }

    @Bean
    public CommandLineRunner mechanicDemoData(MechanicService mechanicService) {
        List<Mechanic> demoMechanicList = Stream.generate(() -> {
            Mechanic mechanic = new Mechanic();
            mechanic.setFirstName(faker.name().firstName());
            mechanic.setLastName(faker.name().lastName());
            mechanic.setMiddleName(faker.name().username());
            mechanic.setHourlyPayment(BigDecimal.valueOf(faker.number().randomDouble(3, 100, 999)));
            System.out.println(mechanic); // demo output
            return mechanic;
        }).limit(COUNT).collect(Collectors.toList());

        return args -> demoMechanicList.forEach(mechanicService::save);
    }

    @Bean
    public CommandLineRunner orderDemoData(OrderService orderService, ClientService clientService, MechanicService mechanicService) {
        final OrderStatus[] orderStatuses = OrderStatus.values();
        long mechanicsCount = mechanicService.getMechanicCount();
        long clientsCount = clientService.getClientCount();

        List<Order> demoOrderList = Stream.generate(() -> {
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

            final List<Mechanic> mechanicListSlise = mechanicService.findAll(getRandomPageable(mechanicsCount)).getContent();
            final Mechanic randomMechanic = mechanicListSlise.get(RandomUtils.nextInt(0, mechanicListSlise.size()));
            order.setMechanic(randomMechanic);

            System.out.println(order); // demo output
            return order;
        }).limit(COUNT).collect(Collectors.toList());

        return args -> demoOrderList.forEach(orderService::save);
    }

    private Pageable getRandomPageable(long totalRecords) {
        final int size = 10;
        final int totalPages = (int) (totalRecords / size);

        return PageRequest.of(RandomUtils.nextInt(0, totalPages), size);
    }

}
