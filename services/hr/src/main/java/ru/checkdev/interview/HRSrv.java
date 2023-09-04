package ru.checkdev.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class HRSrv {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(HRSrv.class);
        application.addListeners(new ApplicationPidFileWriter("./hr.pid"));
        application.run();
    }
}

