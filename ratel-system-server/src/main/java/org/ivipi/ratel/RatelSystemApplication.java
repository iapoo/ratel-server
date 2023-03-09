package org.ivipi.ratel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableScheduling
@MapperScan("org.ivipi.ratel.system.domain.mapper")
public class RatelSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatelSystemApplication.class, args);
    }
}
