package org.ivipi.ratel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("org.ivipi.ratel.rockie.domain.mapper")
public class RockieClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RockieClientApplication.class, args);
    }
}
