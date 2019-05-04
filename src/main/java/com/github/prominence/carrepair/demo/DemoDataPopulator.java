package com.github.prominence.carrepair.demo;

import com.github.javafaker.Faker;
import com.github.prominence.carrepair.model.Client;
import com.github.prominence.carrepair.repository.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DemoDataPopulator {
    private static final int COUNT = 10;

    @Bean
    public CommandLineRunner demoData(ClientRepository clientRepository) {
        Faker faker = new Faker();

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
            demoClientList.forEach(clientRepository::save);
        };
    }

}
