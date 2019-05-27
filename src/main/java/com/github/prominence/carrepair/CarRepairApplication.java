package com.github.prominence.carrepair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @EnableJpaRepositories and @EntityScan annotations are not required as all classes are placed in the main application package or its sub package(s).
@SpringBootApplication
public class CarRepairApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRepairApplication.class, args);
    }

}