package dk.javajolt.security;

import dk.javajolt.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

public class TokenUtils {

    private static final long EXPIRY_MS = 1000 * 60 * 60 * 24;

    private TokenUtils() {}

    public static String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("isAdmin", user.isAdmin())
                .claim("roles", String.join(",", user.getRolesAsStrings()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRY_MS))
                .signWith(getSigningKey())
                .compact();
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static SecretKey getSigningKey() {
        String secret = System.getenv("JWT_SECRET");
        if (secret == null) {
            secret = "bXlkZWZhdWx0c2VjcmV0a2V5Zm9yZGV2ZWxvcG1lbnRvbmx5MTIzNDU2Nzg=";
        }
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}