package com.zenith.spzx;

import com.zenith.spzx.manager.properties.AuthProperties;
import com.zenith.spzx.manager.properties.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties(value = {AuthProperties.class, MinioProperties.class})
@EnableTransactionManagement
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class,args);
    }
}
