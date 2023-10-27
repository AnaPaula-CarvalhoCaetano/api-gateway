package com.backend.apigateway.Filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Component
public class SecurityFilter extends AbstractGatewayFilterFactory<SecurityFilter.Config> {

    public SecurityFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!isRequestSecure(exchange.getRequest())) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }

    private boolean isRequestSecure(ServerHttpRequest request) {
        
        return true; 
    }
}
