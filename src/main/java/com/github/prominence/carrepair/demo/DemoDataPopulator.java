package com.github.prominence.carrepair.demo;

import com.github.javafaker.Faker;
import com.github.prominence.carrepair.model.Client;
import com.github.prominence.carrepair.model.Mechanic;
import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DemoDataPopulator {
    private static final int COUNT = 10;

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

        return args -> {
            demoClientList.forEach(clientService::save);
        };
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

        return args -> {
            demoMechanicList.forEach(mechanicService::save);
        };
    }

}
