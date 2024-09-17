package backend.fatec.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

public class JwtHelper {
    private final int MINUTES = 180;
    private final Key secretKey;
    
    public JwtHelper(Key secretKey){
        this.secretKey = secretKey;
    }

    public String generateToken(String email) {
        var now = Instant.now();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return getTokenBody(token).getSubject();
    }

    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getTokenBody(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new RuntimeException("Access denied: " + e.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {
        try{
            Claims claims = getTokenBody(token);
            System.out.println("Body do token obtido");
            return claims.getExpiration().before(new Date());
        }catch(Exception e){
            System.out.println(e.getMessage());
            return true;
        }
    }
}