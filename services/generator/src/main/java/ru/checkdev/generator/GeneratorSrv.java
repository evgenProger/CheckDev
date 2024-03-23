package ru.checkdev.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeneratorSrv {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GeneratorSrv.class);
        application.run();
    }
}
