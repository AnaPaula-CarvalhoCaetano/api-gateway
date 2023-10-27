package com.backend.apigateway.Filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;





import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> implements GatewayFilter {

	private static final Logger logger = LoggerFactory.getLogger(CustomFilter.class);

	public CustomFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			String parametro = request.getQueryParams().getFirst("parametro");

			if (parametro == null || parametro.isEmpty()) {

				exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
				return exchange.getResponse().setComplete();
			}

			return chain.filter(exchange);
		};
	}

	public static class Config {
		private String myConfigParam;

		public String getMyConfigParam() {
			return myConfigParam;
		}

		public void setMyConfigParam(String myConfigParam) {
			this.myConfigParam = myConfigParam;
		}
	}

	@Cacheable(value = "meuCache", key = "#exchange.request.uri.toString()")
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
	    long startTime = System.currentTimeMillis(); 

	    logger.info("Received request - Method: {}, URI: {}, Headers: {}", exchange.getRequest().getMethod().name(), exchange.getRequest().getURI(), exchange.getRequest().getHeaders());

	    return chain.filter(exchange).doOnSuccess(aVoid -> {
	        long executionTime = System.currentTimeMillis() - startTime;

	        logger.info("Response status code: {}, Execution time: {}ms", exchange.getResponse().getStatusCode(), executionTime);
	    });
	}



}