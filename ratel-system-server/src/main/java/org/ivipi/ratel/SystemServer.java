package org.ivipi.ratel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("org.ivipi.ratel.system.domain.mapper")
public class SystemServer {

    public static void main(String[] args) {
        SpringApplication.run(SystemServer.class, args);
    }
}
