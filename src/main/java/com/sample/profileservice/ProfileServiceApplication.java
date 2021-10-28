package com.sample.profileservice;

import com.sample.profileservice.client.FacebookClient;
import com.sample.profileservice.client.TwitterClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(clients = {FacebookClient.class, TwitterClient.class})
@ComponentScan(basePackages = "com.sample")
public class ProfileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceApplication.class, args);
    }

}
