package com.pragmatest.nolt.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableZuulProxy
@SpringBootApplication
public class RoutingGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoutingGatewayApplication.class, args);
    }
}
