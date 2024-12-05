package com.example.api_gateway.filter;

import com.example.api_gateway.exception.RoleNotMatchedException;
import com.example.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;

import java.util.List;


@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(){ super(Config.class);}

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {

                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing Authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
//                try {
//                    //REST call to AUTH service
//                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
                jwtUtil.validateToken(authHeader);

                String role = jwtUtil.extractRoles(authHeader);
                System.out.println("Extracted Role: " + role);
//                    exchange.getRequest().mutate().header("role", role).build();

                if (validator.isAdminAccess.test(exchange.getRequest())) {
                    if (role.contains("ROLE_ADMIN")) {
                        logger.info("User With Role " + role + " Is Accessing The Api " + exchange.getRequest().getURI());
                    } else {
                        logger.error("User With Role " + role + "  Don't Have Access To This Api " + exchange.getRequest().getURI());
                        throw new RoleNotMatchedException("Only Admin Can access " + exchange.getRequest().getURI());
                    }
                }
                if (validator.isUserAccess.test(exchange.getRequest())) {
                    if (role.contains("ROLE_USER")) {
                        logger.info("User With Role " + role + " Is Accessing The Api " + exchange.getRequest().getURI());
                    } else {
                        logger.error("User With Role " + role + "  Don't Have Access To This Api " + exchange.getRequest().getURI());
                        throw new RoleNotMatchedException("Only User Can Access " + exchange.getRequest().getURI());
                    }
                }
                if(validator.isCommonAccess.test(exchange.getRequest())){
                    if (role.contains("ROLE_ADMIN") || role.contains("ROLE_USER")) {
                        logger.info(role + " access granted for common endpoint: " + exchange.getRequest().getURI());
                    }  else {
                        logger.error("?????????>>>>>>>>>>>>");
                    throw new RoleNotMatchedException("Unauthorized role for: " + exchange.getRequest().getURI());
                }
            }}
//            catch(Exception e){
//                System.out.println("invalid access...!");
//                throw new RuntimeException("Unauthorized access to application");
//            }}
            return chain.filter(exchange);
        });
    }
    public static class Config {

    }
}


