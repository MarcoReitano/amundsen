package dev.marcoreitano.master.amundsen;

import org.springframework.boot.SpringApplication;
import org.springframework.modulith.Modulith;
import org.springframework.scheduling.annotation.EnableScheduling;

@Modulith
@EnableScheduling
public class AmundsenApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmundsenApplication.class, args);
    }

}
