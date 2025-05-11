package com.shubham.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Date;

@Configuration
public class GatewayConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);

    // Define routes with rate-limiting filter
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        log.info("Creating RouteLocator :{}", new Date());
        return builder.routes()
                .route("question-service", r -> r.path("/question/**")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("lb://questions-service"))
                .build();
    }

    // Define a KeyResolver for identifying users by IP
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String ipAddress = exchange.getRequest().getRemoteAddress().getHostName();
            System.out.println("Resolved IP: " + ipAddress);  // Log IP for debugging
            return Mono.just(ipAddress);
        };
    }


    // Configure RedisRateLimiter Bean
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 10) {
            @Override
            public Mono<Response> isAllowed(String routeId, String id) {
                System.out.println("Checking rate limit for route: " + routeId + " and id: " + id);
                return super.isAllowed(routeId, id);
            }
        };
    }

}
