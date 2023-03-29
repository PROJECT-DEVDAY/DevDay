package com.example.apigatewayservice.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CorsWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        if (headers.containsKey(HttpHeaders.ORIGIN)) {
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, headers.getOrigin());
            response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
            response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
            response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
        }
        return chain.filter(exchange);
    }
}