package com.sanapyeong.mtvs_3rd_dreamplanet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.sanapyeong")
@EnableJpaAuditing
public class Mtvs3rdDreamPlanetApplication {

    public static void main(String[] args) {
        SpringApplication.run(Mtvs3rdDreamPlanetApplication.class, args);
    }

}
