package com.ifsc.prosigaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProsigaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProsigaaApplication.class, args);
    }

}
