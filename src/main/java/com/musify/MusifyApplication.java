package com.musify;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Musify Artist API", version = "1.0", description = "Get Artist Information"))
public class MusifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusifyApplication.class, args);
    }

}
