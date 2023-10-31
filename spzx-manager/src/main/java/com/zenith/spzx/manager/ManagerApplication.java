package com.zenith.spzx.manager;

import com.zenith.spzx.common.log.annotation.EnableLogAspect;
import com.zenith.spzx.manager.properties.AuthProperties;
import com.zenith.spzx.manager.properties.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties(value = {AuthProperties.class, MinioProperties.class})
@EnableTransactionManagement
@EnableScheduling
@EnableLogAspect
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class,args);
    }
}
