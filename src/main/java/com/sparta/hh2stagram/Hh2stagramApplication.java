package com.sparta.hh2stagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Hh2stagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(Hh2stagramApplication.class, args);
    }

}
