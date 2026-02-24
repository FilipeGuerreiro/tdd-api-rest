package com.filipeguerreiro.tddapirest;

import org.springframework.boot.SpringApplication;

public class TestTddApiRestApplication {

    public static void main(String[] args) {
        SpringApplication.from(TddApiRestApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
