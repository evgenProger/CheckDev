package ru.job4j.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Spring Boot application class used to run application and to configure it.
 * It is annotated by {@link EnableResourceServer} annotation and extends {@link ResourceServerConfigurerAdapter}
 * to use separate OAuth2 authorization server. It's URL must be configured in <code>application.properties</code>
 * file.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@SpringBootApplication
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Application extends ResourceServerConfigurerAdapter {

    /**
     * Application entry point.
     *
     * @param args Application's command-line arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Configure Spring Security to allow everybody's access to GET methods and only authenticated users access to all
     * other methods.
     *
     * @param http security's configuration object.
     * @throws Exception shouldn't be thrown if code is right.
     */
    public void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/**").permitAll()
                .antMatchers("/**").authenticated();
    }
}