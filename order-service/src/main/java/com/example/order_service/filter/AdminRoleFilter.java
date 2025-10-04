package com.example.order_service.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.order_service.service.JwtUtilService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * AdminRoleFilter
 */
@Component
public class AdminRoleFilter implements Filter {

	private JwtUtilService jwtUtilService;

	public AdminRoleFilter(JwtUtilService jwtUtilService) {
		this.jwtUtilService = jwtUtilService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;

		String path = httpReq.getRequestURI();

		if (path.contains("/admin")) {
			String jwt = null;
			Cookie[] cookies = httpReq.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("accessToken".equals(cookie.getName())) {
						jwt = cookie.getValue();
						break;
					}
				}
			}

			if (jwt == null) {
				httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			List<String> roles = jwtUtilService.getRolesClaim(jwt);

			System.out.println("roles :" + roles);
			System.out.println("roles from jwt :" + jwt);

			if (!roles.contains("ADMIN")) {
				httpRes.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}

		}

		chain.doFilter(request, response);

	}
}
