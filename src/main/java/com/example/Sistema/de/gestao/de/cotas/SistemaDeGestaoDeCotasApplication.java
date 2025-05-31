package com.example.Sistema.de.gestao.de.cotas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"controller", "service", "config"})
@EntityScan("model")
@EnableJpaRepositories("repository")
public class SistemaDeGestaoDeCotasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaDeGestaoDeCotasApplication.class, args);
    }
}
