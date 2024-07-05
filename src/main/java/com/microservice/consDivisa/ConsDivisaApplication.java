package com.microservice.consDivisa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ConsDivisaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsDivisaApplication.class, args);
    }

}
