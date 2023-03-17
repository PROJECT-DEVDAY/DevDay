package com.example.apigatewayservice.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private JWTUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(JWTUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header");
            }

            String accessToken = jwtUtil.resolveToken(request);

            // 유효한 토큰인지 확인합니다.
            if (!jwtUtil.validateToken(accessToken)) {
                return onError(exchange, "JWT token is not valid");
            }

            String userId = jwtUtil.getUserPk(accessToken);
            exchange.getRequest().mutate()
                    .headers(httpHeaders -> httpHeaders.add("userId", userId)).build();

            return chain.filter(exchange);
        };
    }

    // Mono, Flux -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        log.error(err);
        return response.setComplete();
    }

}
