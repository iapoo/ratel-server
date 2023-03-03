package org.ivipi.ratel.system;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("org.ivipi.ratel.system.domain.mapper")
public class RatelSystemClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatelSystemClientApplication.class, args);
    }
}
