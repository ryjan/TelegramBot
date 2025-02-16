package org.ryjan.telegram.authorization;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY;
    private final Long JWT_EXPIRATION_TIME;

    public JwtService(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") Long expiration) {
        this.SECRET_KEY = secretKey;
        this.JWT_EXPIRATION_TIME = expiration;
    }

    public String generateToken(Long userId, UserPermissions userGroup) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", Collections.singletonList("ROLE_" + userGroup.getName()));
        String token = createToken(claims, String.valueOf(userId));
        System.out.println("Generated token: " + token);
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // В данном случае это Telegram User ID
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME * 1000 * 60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("roles", List.class);
        System.out.println("Extracted roles: " + roles);
        return roles;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                System.out.println("Token has expired");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }
}
