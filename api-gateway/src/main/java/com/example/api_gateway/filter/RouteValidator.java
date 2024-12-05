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

    public static final List<String> adminApiEndpoints = List.of(
            "/api/admin/create/bank",
            "/api/admin/put/bank"
    );
    public Predicate<ServerHttpRequest> isAdminAccess =
            request -> adminApiEndpoints
                    .stream()
                    .anyMatch(uri -> new AntPathMatcher().match(uri, request.getURI().getPath()));

    public static final List<String> userApiEndpoints =List.of(
            "/api/user/view/feedback"
    );
    public Predicate<ServerHttpRequest> isUserAccess =
            request -> userApiEndpoints
                    .stream()
                    .anyMatch(uri -> new AntPathMatcher().match(uri, request.getURI().getPath()));

    public static final List<String> commonApiEndpoints = List.of(
            "/api/view/bank"
    );
    public Predicate<ServerHttpRequest> isCommonAccess =
            request -> commonApiEndpoints.contains(request.getURI().getPath());
}

