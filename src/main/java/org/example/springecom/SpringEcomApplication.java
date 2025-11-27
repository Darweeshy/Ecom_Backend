package org.example.springecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SpringEcomApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEcomApplication.class, args);
    }

}
