package com.example.auth_service.service;

import org.example.auth.grpc.AuthServiceGrpc.AuthServiceImplBase;
import org.example.auth.grpc.ValidationRequest;
import org.example.auth.grpc.ValidationResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AuthGrpcService extends AuthServiceImplBase {

	private JwtUtilsService jwtUtilsService;

	public AuthGrpcService(JwtUtilsService jwtUtilsService) {
		this.jwtUtilsService = jwtUtilsService;

	}

	@Override
	public void validateToken(ValidationRequest request, StreamObserver<ValidationResponse> responseObserver) {

	}

}
