package com.zenith.spzx.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.zenith.spzx.common","com.zenith.spzx.user"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
