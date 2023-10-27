package com.backend.apigateway.Config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
			.route("restaurante_online_route", r -> r
				    .path("/restaurante_online/**")
				    .filters(f -> f.filter(new com.backend.apigateway.Filter.CustomFilter()))
				    .uri("lb://restaurante_online"))		
			.build();
		
	}

	
}
