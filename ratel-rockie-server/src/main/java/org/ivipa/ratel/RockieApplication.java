package org.ivipa.ratel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("org.ivipa.ratel.rockie.domain.mapper")
public class RockieApplication {

    public static void main(String[] args) {
        SpringApplication.run(RockieApplication.class, args);
    }
}
