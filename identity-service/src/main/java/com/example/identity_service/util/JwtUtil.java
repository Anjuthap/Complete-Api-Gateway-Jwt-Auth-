package com.example.identity_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

//    @Value("${jwt.secret}")
//    private String secret;

    private final String secret= "eF2k49bT8vXmYpQwZaD43rC7HsW8A2jXzKqF5uL8VzR6T1y";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    Claims claims = extractAllClaims(token);
    boolean isExpired = isTokenExpired(token);
    boolean isValidUser = username.equals(userDetails.getUsername());
    System.out.println("Token Validity: User = " + isValidUser + ", Expired = " + isExpired);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
//public String generateToken(UserDetails userDetails) {
//    Map<String, Object> claims = new HashMap<>();
//    claims.put("roles", userDetails.getAuthorities().stream()
//            .map(authority -> authority.getAuthority())
//            .collect(Collectors.toList()));
//    return createToken(claims, userDetails.getUsername());
//}
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
