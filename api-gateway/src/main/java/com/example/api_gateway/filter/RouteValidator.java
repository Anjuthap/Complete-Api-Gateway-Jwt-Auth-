package com.example.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/signup", //ignore this no token needed if the request is sent through api gateway
            "/auth/login" //ignore this no token needed if the request is sent through api gateway
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    public static final List<String> createBankEndpoints = List.of(
            "/api/admin/create/bank"

    );
    public Predicate<ServerHttpRequest> isCreateBankAccess =
            request -> createBankEndpoints
                    .stream()
                    .anyMatch(uri -> new AntPathMatcher().match(uri, request.getURI().getPath()));

    public static final List<String> updateBankEndpoints =List.of(
            "/api/admin/update/bank",
            "/api/admin/update/logo"
    );
    public Predicate<ServerHttpRequest> isUpdateBankAccess  =
            request -> updateBankEndpoints
                    .stream()
                    .anyMatch(uri -> new AntPathMatcher().match(uri, request.getURI().getPath()));

    public static final List<String> viewApiEndpoints = List.of(
            "/api/view/bank"
    );
    public Predicate<ServerHttpRequest> isViewBankAccess =
            request -> viewApiEndpoints.contains(request.getURI().getPath());
}

