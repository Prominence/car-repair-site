package com.github.prominence.carrepair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.github.prominence.carrepair.repository")
@EntityScan("com.github.prominence.carrepair.model")
@SpringBootApplication
public class CarRepairApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRepairApplication.class, args);
    }

}
// TODO: logging
// TODO: DTO
// TODO: i18n
// TODO: tests
// TODO: big data