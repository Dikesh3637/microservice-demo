package com.example.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.example.api_gateway.client.AuthServiceGrpcClient;

/**
 * JwtAuthenticationFilter
 */
@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

	private AuthServiceGrpcClient authServiceGrpcClient;

	JwtValidationGatewayFilterFactory(AuthServiceGrpcClient authServiceGrpcClient) {
		super(Object.class);
		this.authServiceGrpcClient = authServiceGrpcClient;
	}

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {

			HttpCookie accessTokenCookie = exchange.getRequest().getCookies().getFirst("accessToken");

			if (accessTokenCookie == null) {
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}

			String accessTokenString = accessTokenCookie.getValue();

			return authServiceGrpcClient.validateToken(accessTokenString)
					.flatMap(validationResponse -> {
						// Check if token is valid
						if (!validationResponse.getIsValid()) {
							exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
							return exchange.getResponse().setComplete();
						}

						// If token was refreshed, set the new token as a cookie
						if (validationResponse.getIsRefreshed() &&
								validationResponse.hasRefreshedAccessToken()) {

							// Set refreshed token as a new cookie
							String newToken = validationResponse.getRefreshedAccessToken();
							exchange.getResponse()
									.addCookie(ResponseCookie
											.from("accessToken", newToken)
											.httpOnly(true)
											.secure(false)
											.path("/")
											.maxAge(7 * 24 * 60 * 60)
											.build());
						}

						return chain.filter(exchange);
					})
					.onErrorResume(e -> {
						exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
						return exchange.getResponse().setComplete();
					});
		};
	}
}
