package com.backend.apigateway.handler;

import com.backend.apigateway.Error.ErrorResponse;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandlingFilter extends AbstractGatewayFilterFactory<ErrorHandlingFilter.Config>
		implements GatewayFilter {

	public ErrorHandlingFilter() {
		super(Config.class);
	}

	public static class Config {
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			return chain.filter(exchange).onErrorResume(throwable -> {
				if (throwable instanceof ServerWebInputException) {
					exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
					return prepareErrorResponse(exchange, HttpStatus.BAD_REQUEST,
							"Parâmetro inválido: o campo 'nome' é obrigatório.");
				} else {
					exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
					return prepareErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR,
							"Ocorreu um erro interno no servidor.");
				}
			});
		};
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		return chain.filter(exchange);
	}

	private Mono<Void> prepareErrorResponse(ServerWebExchange exchange, HttpStatus status, String errorMessage) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus("error");
		errorResponse.setCode(String.valueOf(status.value()));
		errorResponse.setMessage(errorMessage);

		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

		byte[] responseBytes = serializeToJson(errorResponse);

		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBytes);
		return exchange.getResponse().writeWith(Mono.just(buffer));
	}

	private byte[] serializeToJson(ErrorResponse errorResponse) {
		String json = "{" + "\"status\":\"" + errorResponse.getStatus() + "\"," + "\"code\":\""
				+ errorResponse.getCode() + "\"," + "\"message\":\"" + errorResponse.getMessage() + "\"" + "}";

		return json.getBytes();
	}
}
