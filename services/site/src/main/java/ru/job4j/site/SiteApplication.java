package ru.job4j.site;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SiteApplication {
    private static final String SITE = "http://localhost:8080";
    private static final Logger LOG = LoggerFactory.getLogger(SiteApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(SiteApplication.class, args);
        LOG.info("Go to -> :{}", SITE);
    }
}
