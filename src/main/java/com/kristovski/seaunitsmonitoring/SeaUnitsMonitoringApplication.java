package com.kristovski.seaunitsmonitoring;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class SeaUnitsMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeaUnitsMonitoringApplication.class, args);
    }

}
