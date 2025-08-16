package com.API_gateway.API_gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
	
	@Autowired
	RoutingValidator routingValidator;
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	public AuthenticationFilter() {
		super(Config.class);
	}

	public static class Config {
		// No fields needed for this simple config
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			String path = request.getPath().toString();

			// Check if the request path is one of the secure paths defined by the validator
			if (routingValidator.isSecured.test(request)) {
				// 1. Check for Authorization header
				if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					System.err.println("AuthenticationFilter: Missing Authorization header.");
					return sendErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Missing Authorization header.");
				}

				// 2. Extract and validate the token
				String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
				if (authHeader == null || !authHeader.startsWith("Bearer ")) {
					System.err.println("AuthenticationFilter: Invalid Authorization header format.");
					return sendErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Invalid Authorization header format.");
				}
				String token = authHeader.substring(7);

				// 3. Call the userauth-services to validate the token and get the user's role
				return webClientBuilder.build()
						.get()
						.uri("http://userauth-services/auth/Validatetoken?Authorization=" + token)
						.retrieve()
						.bodyToMono(String.class)
						.flatMap(role -> {
							// This is the updated logic to handle both ADMIN and USER roles for the new paths
							String requestPath = exchange.getRequest().getPath().toString();

							// Allow both USER and ADMIN to access the booking and vehicles APIs
							if ((requestPath.startsWith("/api/bookings") || requestPath.startsWith("/api/vehicles")) &&
								(role.equals("USER") || role.equals("ADMIN"))) {
								System.out.println("AuthenticationFilter: Access granted for role " + role + " to " + requestPath);
								return chain.filter(exchange);
							}
							
							// Allow only ADMIN to access the old Orders path (if it still exists)
							if (requestPath.startsWith("/auth/Orders") && role.equals("ADMIN")) {
								System.out.println("AuthenticationFilter: Access granted for role " + role + " to " + requestPath);
								return chain.filter(exchange);
							}

							// Fallback: Deny access for any other combination
							System.err.println("AuthenticationFilter: Access denied for role " + role + " to " + requestPath);
							return sendErrorResponse(exchange, HttpStatus.FORBIDDEN, "Access denied.");
						})
						.onErrorResume(e -> {
							// Catch all exceptions (e.g., failed token validation or connection issues)
							System.err.println("AuthenticationFilter: Token validation failed: " + e.getMessage());
							return sendErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Unauthorized access.");
						});
			}

			// If the path is not secured, just pass the request through the filter chain
			return chain.filter(exchange);
		};
	}

	private Mono<Void> sendErrorResponse(ServerWebExchange exchange, HttpStatus status, String message) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		response.getHeaders().add("Content-Type", "application/json");
		// Optional: Write a JSON error body for better client feedback
		String errorBody = "{\"status\": " + status.value() + ", \"error\": \"" + message + "\"}";
		return response.writeWith(Mono.just(response.bufferFactory().wrap(errorBody.getBytes())));
	}
}
