package ru.job4j.site;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@OpenAPIDefinition(info = @Info(title = "Site service", description = "Description"))
@SpringBootApplication
@Slf4j
public class SiteSrv {
    private static final String SITE = "http://localhost:8080";

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SiteSrv.class);
        application.addListeners(new ApplicationPidFileWriter("./site.pid"));
        application.run();
        log.info("Go to -> :{}", SITE);
    }
}
